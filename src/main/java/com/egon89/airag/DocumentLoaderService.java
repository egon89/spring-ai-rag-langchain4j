package com.egon89.airag;

import jakarta.annotation.PostConstruct;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;

import java.io.File;
import java.util.Objects;

@Service
public class DocumentLoaderService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final Tokenizer tokenizer;
    private final Tika tika;

    public DocumentLoaderService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            Tokenizer tokenizer,
            Tika tika
    ) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.tokenizer = tokenizer;
        this.tika = tika;
    }

    @PostConstruct
    public void loadDocuments() throws Exception {
        System.out.println("Loading documents...");
        File folder = new File("documents");
        var ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(50, 0, tokenizer))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            String text = tika.parseToString(file);
            System.out.println("### File: " + file.getName());
            System.out.println(text);
            
            ingestor.ingest(new Document(text));
        }
    }
}
