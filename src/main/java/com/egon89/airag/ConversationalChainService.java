package com.egon89.airag;

import org.springframework.stereotype.Service;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.content.retriever.ContentRetriever;

@Service
public class ConversationalChainService {
    private static final int MAX_MESSAGES = 10;
    private final ContentRetriever contentRetriever;
    private final ChatLanguageModel chatLanguageModel;

    public ConversationalChainService(
            ContentRetriever contentRetriever,
            ChatLanguageModel chatLanguageModel
    ) {
        this.contentRetriever = contentRetriever;
        this.chatLanguageModel = chatLanguageModel;
    }

    public String execute(String question) {
        final var chain = ConversationalRetrievalChain.builder()
            .contentRetriever(contentRetriever)
            .chatLanguageModel(chatLanguageModel)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(MAX_MESSAGES))
            .promptTemplate(PromptTemplate.from("You are a helpful assistant. Answer the question based on the context provided.\n\nContext: {context}\n\nQuestion: {question}"))
            .build();

        return chain.execute(question);
    }
}
