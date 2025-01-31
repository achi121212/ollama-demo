package com.example.ollama_demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final ChatClient chatClient;
	
	public String joke(String query) {
		return chatClient
				.prompt()
				.user(query)
				.call()
				.content();
	}

}
