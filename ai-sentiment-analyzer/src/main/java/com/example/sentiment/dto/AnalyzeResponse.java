package com.example.sentiment.dto;

import java.time.LocalDateTime;

public class AnalyzeResponse {

    private Long id;
    private String originalText;
    private String sentiment;
    private Double confidence;
    private String explanation;
    private LocalDateTime createdAt;

    public AnalyzeResponse() {
    }

    public AnalyzeResponse(Long id, String originalText, String sentiment, Double confidence, String explanation, LocalDateTime createdAt) {
        this.id = id;
        this.originalText = originalText;
        this.sentiment = sentiment;
        this.confidence = confidence;
        this.explanation = explanation;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
