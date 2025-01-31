package com.example.ollama_demo.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ollama_demo.service.ChatService;
import com.example.ollama_demo.service.RAGService;

import lombok.RequiredArgsConstructor;

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
	public List<Document> search(@RequestParam String query) {
		return ragService.search(query);
	}

}
