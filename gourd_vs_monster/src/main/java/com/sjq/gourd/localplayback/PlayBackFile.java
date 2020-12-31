package com.sjq.gourd.localplayback;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.util.Date;

public class PlayBackFile {
    private final SimpleStringProperty fileName; // 回放文件的文件名
    private final SimpleStringProperty fileCreateDate; // 回放文件创建的时间
    private final String filePath;
    private final File file;

    public PlayBackFile(String fileName, Date date, String filePath) {
        this.fileName = new SimpleStringProperty(fileName);
        this.fileCreateDate = new SimpleStringProperty(date.toString());
        this.filePath = filePath;
        this.file = new File(filePath);
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getFileCreateDate() {
        return fileCreateDate.get();
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }
}