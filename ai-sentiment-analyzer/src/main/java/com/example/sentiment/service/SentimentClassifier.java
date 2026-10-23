package com.example.sentiment.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Natural Language Processing / Machine Learning inspired Rule-Based Classifier.
 * Analyzes text sentiment using lexicon tokenization, negation handling,
 * confidence estimation, and dynamic AI-style reasoning.
 */
@Component
public class SentimentClassifier {

    // Positive keyword vocabulary (including common inflections)
    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
            "good", "excellent", "excellently", "amazing", "amazingly", "great", "greatly",
            "love", "loved", "loving", "happy", "happily", "useful", "usefully",
            "wonderful", "wonderfully", "awesome", "awesomely", "best", "fantastic",
            "fantastically", "super", "brilliant", "brilliantly", "perfect", "perfectly",
            "liked", "like", "enjoy", "enjoyed", "pleasant", "impressive", "recommend", "recommended"
    ));

    // Negative keyword vocabulary (including common inflections)
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
            "bad", "badly", "terrible", "terribly", "horrible", "horribly", "poor", "poorly",
            "hate", "hated", "worst", "boring", "disappointing", "disappointed",
            "awful", "awfully", "useless", "slow", "slowly", "broken", "ugly",
            "annoying", "annoyed", "disliked", "dislike", "sad", "sadly", "rubbish",
            "fail", "failed", "defective"
    ));

    // Negation words that invert sentiment polarity
    private static final Set<String> NEGATION_WORDS = new HashSet<>(Arrays.asList(
            "not", "never", "no", "hardly", "barely", "scarcely",
            "isn't", "isnt", "aren't", "arent", "wasn't", "wasnt",
            "don't", "dont", "didn't", "didnt", "won't", "wont",
            "can't", "cant", "couldn't", "couldnt"
    ));

    public static class Result {
        private final String sentiment;
        private final double confidence;
        private final String explanation;

        public Result(String sentiment, double confidence, String explanation) {
            this.sentiment = sentiment;
            this.confidence = confidence;
            this.explanation = explanation;
        }

        public String getSentiment() {
            return sentiment;
        }

        public double getConfidence() {
            return confidence;
        }

        public String getExplanation() {
            return explanation;
        }
    }

    /**
     * Classifies the input text into POSITIVE, NEGATIVE, or NEUTRAL.
     * Computes a confidence percentage and crafts an AI/LLM-style explanation.
     */
    public Result classify(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new Result("NEUTRAL", 50.0, "Empty input provided. Defaulted to neutral sentiment.");
        }

        // Clean and tokenize into lowercase words
        String cleaned = text.toLowerCase().replaceAll("[^a-z0-9'\\s]", " ");
        String[] tokens = cleaned.trim().split("\\s+");

        List<String> detectedPositive = new ArrayList<>();
        List<String> detectedNegative = new ArrayList<>();
        List<String> invertedPhrases = new ArrayList<>();

        int score = 0;

        for (int i = 0; i < tokens.length; i++) {
            String word = tokens[i];
            boolean isNegated = (i > 0 && NEGATION_WORDS.contains(tokens[i - 1]));

            if (POSITIVE_WORDS.contains(word)) {
                if (isNegated) {
                    // "not good" behaves as a negative signal
                    score -= 1;
                    invertedPhrases.add(tokens[i - 1] + " " + word);
                    detectedNegative.add(tokens[i - 1] + " " + word);
                } else {
                    score += 1;
                    detectedPositive.add(word);
                }
            } else if (NEGATIVE_WORDS.contains(word)) {
                if (isNegated) {
                    // "not bad" behaves as a mildly positive/neutralized signal
                    score += 1;
                    invertedPhrases.add(tokens[i - 1] + " " + word);
                    detectedPositive.add(tokens[i - 1] + " " + word);
                } else {
                    score -= 1;
                    detectedNegative.add(word);
                }
            }
        }

        String sentiment;
        double confidence;
        StringBuilder explanation = new StringBuilder();

        if (score > 0) {
            sentiment = "POSITIVE";
            confidence = Math.min(98.0, 65.0 + (score * 8.0));
            explanation.append("The text exhibits an overall POSITIVE tone (Score: +").append(score).append("). ");
            explanation.append("Key positive sentiment indicators identified: ")
                    .append(formatKeywords(detectedPositive)).append(". ");
            if (!detectedNegative.isEmpty()) {
                explanation.append("Minor negative expressions (")
                        .append(formatKeywords(detectedNegative))
                        .append(") were detected but outweighed by positive indicators. ");
            }
            if (!invertedPhrases.isEmpty()) {
                explanation.append("Contextual negation was applied to: ")
                        .append(formatKeywords(invertedPhrases)).append(". ");
            }
        } else if (score < 0) {
            sentiment = "NEGATIVE";
            confidence = Math.min(98.0, 65.0 + (Math.abs(score) * 8.0));
            explanation.append("The text conveys an overall NEGATIVE tone (Score: ").append(score).append("). ");
            explanation.append("Strong negative sentiment indicators identified: ")
                    .append(formatKeywords(detectedNegative)).append(". ");
            if (!detectedPositive.isEmpty()) {
                explanation.append("Positive expressions (")
                        .append(formatKeywords(detectedPositive))
                        .append(") were detected but insufficient to overturn the negative context. ");
            }
            if (!invertedPhrases.isEmpty()) {
                explanation.append("Contextual negation altered the sentiment for: ")
                        .append(formatKeywords(invertedPhrases)).append(". ");
            }
        } else {
            sentiment = "NEUTRAL";
            if (!detectedPositive.isEmpty() && !detectedNegative.isEmpty()) {
                confidence = 58.0;
                explanation.append("The text displays a balanced or mixed sentiment (Score: 0). ");
                explanation.append("Positive indicators (").append(formatKeywords(detectedPositive))
                        .append(") and negative indicators (").append(formatKeywords(detectedNegative))
                        .append(") cancel each other out.");
            } else {
                confidence = 72.0;
                explanation.append("The text is classified as NEUTRAL. ");
                explanation.append("It contains factual, descriptive, or objective language without explicit positive or negative polarity keywords.");
            }
        }

        // Round confidence to 1 decimal place
        double roundedConfidence = Math.round(confidence * 10.0) / 10.0;
        return new Result(sentiment, roundedConfidence, explanation.toString().trim());
    }

    private String formatKeywords(List<String> words) {
        if (words == null || words.isEmpty()) {
            return "none";
        }
        // Deduplicate words for display
        List<String> distinct = words.stream().distinct().limit(4).toList();
        return "'" + String.join("', '", distinct) + "'";
    }
}
