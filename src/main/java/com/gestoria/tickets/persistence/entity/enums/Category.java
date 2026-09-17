package com.gestoria.tickets.persistence.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    HARDWARE("HARDWARE"),
    SOFTWARE("SOFTWARE"),
    CONNECTIVITY("CONECTIVIDAD"),
    ACCESS("ACCESOS"),
    OTHER("OTRO");

    private final String categoryName;

    Category(String categoryName){
        this.categoryName = categoryName;
    }

    @JsonValue
    public String getCategoryName() {
        return this.categoryName;
    }

    @JsonCreator
    public static Category fromSpanish(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (Category c : Category.values()) {
            if (c.categoryName.equalsIgnoreCase(text.trim()) || c.name().equalsIgnoreCase(text.trim())) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + text);
    }

}
