package com.example.ollama_demo.controller;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ollama_demo.service.ChatService;
import com.example.ollama_demo.service.RAGService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class OllamaController {

	private final RAGService ragService;
	private final ChatService chatService;
	
	@GetMapping("/chat")
	public String chat(@RequestParam String query) {
		return chatService.joke(query);
	}
	
	@GetMapping("/loadData")
	public void importWeather() {
		ragService.loadData();
	}
	
	@GetMapping("/search")
	public Flux<ServerSentEvent<String>> search(@RequestParam String query) {
		return ragService.search(query);
	}

}
