package com.koa.koalamailman.domain.chatbot.config;

import com.koa.koalamailman.domain.chatbot.template.PromptTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, MessageChatMemoryAdvisor messageChatMemoryAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.2)
                        .maxTokens(1024)
                        .build())
                .defaultSystem(PromptTemplates.DEFAULT_SYSTEM)
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
    }

    @Bean
    public MessageWindowChatMemory chatMemory(InMemoryChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(20)
                .build();
    }

    @Bean
    public InMemoryChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(MessageWindowChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }
}
