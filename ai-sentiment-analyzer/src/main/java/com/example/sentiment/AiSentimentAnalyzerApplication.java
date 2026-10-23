package com.example.sentiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiSentimentAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSentimentAnalyzerApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" AI Sentiment Analyzer is running successfully! ");
        System.out.println(" Application URL : http://localhost:8085        ");
        System.out.println(" H2 Database URL : http://localhost:8085/h2-console ");
        System.out.println("=================================================");
    }
}
