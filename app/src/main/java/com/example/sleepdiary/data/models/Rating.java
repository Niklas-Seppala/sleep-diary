package com.example.sleepdiary.data.models;

/**
 * User ratings
 */
public enum Rating {
    UNDEFINED(0),
    VERY_BAD(1),
    BAD(2),
    OK(3),
    GOOD(4),
    VERY_GOOD(5);

    private final int value;
    private Rating(int val){
        this.value = val;
    }

    /**
     * Creates Rating enum from Integer.
     * @param integer Integer to convert
     * @return Rating enum
     */
    public static Rating fromInt(int integer) {
        switch (integer) {
            case 1: return Rating.VERY_BAD;
            case 2: return Rating.BAD;
            case 3: return Rating.OK;
            case 4: return Rating.GOOD;
            case 5: return Rating.VERY_GOOD;
            case 0: return Rating.UNDEFINED;
            default: return null;
        }
    }

    /**
     * Converts Rating enum to Integer.
     * @return Rating as int
     */
    public int toInt() {
        return value;
    }
}
