package resources;

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kyle
 */
public class FileClass {

    private File myFi;
    private String name;
    private StringProperty nameProperty;
    private StringProperty byProperty;
    private StringProperty mbProperty;
    private StringProperty gbProperty;
    private StringProperty tbProperty;
    private StringProperty isDirProperty;
    private StringProperty isFilProperty;
    private long by;
    private long mb;
    private long gb;
    private long tb;
    private String isDir = "F";
    private String isFil = "F";

    public FileClass(File f) {
        if (f != null) {
            myFi = f;
        } else {
            throw new NullPointerException("File = null");
        }

        setBy();
        setMb();
        setGb();
        setTb();
        setName();

    }

    public static long getFileFolderSize(File dir) {
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += getFileFolderSize(file);
                }
            }
        } else if (dir.isFile()) {
            size += dir.length();
        }
        return size;
    }

    /**
     * **********
     * Getters
     */
    public String getName() {
        return this.name;
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public StringProperty getByProperty() {
        return byProperty;
    }

    public StringProperty getMbProperty() {
        return mbProperty;
    }

    public StringProperty getGbProperty() {
        return gbProperty;
    }

    public StringProperty getTbProperty() {
        return tbProperty;
    }

    public StringProperty getIsDirProperty() {
        return isDirProperty;
    }

    public StringProperty getIsFilProperty() {
        return isFilProperty;
    }

    public String getByString() {
        return String.valueOf(by);
    }

    public String getMbString() {
        return String.valueOf(mb);
    }

    public String getGbString() {
        return String.valueOf(gb);
    }

    public String getTbString() {
        return String.valueOf(tb);
    }

    public long getBy() {
        return this.by;
    }

    public long getMb() {
        return this.mb;
    }

    public long getGb() {
        return this.gb;
    }

    public long getTb() {
        return this.tb;
    }

    public String ifDir() {
        return this.isDir;
    }

    public String ifFil() {
        return this.isFil;
    }

    /**
     * *****************
     * Private Setters
     */
    private void setBy() {
//        this.by = this.myFi.getTotalSpace()/1024;
        this.by = Math.abs(getFileFolderSize(this.myFi));
    }

    private void setMb() {
        this.mb = Math.abs(this.by / 1024 / 1024);
    }

    private void setGb() {
        this.gb = Math.abs(this.mb / 1024);
    }

    private void setTb() {
        this.tb = Math.abs(this.gb / 1024);
    }

    private void isDirectory() {
        if (myFi.isDirectory()) {
            isDir = "T";
        } else {
            isDir = "F";
        }
    }

    private void isFile() {
        if (myFi.isFile()) {
            isFil = "T";
        } else {
            isFil = "F";
        }
    }

    private void setName() {
        this.name = myFi.getName();
    }

    private void setNameProperty() {
        getNameProperty().set(this.name);
    }

    private void setByProperty() {
        getByProperty().set(String.valueOf(this.by));
    }

    private void setMbProperty() {
        getMbProperty().set(String.valueOf(this.mb));
    }

    private void setGbProperty() {
        getGbProperty().set(String.valueOf(this.gb));
    }

    private void setTbProperty() {
        getTbProperty().set(String.valueOf(this.tb));
    }

    private void setisDirProperty() {
        getIsDirProperty().set(this.isDir);
    }

    private void setisFilProperty() {
        getIsFilProperty().set(this.isFil);
    }

}
