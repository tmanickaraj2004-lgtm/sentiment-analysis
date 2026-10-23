// AI Sentiment Analyzer - Frontend JavaScript Controller

const API_BASE = '/api';

// DOM elements
const reviewText = document.getElementById('reviewText');
const charCount = document.getElementById('charCount');
const analyzeBtn = document.getElementById('analyzeBtn');
const btnText = document.getElementById('btnText');
const btnSpinner = document.getElementById('btnSpinner');

const resultContainer = document.getElementById('resultContainer');
const resultSentiment = document.getElementById('resultSentiment');
const confidenceValue = document.getElementById('confidenceValue');
const confidenceBar = document.getElementById('confidenceBar');
const explanationText = document.getElementById('explanationText');

const historyList = document.getElementById('historyList');
const totalCount = document.getElementById('totalCount');

// Live character counter
reviewText.addEventListener('input', () => {
    const count = reviewText.value.length;
    charCount.textContent = `${count} character${count === 1 ? '' : 's'}`;
});

// Set sample text helper
function setSample(text) {
    reviewText.value = text;
    charCount.textContent = `${text.length} characters`;
    reviewText.focus();
}

// Clear textarea helper
function clearInput() {
    reviewText.value = '';
    charCount.textContent = '0 characters';
    reviewText.focus();
}

// Analyze sentiment function
async function analyzeSentiment() {
    const text = reviewText.value.trim();
    if (!text) {
        alert('Please enter some text, review, or comment to analyze.');
        reviewText.focus();
        return;
    }

    setLoading(true);

    try {
        const response = await fetch(`${API_BASE}/analyze`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text: text })
        });

        if (!response.ok) {
            throw new Error(`Server returned error: ${response.status}`);
        }

        const data = await response.json();
        displayResult(data);
        await loadHistory();
    } catch (error) {
        console.error('Analysis failed:', error);
        alert('Failed to analyze sentiment. Please ensure the Spring Boot server is running.');
    } finally {
        setLoading(false);
    }
}

// Display result card with proper badges and animations
function displayResult(data) {
    resultContainer.classList.remove('hidden');

    const sentiment = (data.sentiment || 'NEUTRAL').toUpperCase();
    resultSentiment.textContent = sentiment;

    // Reset badge classes
    resultSentiment.className = 'sentiment-badge';
    if (sentiment === 'POSITIVE') {
        resultSentiment.classList.add('badge-positive');
        confidenceBar.style.background = 'linear-gradient(90deg, #22c55e, #16a34a)';
    } else if (sentiment === 'NEGATIVE') {
        resultSentiment.classList.add('badge-negative');
        confidenceBar.style.background = 'linear-gradient(90deg, #ef4444, #dc2626)';
    } else {
        resultSentiment.classList.add('badge-neutral');
        confidenceBar.style.background = 'linear-gradient(90deg, #94a3b8, #64748b)';
    }

    const confidence = (typeof data.confidence === 'number') ? data.confidence.toFixed(1) : '50.0';
    confidenceValue.textContent = `${confidence}%`;
    confidenceBar.style.width = `${confidence}%`;

    explanationText.textContent = data.explanation || 'No explanation provided.';

    // Smooth scroll result into view if needed on small screens
    resultContainer.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// Fetch and render history
async function loadHistory() {
    try {
        const response = await fetch(`${API_BASE}/history`);
        if (!response.ok) {
            throw new Error(`Failed to fetch history: ${response.status}`);
        }

        const items = await response.json();
        renderHistory(items);
    } catch (error) {
        console.error('Error fetching history:', error);
    }
}

// Render history list cards
function renderHistory(items) {
    totalCount.textContent = items ? items.length : 0;

    if (!items || items.length === 0) {
        historyList.innerHTML = `
            <div class="empty-state">
                <p>No analyses performed yet. Enter a review above and click <strong>Analyze Sentiment</strong>!</p>
            </div>
        `;
        return;
    }

    historyList.innerHTML = items.map(item => {
        const sentimentClass = (item.sentiment === 'POSITIVE')
            ? 'badge-positive'
            : (item.sentiment === 'NEGATIVE' ? 'badge-negative' : 'badge-neutral');

        const formattedDate = formatDateTime(item.createdAt);

        return `
            <div class="history-item">
                <div class="history-item-top">
                    <span class="history-badge ${sentimentClass}">${escapeHtml(item.sentiment)}</span>
                    <span class="history-time">${formattedDate}</span>
                </div>
                <div class="history-text">"${escapeHtml(item.text)}"</div>
                <div class="history-item-footer">
                    <span>Confidence: <strong>${item.confidence ? item.confidence.toFixed(1) : '50.0'}%</strong></span>
                    <span>ID: #${item.id}</span>
                </div>
            </div>
        `;
    }).join('');
}

// Clear history action
async function clearHistory() {
    const total = parseInt(totalCount.textContent, 10) || 0;
    if (total === 0) {
        alert('History is already empty.');
        return;
    }

    if (!confirm('Are you sure you want to clear all analysis history records?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/history`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`Failed to clear history: ${response.status}`);
        }

        await loadHistory();
        alert('Analysis history cleared successfully!');
    } catch (error) {
        console.error('Error clearing history:', error);
        alert('Failed to clear history. Please check the backend connection.');
    }
}

// Button loading state
function setLoading(isLoading) {
    if (isLoading) {
        analyzeBtn.disabled = true;
        btnText.textContent = 'Analyzing...';
        btnSpinner.classList.remove('hidden');
    } else {
        analyzeBtn.disabled = false;
        btnText.textContent = 'Analyze Sentiment';
        btnSpinner.classList.add('hidden');
    }
}

// Helpers
function formatDateTime(isoString) {
    if (!isoString) return '';
    try {
        const date = new Date(isoString);
        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }) + ' ' +
               date.toLocaleDateString([], { month: 'short', day: 'numeric' });
    } catch (e) {
        return isoString;
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

// Load initial history when page loads
document.addEventListener('DOMContentLoaded', () => {
    loadHistory();
});
