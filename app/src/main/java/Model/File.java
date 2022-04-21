package Model;

import java.io.Serializable;

public class File  implements Serializable {
    private String fileName;
    private String filePath;

    public File(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public File() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
