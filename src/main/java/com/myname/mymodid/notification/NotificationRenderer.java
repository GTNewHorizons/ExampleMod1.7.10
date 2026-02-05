package com.myname.mymodid.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotificationRenderer extends Gui {

    private static final ResourceLocation ACHIEVEMENT_TEXTURES = new ResourceLocation(
        "textures/gui/achievement/achievement_background.png");

    private static final int POPUP_WIDTH = 160;
    private static final int POPUP_HEIGHT = 32;

    private static final int TEX_U = 96;
    private static final int TEX_V = 202;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        NotificationManager manager = NotificationManager.getInstance();
        manager.update();

        Notification notification = manager.getCurrent();
        if (notification == null) {
            return;
        }

        double slideProgress = manager.getSlideProgress();
        if (slideProgress <= 0.0) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenWidth = sr.getScaledWidth();

        int targetX = screenWidth - POPUP_WIDTH;
        int offsetX = (int) ((1.0 - slideProgress) * POPUP_WIDTH);
        int drawX = targetX + offsetX;
        int drawY = 0;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager()
            .bindTexture(ACHIEVEMENT_TEXTURES);
        drawTexturedModalRect(drawX, drawY, TEX_U, TEX_V, POPUP_WIDTH, POPUP_HEIGHT);

        mc.fontRenderer.drawString(notification.getTitle(), drawX + 6, drawY + 7, 0xFFFF00);
        mc.fontRenderer.drawString(notification.getDescription(), drawX + 6, drawY + 18, 0xFFFFFF);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
