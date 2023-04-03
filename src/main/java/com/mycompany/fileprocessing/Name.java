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
public class Name {
    public ArrayList<Entrys> nameFilter(ArrayList<Entrys> list, String keyWord) {
        ArrayList<Entrys> rList = new ArrayList<>();
        for (Entrys x : list) {
            try {
                if (x.file.getName().toLowerCase().contains(keyWord.toLowerCase())) {
                    rList.add(x);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rList;
    }
}
