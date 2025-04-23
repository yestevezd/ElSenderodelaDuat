package com.yestevezd.elsenderodeladuat.core.narrative;

import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import java.util.List;

/**
 * Representa una línea de narrativa en el juego.
 * Puede incluir texto jeroglífico con formato enriquecido, traducción al idioma del jugador,
 * y metadatos como el personaje que habla y el tipo de narrativa (diálogo, narración, pensamiento...).
 */
public class NarrativeLine {

    private String speaker;                 
    private String hieroglyphic;        
    private String translation;           
    private NarrativeType type;          
    private String hieroglyphicRichText;    
    private List<HieroglyphicChar> parsedHieroglyphics;
    private List<NarrativeOption> options;
    private String nextId; 
    private String id;

    // Constructor 
    public NarrativeLine() {
    }

    /**
     * Constructor para líneas sin formato jeroglífico enriquecido.
     *
     * @param speaker      Nombre del personaje que dice la línea.
     * @param hieroglyphic Texto jeroglífico simple (sin etiquetas).
     * @param translation  Traducción al idioma principal del juego.
     * @param type         Tipo de narrativa (diálogo, pensamiento, narración...).
     */
    public NarrativeLine(String speaker, String hieroglyphic, String translation, NarrativeType type) {
        this(speaker, hieroglyphic, translation, type, null);
    }

    /**
     * Constructor para líneas con texto jeroglífico enriquecido.
     *
     * @param speaker              Nombre del personaje que dice la línea.
     * @param hieroglyphic         Texto jeroglífico simple (sin formato).
     * @param translation          Traducción al idioma del jugador.
     * @param type                 Tipo de narrativa.
     * @param hieroglyphicRichText Texto jeroglífico con formato enriquecido (e.g. [1]A[2]B).
     */
    public NarrativeLine(String speaker, String hieroglyphic, String translation, NarrativeType type, String hieroglyphicRichText) {
        this.speaker = speaker;
        this.hieroglyphic = hieroglyphic;
        this.translation = translation;
        this.type = type;
        this.hieroglyphicRichText = hieroglyphicRichText;
    }

    /**
     * Devuelve el nombre del personaje que dice esta línea.
     *
     * @return Nombre del personaje.
     */
    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    /**
     * Devuelve el texto jeroglífico plano (sin formato).
     *
     * @return Texto jeroglífico básico.
     */
    public String getHieroglyphic() {
        return hieroglyphic;
    }

    public void setHieroglyphic(String hieroglyphic) {
        this.hieroglyphic = hieroglyphic;
    }

    /**
     * Devuelve la traducción al idioma principal del juego.
     *
     * @return Traducción textual.
     */
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    /**
     * Devuelve el tipo de narrativa (diálogo, narración, etc.).
     *
     * @return Tipo de narrativa.
     */
    public NarrativeType getType() {
        return type;
    }

    public void setType(NarrativeType type) {
        this.type = type;
    }

    /**
     * Devuelve el texto jeroglífico enriquecido si existe, o el plano si no.
     * Se usa para renderizar.
     *
     * @return Texto jeroglífico efectivo.
     */
    public String getEffectiveHieroglyphic() {
        return hieroglyphicRichText != null ? hieroglyphicRichText : hieroglyphic;
    }

    /**
     * Establece la lista de caracteres jeroglíficos ya procesados a partir del texto enriquecido.
     *
     * @param parsed Lista de caracteres jeroglíficos con fuentes.
     */
    public void setParsedHieroglyphics(List<HieroglyphicChar> parsed) {
        this.parsedHieroglyphics = parsed;
    }

    /**
     * Obtiene la lista ya procesada de caracteres jeroglíficos para dibujar en pantalla.
     *
     * @return Lista de objetos {@link HieroglyphicChar}.
     */
    public List<HieroglyphicChar> getParsedHieroglyphics() {
        return parsedHieroglyphics;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNextId() {
        return nextId;
    }
    
    public void setNextId(String nextId) {
        this.nextId = nextId;
    }
    
    public List<NarrativeOption> getOptions() {
        return options;
    }
    
    public void setOptions(List<NarrativeOption> options) {
        this.options = options;
    }

    public void setHieroglyphicRichText(String hieroglyphicRichText) {
        this.hieroglyphicRichText = hieroglyphicRichText;
    }
    
    public boolean hasOptions() {
        return options != null && !options.isEmpty();
    }
}