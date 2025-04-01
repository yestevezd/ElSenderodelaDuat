package com.yestevezd.elsenderodeladuat.core.narrative;

import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import java.util.List;

public class NarrativeLine {
    private final String speaker;
    private final String hieroglyphic;
    private final String translation;
    private final NarrativeType type;
    private final String hieroglyphicRichText;
    private List<HieroglyphicChar> parsedHieroglyphics;

    public NarrativeLine(String speaker, String hieroglyphic, String translation, NarrativeType type) {
        this(speaker, hieroglyphic, translation, type, null);
    }

    public NarrativeLine(String speaker, String hieroglyphic, String translation, NarrativeType type, String hieroglyphicRichText) {
        this.speaker = speaker;
        this.hieroglyphic = hieroglyphic;
        this.translation = translation;
        this.type = type;
        this.hieroglyphicRichText = hieroglyphicRichText;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getHieroglyphic() {
        return hieroglyphic;
    }

    public String getTranslation() {
        return translation;
    }

    public NarrativeType getType() {
        return type;
    }

    public String getEffectiveHieroglyphic() {
        return hieroglyphicRichText != null ? hieroglyphicRichText : hieroglyphic;
    }

    public void setParsedHieroglyphics(List<HieroglyphicChar> parsed) {
        this.parsedHieroglyphics = parsed;
    }

    public List<HieroglyphicChar> getParsedHieroglyphics() {
        return parsedHieroglyphics;
    }
}