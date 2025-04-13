package com.yestevezd.elsenderodeladuat.core.narrative;

import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import java.util.List;

/**
 * Representa una línea de narrativa en el juego.
 * Puede incluir texto jeroglífico con formato enriquecido, traducción al idioma del jugador,
 * y metadatos como el personaje que habla y el tipo de narrativa (diálogo, narración, pensamiento...).
 */
public class NarrativeLine {

    private final String speaker;                 
    private final String hieroglyphic;        
    private final String translation;           
    private final NarrativeType type;          
    private final String hieroglyphicRichText;    
    private List<HieroglyphicChar> parsedHieroglyphics;

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

    /**
     * Devuelve el texto jeroglífico plano (sin formato).
     *
     * @return Texto jeroglífico básico.
     */
    public String getHieroglyphic() {
        return hieroglyphic;
    }

    /**
     * Devuelve la traducción al idioma principal del juego.
     *
     * @return Traducción textual.
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Devuelve el tipo de narrativa (diálogo, narración, etc.).
     *
     * @return Tipo de narrativa.
     */
    public NarrativeType getType() {
        return type;
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
}