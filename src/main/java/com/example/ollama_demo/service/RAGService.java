package com.example.ollama_demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.example.ollama_demo.entity.Weather;
import com.example.ollama_demo.repository.WeatherRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class RAGService {
	
	private final WeatherRepository weatherRepository;
	private final VectorStore vectorStore;
	private final ChatClient chatClient;
	private static final String DEFAULT_USER_TEXT_ADVISE = """
        以下是背景資訊：
        ---------------------
        {question_answer_context}
        ---------------------
        根據提供的背景資訊和歷史資料，而不是先前的知識，回覆使用者的評論。
        如果答案不在背景資訊中，請告知使用者你無法回答該問題。
            """; 
	
	public void loadData() {
		log.info("import Weather to vectorStore");
		vectorStore.write(getAllWeather());
	}
	
	private List<Document> getAllWeather() {
		List<Weather> dataList = weatherRepository.findAll();
		Map<String, Object> metadata = new HashMap<>();
		metadata.put("type", "weather");
		return dataList.stream().map(w -> 
			new Document(w.getId().toString(), w.toString(), metadata)
		).toList();
	}
	
	/**
	 * 向量搜尋 -> 送入 LLM -> 流式回答
	 * @param query
	 * @return Flux 逐步輸出內容，不需要等到完整回應才返回
	 */
	public Flux<ServerSentEvent<String>> search(String query) {
	    return chatClient.prompt()
                .user(query)
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build(), DEFAULT_USER_TEXT_ADVISE))
                .stream()
                .content()
                .map(response -> ServerSentEvent.builder(response)
                        .event("message")
                        .build());
	}
   
	/**
	 * embedding.model(nomic-embed-text) 將query向量化後，直接搜尋向量資料庫中的相似資料
	 * @param query
	 * @return
	 */
    public List<Document> searchStore(String query) {
        return vectorStore.similaritySearch(query);
    }
}
