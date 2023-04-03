/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

import java.io.File;

/**
 *
 * @author zmcmu
 */
public class LocalEntry extends Entrys {

    String filePath;

    public LocalEntry(String filePath) {
        this.filePath = filePath;
        setFile(this.filePath);
        location = "local";
        if (file.isFile()) {
            type = "file";
        } else if (file.isDirectory()) {
            type = "directory";
        }
        name = this.file.getName();
    }

    final public void setFile(String filePath) {
        try {
            this.file = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
