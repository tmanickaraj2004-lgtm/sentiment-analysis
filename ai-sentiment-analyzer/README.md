# AI Sentiment Analyzer

A beginner-friendly, clean, and interactive **AI Sentiment Analysis** web application built with **Java 17**, **Spring Boot 3 / 4**, **Spring Data JPA**, **H2 Database**, and a responsive modern **HTML/CSS/JavaScript** frontend.

---

## 🌟 Key Features

1. **Intelligent Sentiment Analysis Algorithm**:
   - Classifies text into **POSITIVE**, **NEGATIVE**, or **NEUTRAL**.
   - Calculates a **Confidence Percentage** (50.0% – 98.0%).
   - Generates natural-language **AI/LLM-style Explanations** explaining why the sentiment was chosen.
   - Handles **contextual negation** (e.g., *"not good"*, *"not useful"* correctly mapped to negative).

2. **Persistent H2 Database Storage**:
   - Saves every analysis record (`id`, `text`, `sentiment`, `confidence`, `explanation`, `createdAt`).
   - Integrated with **Spring Data JPA**.
   - Interactive **H2 Web Console** enabled.

3. **Modern, Responsive Frontend**:
   - Clean card-based design with micro-interactions.
   - Quick one-click sample buttons (Positive, Negative, Neutral, Negation test cases).
   - Real-time character counter.
   - Dynamic sentiment badges and confidence progress bar.
   - History section with instant "Clear History" capability.

4. **Zero External API Dependencies**:
   - No OpenAI/Google API keys required.
   - Runs 100% locally and offline.

---

## 📂 Project Structure

```
ai-sentiment-analyzer/
 ├── pom.xml                               # Maven build configuration
 ├── README.md                             # Documentation & presentation guide
 ├── src/
 │   ├── main/
 │   │   ├── java/com/example/sentiment/
 │   │   │   ├── AiSentimentAnalyzerApplication.java  # Main application entry point
 │   │   │   ├── controller/
 │   │   │   │   └── SentimentController.java        # REST API endpoints (/api/analyze, /api/history)
 │   │   │   ├── service/
 │   │   │   │   ├── SentimentService.java           # Business logic & DB transaction management
 │   │   │   │   └── SentimentClassifier.java        # ML/NLP-inspired rule-based sentiment classifier
 │   │   │   ├── entity/
 │   │   │   │   └── Analysis.java                   # JPA entity representing saved analyses
 │   │   │   ├── repository/
 │   │   │   │   └── AnalysisRepository.java         # Spring Data JPA repository
 │   │   │   └── dto/
 │   │   │       ├── AnalyzeRequest.java             # Request payload DTO
 │   │   │       └── AnalyzeResponse.java            # Response payload DTO
 │   │   └── resources/
 │   │       ├── application.properties             # Port, JPA, and H2 console settings
 │   │       └── static/
 │   │           ├── index.html                     # Web UI homepage
 │   │           ├── css/style.css                  # Modern styling & animations
 │   │           └── js/app.js                      # REST API client & UI controller
 └── src/test/java/com/example/sentiment/
     └── SentimentClassifierTest.java               # Comprehensive unit tests
```

---

## 🚀 How to Run in IntelliJ IDEA

1. Open **IntelliJ IDEA**.
2. Click **File -> Open...** and navigate to:
   ```
   C:\Users\tmani\.gemini\antigravity\scratch\ai-sentiment-analyzer
   ```
3. IntelliJ will automatically detect it as a **Maven project** and load the project structure.
4. Locate `AiSentimentAnalyzerApplication.java` inside `src/main/java/com/example/sentiment/`.
5. Click the green **Run ▶** button beside `public static void main`.
6. Open your web browser and go to:
   - **Web Application**: [http://localhost:8085](http://localhost:8085)
   - **H2 Database Console**: [http://localhost:8085/h2-console](http://localhost:8085/h2-console)
     - *JDBC URL*: `jdbc:h2:mem:sentimentdb`
     - *User Name*: `sa`
     - *Password*: *(leave blank)*

---

## 🛠️ How to Run from the Terminal / Command Line

```powershell
cd C:\Users\tmani\.gemini\antigravity\scratch\ai-sentiment-analyzer

# Build and package:
mvn clean package -DskipTests

# Run the application:
java -jar target/ai-sentiment-analyzer-1.0.0.jar
```

---

## 📡 REST API Endpoints

| Method | Endpoint | Description | Sample Request / Response |
|---|---|---|---|
| `POST` | `/api/analyze` | Analyzes text & stores record | **Body**: `{"text": "Great product, loved it!"}`<br>**Response**: `{"id": 1, "originalText": "...", "sentiment": "POSITIVE", "confidence": 89.0, "explanation": "..."}` |
| `GET` | `/api/history` | Fetches all past analyses | **Response**: List of analyses ordered by newest first |
| `DELETE` | `/api/history` | Deletes all analysis history | **Response**: `{"message": "History cleared successfully"}` |

---

## 🎓 College Presentation Talking Points

1. **Architecture (Layered MVC pattern)**:
   - **Controller Layer** (`SentimentController`): Handles incoming HTTP requests and delegates to the service layer.
   - **Service Layer** (`SentimentService`): Implements business workflow and interacts with both the classifier and the database.
   - **Classifier Module** (`SentimentClassifier`): Analyzes text polarity using keyword weighting and contextual negation reversal (e.g., transforming *"not good"* into a negative signal).
   - **Data Access Layer** (`AnalysisRepository`): Communicates with the in-memory H2 database via Spring Data JPA without boilerplate SQL.
   - **Presentation Layer** (`HTML5`, `CSS3`, `JavaScript`): Connects asynchronously (`fetch` API) to backend REST endpoints.

2. **Why Rule-Based / Lexicon ML?**:
   - Fast, predictable, and requires zero external API keys or expensive GPU hardware.
   - Provides clear explainability (*"Why was this classified this way?"*), which is a key topic in modern Explainable AI (XAI).
