package com.example.sentiment;

import com.example.sentiment.service.SentimentClassifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SentimentClassifier algorithm.
 * Can be executed via IDE (IntelliJ JUnit runner) or standalone via main().
 */
public class SentimentClassifierTest {

    private SentimentClassifier classifier;

    @BeforeEach
    public void setUp() {
        classifier = new SentimentClassifier();
    }

    @Test
    public void testPositiveSentiment() {
        String input = "This product is amazing, excellent, and great!";
        SentimentClassifier.Result result = classifier.classify(input);

        assertEquals("POSITIVE", result.getSentiment());
        assertTrue(result.getConfidence() >= 65.0, "Confidence should be >= 65%");
        assertTrue(result.getExplanation().contains("POSITIVE"));
    }

    @Test
    public void testNegativeSentiment() {
        String input = "Terrible experience, horrible quality, and worst support.";
        SentimentClassifier.Result result = classifier.classify(input);

        assertEquals("NEGATIVE", result.getSentiment());
        assertTrue(result.getConfidence() >= 65.0, "Confidence should be >= 65%");
        assertTrue(result.getExplanation().contains("NEGATIVE"));
    }

    @Test
    public void testNeutralSentiment() {
        String input = "The conference is scheduled for Thursday at the auditorium.";
        SentimentClassifier.Result result = classifier.classify(input);

        assertEquals("NEUTRAL", result.getSentiment());
        assertTrue(result.getExplanation().contains("NEUTRAL"));
    }

    @Test
    public void testNegationHandling() {
        String input = "The design was not good and not useful.";
        SentimentClassifier.Result result = classifier.classify(input);

        assertEquals("NEGATIVE", result.getSentiment(), "Negation should invert positive words to negative");
        assertTrue(result.getExplanation().contains("negation"));
    }

    /**
     * Standalone main method for executing tests without requiring surefire plugin resolution.
     */
    public static void main(String[] args) {
        System.out.println("Running SentimentClassifier tests...");
        SentimentClassifierTest test = new SentimentClassifierTest();

        test.setUp();
        test.testPositiveSentiment();
        System.out.println("  [PASS] testPositiveSentiment");

        test.setUp();
        test.testNegativeSentiment();
        System.out.println("  [PASS] testNegativeSentiment");

        test.setUp();
        test.testNeutralSentiment();
        System.out.println("  [PASS] testNeutralSentiment");

        test.setUp();
        test.testNegationHandling();
        System.out.println("  [PASS] testNegationHandling");

        System.out.println("All 4 SentimentClassifier unit tests PASSED successfully!");
    }
}
