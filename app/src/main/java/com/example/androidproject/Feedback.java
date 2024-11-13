package com.example.androidproject;

public class Feedback {
    private String feedbackText;
    private long timestamp;

    public Feedback(String feedbackText, long timestamp) {
        this.feedbackText = feedbackText;
        this.timestamp = timestamp;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
