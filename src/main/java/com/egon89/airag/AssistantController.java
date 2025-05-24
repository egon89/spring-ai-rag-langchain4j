package com.egon89.airag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class AssistantController {

    private final LangChain4jAssistant langChain4jAssistant;

    public AssistantController(LangChain4jAssistant langChain4jAssistant) {
        this.langChain4jAssistant = langChain4jAssistant;
    }
    
    @PostMapping
    @RequestMapping("/assistant")
    public Flux<String> chat(@RequestBody AssistantRequest request) {
        
        return langChain4jAssistant.chat(request.id, request.message);
    }

    record AssistantRequest(String id, String message) {}
}
