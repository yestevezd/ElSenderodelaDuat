package com.yestevezd.elsenderodeladuat.core.narrative.dialogues;

import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;

public class DialogueManager {

    private final DialogueTree dialogueTree;
    private final DialogueBox dialogueBox;
    private boolean active = false;
    private boolean waitingForAdvance = false;
    private String lastNodeId = null;

    private float inputCooldown = 0.2f;
    private float elapsedSinceStart = 0f;

    public DialogueManager(DialogueTree tree, DialogueBox box) {
        this.dialogueTree = tree;
        this.dialogueBox = box;
    }

    public void start() {
        active = true;
        elapsedSinceStart = 0f;
        waitingForAdvance = false;
        showCurrentLine();
    }

    public void update(float delta) {
        if (!active || !dialogueBox.isVisible()) return;
    
        elapsedSinceStart += delta;
        if (elapsedSinceStart < inputCooldown) return;
    
        dialogueBox.update();
    
        NarrativeLine current = dialogueTree.getCurrentLine();
        if (current == null) {
            dialogueBox.hide();
            active = false;
            return;
        }
    
        lastNodeId = current.getId();
    
        if (dialogueBox.isOptionsVisible()) {
            if (InputManager.isInteractPressed()) {
                int selected = dialogueBox.getSelectedOptionIndex();
                dialogueTree.selectOption(selected);
                showCurrentLine();
            }
        } else if (waitingForAdvance && InputManager.isInteractPressed()) {
            if (current.getNextId() != null) {
                dialogueTree.selectOption(0);
                showCurrentLine();
            } else {
                dialogueTree.selectOption(0);
                dialogueBox.hide();
                active = false;
            }
        }
    }
    

    private void showCurrentLine() {
        NarrativeLine line = dialogueTree.getCurrentLine();
        if (line == null) return;
    
        String fullText = line.getSpeaker() + ": " + line.getTranslation();
    
        if (line.hasOptions()) {
            dialogueBox.showWithOptions(fullText, line.getOptions());
            waitingForAdvance = false;
        } else {
            dialogueBox.show(fullText);
            waitingForAdvance = true;
        }
    }
    

    public boolean isActive() {
        return active;
    }

    public String getCurrentNodeId() {
        return lastNodeId;
    }
}
