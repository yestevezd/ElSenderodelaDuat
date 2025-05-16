package com.yestevezd.elsenderodeladuat.core.save;

import com.yestevezd.elsenderodeladuat.core.entities.*;

public class ItemFactory {
    public static Item createById(String id) {
        switch(id) {
            case "escarabajo": 
                return new Escarabajo(); 
            case "espada": 
                return new Espada();
            case "estatua_dios_amon":
                return new Amuleto_estatua();
            case "anj":
                return new Anj();
            case"sandalias":
                return new Sandalias();
            case "libro_de_los_muertos":
                return new Libro_de_los_muertos();
            default:
                throw new IllegalArgumentException("Item desconocido: " + id);
        }
    }
}
