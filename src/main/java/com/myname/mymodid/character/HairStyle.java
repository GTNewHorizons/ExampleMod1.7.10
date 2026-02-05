package com.myname.mymodid.character;

public enum HairStyle {

    BASE("Base"),
    SPIKY("Spiky"),
    LONG("Long"),
    WILD("Wild");

    private final String displayName;

    HairStyle(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static HairStyle fromOrdinal(int ordinal) {
        HairStyle[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return BASE;
        }
        return values[ordinal];
    }
}
