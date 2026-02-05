package com.myname.mymodid.character;

public enum BodyColor {

    LIGHT(0xFF, 0xDC, 0xB5, "Light"),
    MEDIUM(0xD4, 0xA5, 0x74, "Medium"),
    TAN(0xC6, 0x8C, 0x53, "Tan"),
    DARK(0x8D, 0x5C, 0x2A, "Dark"),
    DEEP(0x5C, 0x3A, 0x1E, "Deep");

    private final int red;
    private final int green;
    private final int blue;
    private final String displayName;

    BodyColor(int red, int green, int blue, String displayName) {
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

    public static BodyColor fromOrdinal(int ordinal) {
        BodyColor[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return LIGHT;
        }
        return values[ordinal];
    }
}
