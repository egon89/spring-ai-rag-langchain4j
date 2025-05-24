package com.egon89.airag;

import jakarta.annotation.PostConstruct;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class DocumentLoaderService {

    private final Tika tika;

    public DocumentLoaderService(Tika tika) {
        this.tika = tika;
    }

    @PostConstruct
    public void loadDocuments() throws Exception {
        System.out.println("Loading documents...");
        File folder = new File("documents");

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            String text = tika.parseToString(file);
            System.out.println("### File: " + file.getName());
            System.out.println(text);
        }
    }
}
