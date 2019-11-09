/*
 * Nothing to see here
 */
package fxmlfileviewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileSystemView;
import resources.FileClass;

/**
 *
 * @author Kyle
 */
public class FXMLTab1Controller implements Initializable {

    /* Get system info to pull home directory */
    FileSystemView newView = new FileSystemView() {
        @Override
        public File createNewFolder(File containingDir) throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

    private double totalFiles = 0;
    private double count = 0;
    private File pathDir;
    private String pathStr;
    private List<File> dirList;
    private Stage primaryStage;
    private Pane pn;
    private static FXMLTab1Controller instance;
    private Task copyWorker;
    
    @FXML
    private Button scanBtn;
    @FXML
    private Button cmdBtn;
    
    @FXML 
    private AnchorPane aPane;
    @FXML
    private Label label;
    @FXML
    private TextField dirEntry;
    @FXML
    private ProgressBar progBar;
    @FXML
    private ProgressIndicator progInd;

    /* TreeTableView */
    @FXML
    private TreeTableView<FileClass> treeFileView;
    @FXML
    private TreeTableColumn<FileClass, String> fileName;
    @FXML
    private TreeTableColumn<FileClass, String> fileMb;
    @FXML
    private TreeTableColumn<FileClass, String> fileGb;
    @FXML
    private TreeTableColumn<FileClass, String> filePercent;
    
    /* BarChart */
    @FXML
    private BarChart bChart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
    private XYChart.Series series1;
    private BarChart.Series<String, Number> series2;
    private ObservableList<XYChart.Series<String, Number>> barChartData;
    
    /* PieChart */
    @FXML
    protected PieChart pChart;
    protected ObservableList<PieChart.Data> pieChartData;
    

    @FXML
    private void handleScan(ActionEvent event) {
        System.out.println("Scan given path");
        pathDir = new File(dirEntry.getText());
        
        Task task3 = new Task<Void>() {
            @Override 
            public Void call() {
                getFiles(dirEntry.getText());
                return null;
            }
        };
        
        new Thread(copyWorker).start();

//        count = 0;
        
        Runnable displayStuff = new Runnable() {
            @Override
            public void run() {
                //Display data in the tree
                displayTreeView(pathDir);
                //Display data in the graphs
                displayGraphs();
            }

        };
        
        new Thread(displayStuff).start();

        label.setText("Hello World!");
    }

    @FXML
    private void handleClear(ActionEvent event) {
        System.out.println("You clicked me!");
        bChart.getData().clear();
        pChart.getData().clear();
        treeFileView.setRoot(null);
        progBar.setProgress(0);
        label.setText("Hello World!");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        System.out.println("You clicked me!");
        dirEntry.setText("R:\\\\PROGRAMS\\\\Java");
        label.setText("Hello World!");
    }
    
    @FXML
    private void handleBrowse(ActionEvent event) {
        System.out.println("You clicked me!");
//        browseDir(pn);
        Stage st = (Stage)aPane.getScene().getWindow();
        browseDir(st);
        dirEntry.setText(pathStr);
    }
    
    @FXML
    private void handleCmd(ActionEvent event) {
        System.out.println("Open a command prompt in the selected directory");
                    try {
                        if (pathDir.getAbsolutePath() != null) {
                            Runtime.getRuntime().exec(new String[]{"cmd", "/K", "Start dir " + pathDir.getAbsolutePath()});
                        } else {
                            System.out.println("can't access that directory");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLTab1Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }
    
    public static FXMLTab1Controller getInstance() {
		return instance;
	}
    
    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < count; i++) {
                    Thread.sleep(50);
                    updateMessage("2000 milliseconds");
                    updateProgress(i + 1, 10);
                }
                return true;
            }
        };
    }

    /**
     * Recursively search though file structure. Depth-First search
     * @param file
     * @param parent
     */
    public void createTree(File file, TreeItem<FileClass> parent) {
        count++;
        FileClass newF = new FileClass(file);
        if (file.isDirectory()) {
            TreeItem<FileClass> treeItem = new TreeItem<>(newF);
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        } else {
            parent.getChildren().add(new TreeItem<>(new FileClass(file)));
        }
    }

    /**
     * display tree view to page
     * @param inputDirectoryLocation
     */
    public void displayTreeView(File inputDirectoryLocation) {
        // Creates the root item.
        TreeItem<FileClass> rootItem = new TreeItem<>();
        // Hides the root item of the tree view.
        treeFileView.setShowRoot(false);
        treeFileView.setStyle("-fx-cell-size: 50px;");
        //Initial list of files
        File fileList[] = inputDirectoryLocation.listFiles();
        // create tree
        for (File file : fileList) {
            FileClass nf = new FileClass(file);
            TreeItem tItem = new TreeItem<>();
            rootItem.getChildren().add(tItem);
            createTree(file, rootItem);
        }
        treeFileView.setRoot(rootItem);
    }

    /**
     * Gets the total files that will be displayed. 
     * Used to update the progress bar
     * @param dirPath
     */
    private void getFiles(String dirPath) {
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (files != null) {
            for (File file1 : files) {
                count++;
                File file = file1;
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath());
                }
            }
        }
    }
    
    private void displayGraphs(){
        
        HashMap<String, Integer> sortMap = new HashMap<>();
        dirList = findDirs(pathDir);
        for(File f : dirList){
            FileClass newF = new FileClass(f);
            sortMap.put(newF.getName(), (int)newF.getBy());
        }
        
        sortMap = sortByValue(sortMap);
        for (Map.Entry<String, Integer> entry : sortMap.entrySet()) {
            series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        barChartData.add(series1);
        bChart.setData(barChartData);
        pChart.setData(pieChartData);
        
    }
    
    /**
     * Add a directory browsing system so the user can 
     * easily pick the directory they want to scan.
     * @param primaryStage 
     */
    private void browseDir(Stage primaryStage) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();
            pathStr = selectedDirectory.getPath();
            dirEntry.setText(pathStr);
        }
    }

    /**
     * Method to sort the file size data,
     * Used for bar chart and pie chart
     * @param hm
     * @return 
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    /**
     * Create list of files in a directory.
     * Used for adding to graphs
     * @param directory
     * @return 
     */
    private List<File> findDirs(File directory){
        File[] listOfFiles = directory.listFiles(); 
        List<File> dirsToReturn = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
//                    numDirs++;
                    dirsToReturn.add(listOfFiles[i]);
                }
            }
        return dirsToReturn;
    }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        scanBtn.setDefaultButton(true);
        cmdBtn.setDefaultButton(false);

        //Create thread for progress bar, (not working)
        Task task2 = new Task<Void>() {
            @Override 
            public Void call() {
                for (int i = 1; i <= totalFiles; i++) {
                    updateProgress(i, totalFiles);
                }
                return null;
            }
        };
        
        progBar.setProgress(0.0);
        progInd.setProgress(0.0);
        copyWorker = createWorker();
        progBar.progressProperty().unbind();
        progInd.progressProperty().unbind();
        progBar.progressProperty().bind(copyWorker.progressProperty());
        progInd.progressProperty().bind(copyWorker.progressProperty());
        copyWorker.messageProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        System.out.println(newValue);
                        label.setText(newValue);
                    }
                });
