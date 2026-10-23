package com.example.sentiment.controller;

import com.example.sentiment.dto.AnalyzeRequest;
import com.example.sentiment.dto.AnalyzeResponse;
import com.example.sentiment.entity.Analysis;
import com.example.sentiment.service.SentimentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for sentiment analysis and history management.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    /**
     * POST /api/analyze
     * Analyzes user text and returns the classified sentiment, confidence score, and AI explanation.
     */
    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest request) {
        AnalyzeResponse response = sentimentService.analyzeText(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/history
     * Returns all previous sentiment analyses saved in the database.
     */
    @GetMapping("/history")
    public ResponseEntity<List<Analysis>> getHistory() {
        List<Analysis> history = sentimentService.getHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * DELETE /api/history
     * Deletes all analysis history from the database.
     */
    @DeleteMapping("/history")
    public ResponseEntity<Map<String, String>> clearHistory() {
        sentimentService.clearHistory();

        Map<String, String> response = new HashMap<>();
        response.put("message", "History cleared successfully");
        return ResponseEntity.ok(response);
    }
}
