package com.maple.mapleinfo.utils;

public final class CubeLimitConstants {

    private CubeLimitConstants() {}

    public static final int RARE_TO_EPIC_DEFAULT_LIMIT = 10;
    public static final int EPIC_TO_UNIQUE_DEFAULT_LIMIT = 42;
    public static final int UNIQUE_TO_LEGENDARY_DEFAULT_LIMIT = 107;
    public static final int RARE_TO_EPIC_ADDITIONAL_LIMIT = 31;
    public static final int EPIC_TO_UNIQUE_ADDITIONAL_LIMIT = 76;
    public static final int UNIQUE_TO_LEGENDARY_ADDITIONAL_LIMIT = 214;
    public static final int LEGENDARY_LIMIT = 0;

    public static int findLimit(Grade grade, CubeType type) {
        if (grade == Grade.LEGENDARY) {
            return LEGENDARY_LIMIT;
        }

        if (type == CubeType.DEFAULT) {
            return switch (grade) {
                case RARE -> RARE_TO_EPIC_DEFAULT_LIMIT;
                case EPIC -> EPIC_TO_UNIQUE_DEFAULT_LIMIT;
                case UNIQUE -> UNIQUE_TO_LEGENDARY_DEFAULT_LIMIT;
                default -> LEGENDARY_LIMIT;
            };
        }

        if (type == CubeType.ADDITIONAL) {
            return switch (grade) {
                case RARE -> RARE_TO_EPIC_ADDITIONAL_LIMIT;
                case EPIC -> EPIC_TO_UNIQUE_ADDITIONAL_LIMIT;
                case UNIQUE -> UNIQUE_TO_LEGENDARY_ADDITIONAL_LIMIT;
                default -> LEGENDARY_LIMIT;
            };
        }
        return LEGENDARY_LIMIT;
    }
}
