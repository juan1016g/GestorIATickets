package com.gestoria.tickets.persistence.entity.enums;

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
    }}
