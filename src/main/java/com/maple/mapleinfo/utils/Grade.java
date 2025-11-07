package com.maple.mapleinfo.utils;

public enum Grade {

    RARE, EPIC, UNIQUE, LEGENDARY;

    public Grade next() {
        return switch (this) {
            case RARE -> EPIC;
            case EPIC -> UNIQUE;
            default -> LEGENDARY;
        };
    }
}
