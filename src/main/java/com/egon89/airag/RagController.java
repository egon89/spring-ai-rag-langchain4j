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
  private final ConversationalChainService conversationalChainService;

    public RagController(ContentRetriever contentRetriever, ConversationalChainService conversationalChainService) {
        this.contentRetriever = contentRetriever;
        this.conversationalChainService = conversationalChainService;
    }

  @PostMapping("/ask")
  public ResponseEntity<String> ask(@RequestBody AskRequest input) {
    return ResponseEntity.ok(conversationalChainService.execute(input.question));
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
