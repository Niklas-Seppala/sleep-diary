package com.example.sleepdiary.data;

public enum SleepRating {
    UNDEFINED,
    BAD,
    OK,
    GOOD;

    public static SleepRating fromInt(int asInt) {
        switch (asInt) {
            case 1: return SleepRating.BAD;
            case 2: return SleepRating.OK;
            case 3: return SleepRating.GOOD;
            case 0: return SleepRating.UNDEFINED;
            default: return null;
        }
    }

    public int toInt() {
        switch (this) {
            case BAD: return 1;
            case OK: return 2;
            case GOOD: return 3;
            case UNDEFINED:
            default:  return 0;
        }
    }
}
