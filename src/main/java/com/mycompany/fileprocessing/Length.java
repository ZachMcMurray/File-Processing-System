/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author zmcmu
 */
public class Length {
    public ArrayList<Entrys> lenghtFilter(ArrayList<Entrys> list, String operater, long length) {
        ArrayList<Entrys> rList = new ArrayList<>();
        for (Entrys x : list) {
            try {
                if (x.file.isFile()) {
                    if (operater.equalsIgnoreCase("EQ")) {
                        if (x.file.length() == length) {
                            rList.add(x);
                        }
                    } else if (operater.equalsIgnoreCase("NEQ")) {
                        if (x.file.length() != length) {
                            rList.add(x);
                        }
                    } else if (operater.equalsIgnoreCase("GT")) {
                        if (x.file.length() > length) {
                            rList.add(x);
                        }
                    } else if (operater.equalsIgnoreCase("GTE")) {
                        if (x.file.length() >= length) {
                            rList.add(x);
                        }
                    } else if (operater.equalsIgnoreCase("LT")) {
                        if (x.file.length() < length) {
                            rList.add(x);
                        }
                    } else if (operater.equalsIgnoreCase("LTE")) {
                        if (x.file.length() <= length) {
                            rList.add(x);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("shit really fucked");
            }
        }
        return rList;
    }
}
