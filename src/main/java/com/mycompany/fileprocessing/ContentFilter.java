/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zmcmu
 */

public class ContentFilter {
    public List<Entrys> filter(List<Entrys> entries, String key) throws IOException {
        List<Entrys> output = new ArrayList<>();
        for (Entrys entry : entries) {
            if (!entry.file.isFile()) {  // If entry is not a file, skip it
                continue;
            }
            BufferedReader reader = new BufferedReader(new FileReader(entry.file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(key)) {  // If line contains key, add file to output and break out of loop
                    output.add(entry);
                    break;
                }
            }
            reader.close();
        }
        return output;
    }
}

