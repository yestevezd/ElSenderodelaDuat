package com.yestevezd.elsenderodeladuat.core.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicParser;

import java.util.ArrayList;
import java.util.List;

public class NarrativeManager {

    private final List<NarrativeLine> lines = new ArrayList<>();
    private final float charDelay = 0.07f;
    private final float autoAdvanceDelay = 0.3f; // Tiempo tras mostrar línea antes de avanzar automáticamente

    private int currentLineIndex = 0;
    private float timer = 0f;
    private int charCount = 0;
    private float delayTimer = 0f;
    private boolean waitingNextLine = false;

    private BitmapFont[] hieroglyphicFonts;

    public void setHieroglyphicFonts(BitmapFont... fonts) {
        this.hieroglyphicFonts = fonts;
    }

    public void addLine(NarrativeLine line) {
        lines.add(line);

        if (line.getType() == NarrativeType.CONTEXTUALIZED) {
            String rich = line.getEffectiveHieroglyphic();
            List<HieroglyphicChar> parsed = HieroglyphicParser.parse(rich, hieroglyphicFonts);
            line.setParsedHieroglyphics(parsed);
        }
    }

    public void update(float delta) {
        if (isFinished()) return;

        if (!waitingNextLine) {
            timer += delta;
            while (timer >= charDelay) {
                timer -= charDelay;
                charCount++;

                if (isLineFullyShown()) {
                    waitingNextLine = true;
                    delayTimer = 0f;
                    break;
                }
            }
        } else {
            delayTimer += delta;
            if (delayTimer >= autoAdvanceDelay) {
                nextLine();
                waitingNextLine = false;
            }
        }
    }

    public NarrativeLine getCurrentLine() {
        return lines.get(currentLineIndex);
    }

    public boolean isLineFullyShown() {
        NarrativeLine line = getCurrentLine();
        int hieroLength = line.getParsedHieroglyphics() != null ? line.getParsedHieroglyphics().size() : 0;
        int totalLength = hieroLength + line.getTranslation().length();
        return charCount >= totalLength;
    }

    public boolean isFinished() {
        return currentLineIndex >= lines.size();
    }

    public void nextLine() {
        currentLineIndex++;
        charCount = 0;
        timer = 0;
    }

    public List<NarrativeLine> getVisibleLines() {
        return lines.subList(0, Math.min(currentLineIndex + 1, lines.size()));
    }

    public int getCharIndexForLine(int index) {
        return index == currentLineIndex ? charCount : Integer.MAX_VALUE;
    }
}