//        new Thread(copyWorker).start();
        
//        progBar.progressProperty().bind(task2.progressProperty());
//        new Thread(task2).start();

        //link columns with values in FileClass
        fileName.setCellValueFactory(new TreeItemPropertyValueFactory<FileClass, String>("name"));
        fileName.setMinWidth(150);
        fileMb.setCellValueFactory(new TreeItemPropertyValueFactory<FileClass, String>("mb"));
        fileMb.setMinWidth(100);
        fileGb.setCellValueFactory(new TreeItemPropertyValueFactory<FileClass, String>("gb"));
        fileGb.setMinWidth(50);
        
//        treeFileView.getStyleClass().add("treeTable");
        
        //Bar Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChartData = FXCollections.observableArrayList();
        series1 = new BarChart.Series<String, Number>();
        series1.setName("Data");
        bChart.setAnimated(false);
        
        x = new CategoryAxis();
        y = new NumberAxis();
        x.minWidth(5);
        x.maxWidth(5);
        bChart.setBarGap(8);
        bChart.setCategoryGap(12);
        
        //Pie Chart
        pieChartData = FXCollections.observableArrayList();
        barChartData = FXCollections.observableArrayList();
        pChart.setTitle("Data Sizes");

        double maxBarWidth = 40;
        double minCategoryGap = 10;
    }

}
