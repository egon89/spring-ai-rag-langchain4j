package com.egon89.airag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;

@RestController
public class RagController {

  private final ContentRetriever contentRetriever;

    public RagController(ContentRetriever contentRetriever) {
        this.contentRetriever = contentRetriever;
    }

  @PostMapping("/ask")
  public String ask(@RequestBody AskRequest question) {
    return "post Hello, " + question;
  }

  @PostMapping("/v2/ask")
    public ResponseEntity<List<String>> retrieve(@RequestBody AskRequest input) {
        
      final var results = contentRetriever.retrieve(new Query(input.question)).stream()
          .map(content -> content.textSegment().text())
          .toList();

      return ResponseEntity.ok(results);
    }

  public record AskRequest(String question) {
  }
}
