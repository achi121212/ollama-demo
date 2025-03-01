package com.example.ollama_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

	@Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
	
}
