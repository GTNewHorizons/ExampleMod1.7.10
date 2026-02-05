package com.myname.mymodid.character;

public enum HairColor {

    BLACK(0x1A, 0x1A, 0x1A, "Black"),
    DARK_BROWN(0x3B, 0x1F, 0x0B, "Dark Brown"),
    BROWN(0x6B, 0x3A, 0x1F, "Brown"),
    RED(0x8B, 0x0A, 0x0A, "Red"),
    BLUE(0x0A, 0x0A, 0x6B, "Blue");

    private final int red;
    private final int green;
    private final int blue;
    private final String displayName;

    HairColor(int red, int green, int blue, String displayName) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.displayName = displayName;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int toPackedRGB() {
        return (red << 16) | (green << 8) | blue;
    }

    public static HairColor fromOrdinal(int ordinal) {
        HairColor[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return BLACK;
        }
        return values[ordinal];
    }
}
