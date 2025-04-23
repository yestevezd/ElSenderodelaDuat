package com.yestevezd.elsenderodeladuat.core.narrative;

public class NarrativeOption {
    private String text;
    private String nextId;

    // Constructor vacío 
    public NarrativeOption() {}

    public NarrativeOption(String text, String nextId) {
        this.text = text;
        this.nextId = nextId;
    }

    public String getText() {
        return text;
    }

    public String getNextId() {
        return nextId;
    }
}