package com.egon89.airag;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final MessageCache messageCache;
    private final LangChain4jAssistant langChain4jAssistant;

    public AssistantController(MessageCache messageCache, LangChain4jAssistant langChain4jAssistant) {
        this.messageCache = messageCache;
        this.langChain4jAssistant = langChain4jAssistant;
    }

    @PostMapping
    public ResponseEntity<AssistantResponse> submitMessage(@RequestBody AssistantRequest request) {
        messageCache.put(request.id, request.message);

        return ResponseEntity.ok(new AssistantResponse(request.id));
    }

    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamResponse(@PathVariable String id) {
        final var message = messageCache.get(id);
        if (Objects.isNull(message)) {
            return Flux.error(new RuntimeException("No message found for ID: " + id));
        }

        AtomicReference<String> lastChunk = new AtomicReference<>("");
        Flux<String> rawStream = langChain4jAssistant.chat(id, message);
        Flux<String> smartStream = rawStream.map(chunk -> {
            String previous = lastChunk.getAndSet(chunk);

            // If previous is empty, it's the first chunk
            if (previous.isEmpty()) {
                return chunk;
            }

            // If last character of previous is not space and current chunk doesn't start with punctuation
            boolean needsSpace = !previous.endsWith(" ") && !chunk.matches("^[.,!?)]");

            return (needsSpace ? " " : "") + chunk;
        });

        return smartStream;
    }
    
    @PostMapping
    @RequestMapping("/assistant")
    public Flux<String> chat(@RequestBody AssistantRequest request) {
        
        return langChain4jAssistant.chat(request.id, request.message);
    }

    record AssistantRequest(String id, String message) {}

    record AssistantResponse(String id) {}
}
