package com.myname.mymodid.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceLocationBinder {

    public static void bind(ResourceLocation location) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(location);
    }
}
