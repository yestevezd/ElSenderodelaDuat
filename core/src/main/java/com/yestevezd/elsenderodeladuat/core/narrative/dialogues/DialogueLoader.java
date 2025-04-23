package com.yestevezd.elsenderodeladuat.core.narrative.dialogues;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;

import java.util.List;

public class DialogueLoader {

    public static DialogueTree load(String internalPath, String dialogueId) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(NarrativeLine.class, new NarrativeLineAdapter())
            .create();
    
        String json = AssetLoader.loadInternalText(internalPath);
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
    
        JsonObject dialogues = root.getAsJsonObject("dialogues");
        JsonObject selected = dialogues.getAsJsonObject(dialogueId);
    
        List<NarrativeLine> lines = gson.fromJson(
            selected.getAsJsonArray("lines"),
            new TypeToken<List<NarrativeLine>>() {}.getType()
        );
    
        DialogueTree tree = new DialogueTree();
        for (NarrativeLine line : lines) {
            tree.addLine(line);
        }
    
        return tree;
    }
}

