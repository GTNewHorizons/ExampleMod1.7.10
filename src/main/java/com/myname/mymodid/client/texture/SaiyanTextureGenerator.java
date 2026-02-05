package com.myname.mymodid.client.texture;

import java.awt.image.BufferedImage;

import com.myname.mymodid.character.BodyColor;
import com.myname.mymodid.character.HairColor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaiyanTextureGenerator {

    private static final int TEX_WIDTH = 64;
    private static final int TEX_HEIGHT = 64;

    public static BufferedImage generate(BodyColor bodyColor, HairColor hairColor) {
        BufferedImage image = new BufferedImage(TEX_WIDTH, TEX_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        int skinBase = toARGB(bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue());
        int skinShadow = toARGB(
            (int) (bodyColor.getRed() * 0.75),
            (int) (bodyColor.getGreen() * 0.75),
            (int) (bodyColor.getBlue() * 0.75));

        int hairBase = toARGB(hairColor.getRed(), hairColor.getGreen(), hairColor.getBlue());
        int hairHighlight = toARGB(
            Math.min(255, (int) (hairColor.getRed() * 1.4)),
            Math.min(255, (int) (hairColor.getGreen() * 1.4)),
            Math.min(255, (int) (hairColor.getBlue() * 1.4)));

        // Gi colors (orange Saiyan gi)
        int giBase = toARGB(0xFF, 0x6A, 0x00);
        int giShadow = toARGB(0xCC, 0x55, 0x00);

        // Blue undershirt
        int shirtBase = toARGB(0x22, 0x44, 0xBB);
        int shirtShadow = toARGB(0x1A, 0x33, 0x88);

        // Belt
        int beltColor = toARGB(0x33, 0x33, 0xCC);

        // Boots
        int bootBase = toARGB(0x22, 0x44, 0xBB);
        int bootShadow = toARGB(0x1A, 0x33, 0x88);

        // Tail fur
        int tailBase = toARGB(0x8B, 0x6B, 0x3E);
        int tailShadow = toARGB(0x6B, 0x4E, 0x2A);

        // --- Head (0,0 to 32,16) region in standard skin layout ---
        // Face area: front of head (8,8 to 16,16)
        fillRect(image, 8, 0, 8, 8, skinBase); // Top of head
        fillRect(image, 0, 8, 8, 8, skinBase); // Right side of head
        fillRect(image, 8, 8, 8, 8, skinBase); // Face
        fillRect(image, 16, 8, 8, 8, skinBase); // Left side of head
        fillRect(image, 24, 8, 8, 8, skinBase); // Back of head

        // Eyes on face
        int eyeWhite = toARGB(0xFF, 0xFF, 0xFF);
        int eyeIris = toARGB(0x11, 0x11, 0x11);
        // Left eye
        image.setRGB(10, 12, eyeWhite);
        image.setRGB(11, 12, eyeIris);
        // Right eye
        image.setRGB(12, 12, eyeIris);
        image.setRGB(13, 12, eyeWhite);

        // Mouth
        int mouthColor = toARGB(
            (int) (bodyColor.getRed() * 0.6),
            (int) (bodyColor.getGreen() * 0.5),
            (int) (bodyColor.getBlue() * 0.5));
        image.setRGB(11, 14, mouthColor);
        image.setRGB(12, 14, mouthColor);

        // Facial shadow under eyes
        image.setRGB(10, 13, skinShadow);
        image.setRGB(13, 13, skinShadow);

        // --- Body (16,16 to 40,32) region ---
        // Front of body (20,20 to 28,32)
        fillRect(image, 20, 20, 8, 12, giBase);
        // Add gi shadow on sides
        fillRect(image, 20, 20, 1, 12, giShadow);
        fillRect(image, 27, 20, 1, 12, giShadow);
        // Belt in middle
        fillRect(image, 20, 27, 8, 2, beltColor);
        // V-neck showing undershirt
        image.setRGB(23, 20, shirtBase);
        image.setRGB(24, 20, shirtBase);
        image.setRGB(23, 21, shirtBase);
        image.setRGB(24, 21, shirtBase);

        // Back of body
        fillRect(image, 32, 20, 8, 12, giBase);
        fillRect(image, 32, 20, 1, 12, giShadow);
        fillRect(image, 39, 20, 1, 12, giShadow);
        fillRect(image, 32, 27, 8, 2, beltColor);

        // Left/right sides of body
        fillRect(image, 16, 20, 4, 12, giShadow);
        fillRect(image, 28, 20, 4, 12, giShadow);
        // Top of body
        fillRect(image, 20, 16, 8, 4, giBase);

        // --- Arms (40,16 to 56,32) ---
        // Arm front
        fillRect(image, 44, 20, 4, 12, shirtBase);
        fillRect(image, 44, 20, 4, 5, shirtBase);
        // Lower arm is skin
        fillRect(image, 44, 25, 4, 7, skinBase);
        fillRect(image, 44, 25, 1, 7, skinShadow);

        // Arm back/sides
        fillRect(image, 48, 20, 4, 12, shirtShadow);
        fillRect(image, 40, 20, 4, 12, shirtShadow);
        fillRect(image, 52, 20, 4, 12, shirtBase);

        // Lower half skin
        fillRect(image, 48, 25, 4, 7, skinShadow);
        fillRect(image, 40, 25, 4, 7, skinShadow);
        fillRect(image, 52, 25, 4, 7, skinBase);

        // Arm top
        fillRect(image, 44, 16, 4, 4, shirtBase);

        // --- Legs (0,16 to 16,32) ---
        // Leg front
        fillRect(image, 4, 20, 4, 12, giBase);
        // Lower part: boots
        fillRect(image, 4, 26, 4, 6, bootBase);
        fillRect(image, 4, 26, 1, 6, bootShadow);

        // Leg back/sides
        fillRect(image, 8, 20, 4, 12, giShadow);
        fillRect(image, 0, 20, 4, 12, giShadow);
        fillRect(image, 12, 20, 4, 12, giBase);
        fillRect(image, 8, 26, 4, 6, bootShadow);
        fillRect(image, 0, 26, 4, 6, bootShadow);
        fillRect(image, 12, 26, 4, 6, bootBase);

        // Leg top
        fillRect(image, 4, 16, 4, 4, giBase);

        // --- Hair region (0,32 to 36,48) ---
        // Hair base volume
        fillRect(image, 0, 32, 36, 10, hairBase);
        // Add highlights
        for (int x = 0; x < 36; x += 3) {
            for (int y = 32; y < 37; y += 2) {
                if (x < TEX_WIDTH && y < TEX_HEIGHT) {
                    image.setRGB(x, y, hairHighlight);
                }
            }
        }

        // Hair spikes region (36,32 to 44,48)
        fillRect(image, 36, 32, 8, 10, hairBase);
        fillRect(image, 36, 32, 2, 3, hairHighlight);
        fillRect(image, 40, 33, 2, 2, hairHighlight);

        // Hair back region (44,32 to 56,48)
        fillRect(image, 44, 32, 12, 10, hairBase);
        fillRect(image, 44, 32, 2, 3, hairHighlight);
        fillRect(image, 50, 34, 2, 2, hairHighlight);

        // --- Tail region (0,46 to 12,64) ---
        // Tail segment 1
        fillRect(image, 0, 46, 7, 3, tailBase);
        image.setRGB(0, 46, tailShadow);
        image.setRGB(6, 46, tailShadow);
        image.setRGB(0, 48, tailShadow);
        image.setRGB(6, 48, tailShadow);

        // Tail segment 2
        fillRect(image, 0, 52, 7, 3, tailBase);
        image.setRGB(0, 52, tailShadow);
        image.setRGB(6, 52, tailShadow);

        // Tail segment 3 (tip)
        fillRect(image, 0, 58, 6, 3, tailBase);
        image.setRGB(0, 58, tailShadow);
        image.setRGB(5, 58, tailShadow);
        // Lighter tip
        image.setRGB(3, 59, tailShadow);
        image.setRGB(4, 59, tailShadow);

        return image;
    }

    private static void fillRect(BufferedImage image, int x, int y, int width, int height, int color) {
        for (int px = x; px < x + width && px < image.getWidth(); px++) {
            for (int py = y; py < y + height && py < image.getHeight(); py++) {
                image.setRGB(px, py, color);
            }
        }
    }

    private static int toARGB(int r, int g, int b) {
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
