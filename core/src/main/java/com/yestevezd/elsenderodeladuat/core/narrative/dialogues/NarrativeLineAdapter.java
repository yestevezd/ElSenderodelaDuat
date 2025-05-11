package com.yestevezd.elsenderodeladuat.core.narrative.dialogues;

import com.google.gson.*;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeOption;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NarrativeLineAdapter implements JsonDeserializer<NarrativeLine> {

    @Override
    public NarrativeLine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        NarrativeLine line = new NarrativeLine();

        line.setId(obj.get("id").getAsString());
        line.setSpeaker(obj.get("speaker").getAsString());
        line.setTranslation(obj.get("translation").getAsString());
        line.setType(NarrativeType.valueOf(obj.get("type").getAsString()));

        if (obj.has("hieroglyphic")) {
            line.setHieroglyphic(obj.get("hieroglyphic").getAsString());
        }

        if (obj.has("hieroglyphicRichText")) {
            line.setHieroglyphicRichText(obj.get("hieroglyphicRichText").getAsString());
        }

        if (obj.has("nextId")) {
            line.setNextId(obj.get("nextId").getAsString());
        }

        if (obj.has("options")) {
            List<NarrativeOption> options = new ArrayList<>();
            JsonArray arr = obj.getAsJsonArray("options");
            for (JsonElement e : arr) {
                JsonObject opt = e.getAsJsonObject();
                String text   = opt.get("text").getAsString();
                String nextId = opt.get("nextId").getAsString();
        
                Integer delta = null;
                if (opt.has("effect")) {
                    delta = opt.getAsJsonObject("effect").get("value").getAsInt();
                }
                options.add(new NarrativeOption(text, nextId, delta));
            }
            line.setOptions(options);
        }

        return line;
    }
}
