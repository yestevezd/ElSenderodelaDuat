package com.yestevezd.elsenderodeladuat.core.narrative;

public class NarrativeOption {
    private String text;
    private String nextId;
    private Integer corazonDelta;

    // Constructor vacío 
    public NarrativeOption() {}

    public NarrativeOption(String text, String nextId) {
        this.text = text;
        this.nextId = nextId;
    }

    public NarrativeOption(String text, String nextId, Integer corazonDelta) {
        this.text         = text;
        this.nextId       = nextId;
        this.corazonDelta = corazonDelta;
    }

    public String getText() {
        return text;
    }

    public String getNextId() {
        return nextId;
    }

    public Integer getCorazonDelta() {
        return corazonDelta;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public void setCorazonDelta(Integer corazonDelta) {
        this.corazonDelta = corazonDelta;
    }
}