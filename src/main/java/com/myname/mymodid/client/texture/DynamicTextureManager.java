package com.myname.mymodid.client.texture;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import com.myname.mymodid.character.BodyColor;
import com.myname.mymodid.character.HairColor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DynamicTextureManager {

    private static final DynamicTextureManager INSTANCE = new DynamicTextureManager();
    private final Map<String, ResourceLocation> textureCache = new HashMap<>();

    public static DynamicTextureManager getInstance() {
        return INSTANCE;
    }

    public ResourceLocation getOrCreateSaiyanTexture(BodyColor bodyColor, HairColor hairColor) {
        String key = "saiyan_" + bodyColor.ordinal() + "_" + hairColor.ordinal();

        if (textureCache.containsKey(key)) {
            return textureCache.get(key);
        }

        BufferedImage image = SaiyanTextureGenerator.generate(bodyColor, hairColor);
        DynamicTexture dynamicTexture = new DynamicTexture(image);
        ResourceLocation location = Minecraft.getMinecraft()
            .getTextureManager()
            .getDynamicTextureLocation("dbx_" + key, dynamicTexture);

        textureCache.put(key, location);
        return location;
    }

    public void clearCache() {
        textureCache.clear();
    }
}
