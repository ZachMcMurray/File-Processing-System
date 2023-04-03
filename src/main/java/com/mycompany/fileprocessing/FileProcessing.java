/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

//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
package com.mycompany.fileprocessing;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * @author zmcmu
 */
public class FileProcessing {

    public static void main(String[] args) {
        ArrayList<Entrys> inputList = new ArrayList<>();
        ArrayList<Entrys> outputList = new ArrayList<>();
        String key = null;
        String operator = null;
        long length = 0;
        int min=0;
        int lines=0;
        int max=0;
        String suffix=null;
        
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\zmcmu\\OneDrive\\Documents\\NetBeansProjects\\FileProcessing\\jasonJsonV2.txt")) {
            Object obj = jsonParser.parse(reader);

            JSONObject j = (JSONObject) obj;
            JSONArray processing_elements = (JSONArray) j.get("processing_elements");
            JSONObject firstElement = (JSONObject) processing_elements.get(0);
            JSONArray input_entries = (JSONArray) firstElement.get("input_entries");

            for (int x = 0; x < input_entries.size(); x++) {
                JSONObject temp = (JSONObject) input_entries.get(x);

                if (temp.get("type").equals("local")) {
                    try {
                        String path = (String) temp.get("path");
                        inputList.add(new LocalEntry(path));
                    } catch (Exception d) {
                        d.printStackTrace();
                    }
                } else if (temp.get("type").equals("remte")) {
                    String repoId = (String) temp.get("repositoryId");
                    long entNum = (long) temp.getAsNumber("entryId");
                    inputList.add(new RemteEntry(repoId, (int) entNum));
                }
            }

            for (Entrys c : inputList) {
                System.out.println(c.name);
            }

            JSONArray parameters;
            for (int x = 0; x < processing_elements.size(); x++) {
                JSONObject tempElement = (JSONObject) processing_elements.get(x);
                String processingType = tempElement.getAsString("type");
                switch (processingType) {
                    case "Name Filter":
                        System.out.println("Name filter");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Key":
                                    key = (String) pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        Name nFilter = new Name();
                        outputList = new ArrayList(nFilter.nameFilter(inputList, key));
                        break;

                    case "Length Filter":
                        System.out.println("Length Filter");

                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Operator":
                                    operator = pN.getAsString("value");
                                    break;
                                case "Length":
                                    length = (long) pN.getAsNumber("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        Length lFilter = new Length();
                        outputList = new ArrayList(lFilter.lenghtFilter(inputList, operator, length));
                        break;

                    case "Content Filter":
                        System.out.println("Content Filter");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Key":
                                    key = (String) pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        ContentFilter cFFilter = new ContentFilter();
                        outputList = new ArrayList(cFFilter.filter(inputList, key));

                        break;
                    case "Count Filter":
                        System.out.println("Count Filter");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Key":
                                    key = pN.getAsString("value");
                                    break;
                                case "Min":
                                    min = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        
                        Count cFilter = new Count();
                        outputList = new ArrayList(cFilter.filter(inputList, key, min));
                        break;
                    case "Split":
                        System.out.println("Split");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Lines":
                                    lines = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        break;
                    case "List":
                        System.out.println("List");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Max":
                                    max = pN.getAsNumber("value").intValue();
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        break;
                    case "Rename":
                        System.out.println("Rename");
                        parameters = (JSONArray) tempElement.get("parameters");
                        for (int y = 0; y < parameters.size(); y++) {
                            JSONObject pN = (JSONObject) parameters.get(y);
                            String pName = (String) pN.getAsString("name");
                            switch (pName) {
                                case "Suffix":
                                    suffix = pN.getAsString("value");
                                    break;
                                default:
                                    System.out.println("name in json file does not line up with parameters");
                            }
                        }
                        break;
                    case "Print":
                        System.out.println("Print");
                        break;
                    default:
                        break;
                }

                for (Entrys c : outputList) {
                    System.out.println(c.name);
                }

            }

        } catch (Exception a) {
            a.printStackTrace();
        }
    }
}
