package com.example.sentiment.service;

import com.example.sentiment.dto.AnalyzeRequest;
import com.example.sentiment.dto.AnalyzeResponse;
import com.example.sentiment.entity.Analysis;
import com.example.sentiment.repository.AnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class handling sentiment analysis workflow, database persistence, and history retrieval.
 */
@Service
public class SentimentService {

    private final SentimentClassifier sentimentClassifier;
    private final AnalysisRepository analysisRepository;

    public SentimentService(SentimentClassifier sentimentClassifier, AnalysisRepository analysisRepository) {
        this.sentimentClassifier = sentimentClassifier;
        this.analysisRepository = analysisRepository;
    }

    /**
     * Analyzes input text, saves the result to H2 database, and returns the analysis response.
     */
    @Transactional
    public AnalyzeResponse analyzeText(AnalyzeRequest request) {
        String input = (request != null && request.getText() != null) ? request.getText().trim() : "";

        // Perform sentiment classification
        SentimentClassifier.Result result = sentimentClassifier.classify(input);

        // Save result in H2 database
        Analysis analysis = new Analysis(
                input,
                result.getSentiment(),
                result.getConfidence(),
                result.getExplanation()
        );
        Analysis saved = analysisRepository.save(analysis);

        // Convert to response DTO
        return new AnalyzeResponse(
                saved.getId(),
                saved.getText(),
                saved.getSentiment(),
                saved.getConfidence(),
                saved.getExplanation(),
                saved.getCreatedAt()
        );
    }

    /**
     * Retrieves all past analysis records, newest first.
     */
    @Transactional(readOnly = true)
    public List<Analysis> getHistory() {
        return analysisRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Deletes all records from the database.
     */
    @Transactional
    public void clearHistory() {
        analysisRepository.deleteAll();
    }
}
