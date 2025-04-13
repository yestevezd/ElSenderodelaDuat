package com.yestevezd.elsenderodeladuat.core.narrative;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra la narrativa del juego línea por línea.
 * Controla la lógica de tiempo de aparición de caracteres y transiciones automáticas entre líneas.
 */
public class NarrativeManager {

    private final List<NarrativeLine> lines = new ArrayList<>(); 

    private final float charDelay = 0.07f;         
    private final float autoAdvanceDelay = 0.3f; 

    private int currentLineIndex = 0;  
    private float timer = 0f;         
    private int charCount = 0;         
    private float delayTimer = 0f;   
    private boolean waitingNextLine = false; 

    private BitmapFont[] hieroglyphicFonts; 

    /**
     * Asigna las fuentes jeroglíficas que se usarán para el parseo del texto enriquecido.
     *
     * @param fonts Array de fuentes BitmapFont.
     */
    public void setHieroglyphicFonts(BitmapFont... fonts) {
        this.hieroglyphicFonts = fonts;
    }

    /**
     * Añade una línea a la narrativa.
     * Si la línea es de tipo CONTEXTUALIZED, también se parsea el texto jeroglífico enriquecido.
     *
     * @param line Línea de narrativa a añadir.
     */
    public void addLine(NarrativeLine line) {
        lines.add(line);

        if (line.getType() == NarrativeType.CONTEXTUALIZED) {
            String rich = line.getEffectiveHieroglyphic();
            List<HieroglyphicChar> parsed = HieroglyphicParser.parse(rich, hieroglyphicFonts);
            line.setParsedHieroglyphics(parsed);
        }
    }

    /**
     * Actualiza el estado del sistema narrativo, animando los caracteres y gestionando el tiempo de transición.
     *
     * @param delta Tiempo transcurrido desde el último frame.
     */
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

    /**
     * Devuelve la línea narrativa actual.
     *
     * @return Línea actual.
     */
    public NarrativeLine getCurrentLine() {
        return lines.get(currentLineIndex);
    }

    /**
     * Determina si todos los caracteres de la línea actual han sido mostrados.
     *
     * @return true si la línea se ha mostrado completamente.
     */
    public boolean isLineFullyShown() {
        NarrativeLine line = getCurrentLine();
        int hieroLength = line.getParsedHieroglyphics() != null ? line.getParsedHieroglyphics().size() : 0;
        int totalLength = hieroLength + line.getTranslation().length();
        return charCount >= totalLength;
    }

    /**
     * Indica si se han mostrado todas las líneas de narrativa.
     *
     * @return true si no quedan líneas por mostrar.
     */
    public boolean isFinished() {
        return currentLineIndex >= lines.size();
    }

    /**
     * Avanza manual o automáticamente a la siguiente línea narrativa.
     */
    public void nextLine() {
        currentLineIndex++;
        charCount = 0;
        timer = 0;
    }

    /**
     * Devuelve todas las líneas que deben ser visibles hasta el momento,
     * incluyendo la que se está escribiendo parcialmente.
     *
     * @return Lista parcial de líneas visibles.
     */
    public List<NarrativeLine> getVisibleLines() {
        return lines.subList(0, Math.min(currentLineIndex + 1, lines.size()));
    }

    /**
     * Obtiene el número de caracteres visibles de una línea específica.
     *
     * @param index Índice de la línea.
     * @return Número de caracteres visibles si es la actual, o Integer.MAX_VALUE si es anterior (totalmente visible).
     */
    public int getCharIndexForLine(int index) {
        return index == currentLineIndex ? charCount : Integer.MAX_VALUE;
    }
}