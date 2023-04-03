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

public class Count {
      public List<Entrys> filter(List<Entrys> entries, String key, int min) throws IOException {
        List<Entrys> output = new ArrayList<>();
        for (Entrys entry : entries) {
            if (!entry.file.isFile()) {  // If entry is not a file, skip it
                continue;
            }
            BufferedReader reader = new BufferedReader(new FileReader(entry.file));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count += countOccurrences(line, key);
                if (count >= min) {  // If count is at least min, add file to output and break out of loop
                    output.add(entry);
                    break;
                }
            }
            reader.close();
        }
        return output;
    }
   
    private static int countOccurrences(String str, String subStr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }
        return count;
    }
   
}
