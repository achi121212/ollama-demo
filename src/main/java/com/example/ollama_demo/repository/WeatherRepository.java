package com.example.ollama_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ollama_demo.entity.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

}
