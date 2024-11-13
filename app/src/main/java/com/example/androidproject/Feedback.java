package com.example.androidproject;

public class Feedback {
    private String feedbackText;
    private long timestamp;
    private float rating; // Add a rating field

    public Feedback(String feedbackText, long timestamp, float rating) {
        this.feedbackText = feedbackText;
        this.timestamp = timestamp;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
