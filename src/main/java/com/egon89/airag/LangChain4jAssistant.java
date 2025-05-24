package com.egon89.airag;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface LangChain4jAssistant {
    
    @SystemMessage("You are a helpful assistant. Answer the question based on the context provided.")
    Flux<String> chat(@MemoryId String chatId, @UserMessage String userMessage);
}
