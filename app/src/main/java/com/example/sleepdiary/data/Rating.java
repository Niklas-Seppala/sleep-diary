package com.example.sleepdiary.data;

/**
 * User ratings
 */
public enum Rating {
    BAD(1),
    OK(2),
    GOOD(3),
    UNDEFINED(0);

    // Internal value
    private final int value;
    // Store rating to internal field.
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
            case 1: return Rating.BAD;
            case 2: return Rating.OK;
            case 3: return Rating.GOOD;
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
