/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlfileviewer;

import resources.FileClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kyle
 */
public class FXMLFileViewer extends Application {
    
    protected Stage newStage;
    protected Scene scene;
    static FXMLTab1Controller tab1Controller;
    static FXMLTab2Controller tab2Controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        newStage = stage;
        tab1Controller = (FXMLTab1Controller)loader.getController();
        tab2Controller = (FXMLTab2Controller)loader.getController();
//        Parent root = loader.load();
        scene = new Scene(root);
        scene.getStylesheets().add("fxmlfileviewer/styleCss.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
