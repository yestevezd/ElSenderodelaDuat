package com.yestevezd.elsenderodeladuat.core.narrative.dialogues;

import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.ui.DialogueBox;

public class DialogueManager {

    private final DialogueTree dialogueTree;
    private final DialogueBox dialogueBox;
    private boolean active = false;
    private boolean waitingForAdvance = false;

    public DialogueManager(DialogueTree tree, DialogueBox box) {
        this.dialogueTree = tree;
        this.dialogueBox = box;
    }

    public void start() {
        active = true;
        showCurrentLine();
    }

    public void update() {
        if (!active || !dialogueBox.isVisible()) return;
    
        dialogueBox.update();
    
        NarrativeLine current = dialogueTree.getCurrentLine();
    
        // Si ya no hay línea activa, cerramos
        if (current == null) {
            dialogueBox.hide();
            active = false;
            return;
        }
    
        if (dialogueBox.isOptionsVisible()) {
            if (InputManager.isInteractPressed()) {
                int selected = dialogueBox.getSelectedOptionIndex();
                dialogueTree.selectOption(selected);
                showCurrentLine();
            }
        } else if (waitingForAdvance && InputManager.isInteractPressed()) {
            // Solo avanzamos si hay nextId
            if (current.getNextId() != null) {
                dialogueTree.selectOption(0); // usamos el nextId directamente
                showCurrentLine();
            } else {
                // No hay más líneas que mostrar, fin
                dialogueTree.selectOption(0); // nos aseguramos de llegar a null
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
}
