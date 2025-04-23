package com.yestevezd.elsenderodeladuat.core.narrative.dialogues;

import java.util.HashMap;
import java.util.Map;

import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeOption;

public class DialogueTree {
    private Map<String, NarrativeLine> lines = new HashMap<>();
    private NarrativeLine currentLine;

    public void addLine(NarrativeLine line) {
        lines.put(line.getId(), line);
        if (currentLine == null) {
            currentLine = line;
        }
    }

    public NarrativeLine getCurrentLine() {
        return currentLine;
    }

    public void selectOption(int index) {
        if (currentLine == null) return;
    
        if (currentLine.hasOptions()) {
            NarrativeOption option = currentLine.getOptions().get(index);
            currentLine = lines.get(option.getNextId());
        } else if (currentLine.getNextId() != null) {
            currentLine = lines.get(currentLine.getNextId());
        } else {
            currentLine = null;
        }
    }

    public void advance() {
        if (currentLine != null && currentLine.getNextId() != null) {
            currentLine = lines.get(currentLine.getNextId());
        } else {
            currentLine = null;
        }
    }

    public boolean isFinished() {
        return currentLine == null;
    }
}