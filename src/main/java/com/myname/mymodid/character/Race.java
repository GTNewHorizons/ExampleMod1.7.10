package com.myname.mymodid.character;

public enum Race {

    NONE("None"),
    SAIYAN("Saiyan");

    private final String displayName;

    Race(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Race fromOrdinal(int ordinal) {
        Race[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return NONE;
        }
        return values[ordinal];
    }
}
