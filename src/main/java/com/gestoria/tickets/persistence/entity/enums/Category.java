package com.gestoria.tickets.persistence.entity.enums;

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

    public String getCategoryName() {
        return this.categoryName;
    }}
