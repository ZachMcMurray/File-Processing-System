/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

import static java.lang.Math.abs;
import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author zmcmu
 */
public class RemteEntry extends Entrys {

    private final String servicePrincipalKey = "9BfKV0AUdVsctHoG8R-8";
    private final String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiZWRjZjNjMTktMTE4Zi00ZTA4LTk3ZDEtODg5OTBiY2MxMzRjIiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogImFha3oySUIySC12ZVlXVHY4czRxX0ZlREU5Mk5Edi12eTk1Z1N4MDVwbTQiLAoJCSJ4IjogInRNSGVQU2NCVVYzTUlacXlUUkN0c01tdUd0M2E3NWlJVVdfLXJ5LXN1dE0iLAoJCSJ5IjogIjBaSVd0MWMzdVJWdTRRSnJZMzhrRHlaMTNVYmdkSUc0cENkalQ1cGhPOEkiLAoJCSJkIjogIjVsN1ZEa3ZxUm5taXFEb3gwbWc3Z3FVUTl4Z296OXY2clNiLWJ1NDFsVGsiLAoJCSJpYXQiOiAxNjc3Mjk3NzczCgl9Cn0=";
    private String repoId;
    private int entryId;
    public Entry entry;

    public RemteEntry(String repoId, int entryId) {
        location = "remte";
        this.repoId = repoId;
        setEntryId(entryId);
        try {
            AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);
            RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                    servicePrincipalKey, accessKey);
            entry = client.getEntriesClient()
                    .getEntry(this.repoId, this.entryId, null).join();
            this.name = entry.getName();
            if (entry.getEntryType().name().equals("DOCUMENT")) {

                Consumer<InputStream> consumer = inputStream -> {
                    File exportedFile = new File(name);
                    try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int length = inputStream.read(buffer);
                            if (length == -1) {
                                break;
                            }
                            outputStream.write(buffer, 0, length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    this.file = exportedFile;
                };

                client.getEntriesClient()
                        .exportDocument(this.repoId, entryId, null, consumer)
                        .join();
                this.type = "file";

            } else if (entry.getEntryType().name().equals("FOLDER")) {
                this.type = "directory";
                this.file = new File("C:\\Users\\zmcmu\\OneDrive\\Documents\\NetBeansProjects\\FileProcessing\\" + name);
                file.mkdir();
                downloadFolder(client,this.entryId,this.file);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.name = file.getName();
    }

    final public void setEntryId(int entryId) {
        if (entryId == 0) {
            this.entryId = 1;
        } else {
            this.entryId = abs(entryId);
        }
    }

    final void downloadFolder(RepositoryApiClient client, int folderId, File folder) {
        try {
            ODataValueContextOfIListOfEntry result = client
                    .getEntriesClient()
                    .getEntryListing(repoId, entryId, true, null, null, null, null, null, "name", null, null, null).join();

            List<Entry> entries = result.getValue();

            for (Entry childEntry : entries) {
                if (childEntry.getEntryType().name().equals("FOLDER")) {
                    File subFolder = new File(folder.getAbsolutePath() + "\\" + childEntry.getName());
                    subFolder.mkdirs();

                    downloadFolder(client, childEntry.getId(), subFolder);
                } else {
                    Consumer<InputStream> consumer = inputStream -> {
                        File exportedFile = new File(folder.getAbsolutePath() + "\\" + childEntry.getName());
                        try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int length = inputStream.read(buffer);
                                if (length == -1) {
                                    break;
                                }
                                outputStream.write(buffer, 0, length);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        this.file = exportedFile;
                    };

                    client.getEntriesClient()
                            .exportDocument(this.repoId, childEntry.getId(), null, consumer)
                            .join();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
