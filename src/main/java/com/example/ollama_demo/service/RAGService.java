package com.example.ollama_demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.example.ollama_demo.entity.Weather;
import com.example.ollama_demo.repository.WeatherRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RAGService {
	
	private final WeatherRepository weatherRepository;
	private final VectorStore vectorStore;
	
	public void loadData() {
		log.info("import Weather to vectorStore");
		vectorStore.write(getAllWeather());
	}
	
	private List<Document> getAllWeather() {
		List<Weather> dataList = weatherRepository.findAll();
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("type", "weather");
		List<Document> weathers = dataList.stream().map(w -> 
			new Document(w.getId().toString(), w.toString(), metadata)
		).toList();
		return weathers;
	}
	
	public List<Document> search(String query) {
		return vectorStore.similaritySearch(query);
	}
}
