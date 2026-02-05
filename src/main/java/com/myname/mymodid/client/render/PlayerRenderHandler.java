package com.myname.mymodid.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import com.myname.mymodid.character.CharacterData;
import com.myname.mymodid.character.Race;
import com.myname.mymodid.client.model.ModelSaiyan;
import com.myname.mymodid.client.texture.DynamicTextureManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerRenderHandler {

    private final ModelSaiyan saiyanModel = new ModelSaiyan();

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        CharacterData data = CharacterData.get(event.entityPlayer);
        if (data == null || !data.isCreated() || data.getRace() != Race.SAIYAN) {
            return;
        }

        // Cancel the default render - we render our custom model instead
        event.setCanceled(true);

        AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
        float partialTicks = event.partialRenderTick;

        saiyanModel.setHairStyle(data.getHairStyle().ordinal());

        ResourceLocationBinder.bind(
            DynamicTextureManager.getInstance()
                .getOrCreateSaiyanTexture(data.getBodyColor(), data.getHairColor()));

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glTranslatef((float) event.x, (float) event.y, (float) event.z);

        float bodyYaw = interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
        GL11.glRotatef(180.0F - bodyYaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glTranslatef(0.0F, -1.5078125F, 0.0F);

        // f7 = interpolated swing amplitude, f8 = interpolated swing position
        float swingAmount = player.prevLimbSwingAmount
            + (player.limbSwingAmount - player.prevLimbSwingAmount) * partialTicks;
        float swingPos = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);

        if (swingAmount > 1.0F) {
            swingAmount = 1.0F;
        }

        float ageInTicks = player.ticksExisted + partialTicks;
        float headYaw = interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks)
            - bodyYaw;
        float headPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        saiyanModel
            .setRotationAngles(swingPos, swingAmount, ageInTicks, headYaw, headPitch, 0.0625F, player);
        saiyanModel.render(player, swingPos, swingAmount, ageInTicks, headYaw, headPitch, 0.0625F);

        GL11.glPopMatrix();

        // Render nametag manually
        if (shouldRenderName(player)) {
            renderNameTag(player, event.x, event.y, event.z);
        }
    }

    private boolean shouldRenderName(AbstractClientPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) {
            return false;
        }
        return player.getAlwaysRenderNameTagForRender();
    }

    private void renderNameTag(AbstractClientPlayer player, double x, double y, double z) {
        double distSq = player.getDistanceSqToEntity(RenderManager.instance.livingPlayer);
        float maxDist = player.isSneaking() ? 32.0F : 64.0F;
        if (distSq > (double) (maxDist * maxDist)) {
            return;
        }

        String name = player.getDisplayName();
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        float scale = 1.6F;
        float nameScale = scale * 0.016666668F;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + player.height + 0.5F, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-nameScale, -nameScale, nameScale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Tessellator tessellator = Tessellator.instance;
        int halfWidth = fontRenderer.getStringWidth(name) / 2;

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-halfWidth - 1, -1, 0.0);
        tessellator.addVertex(-halfWidth - 1, 8, 0.0);
        tessellator.addVertex(halfWidth + 1, 8, 0.0);
        tessellator.addVertex(halfWidth + 1, -1, 0.0);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Render name text with slight transparency first (behind depth)
        fontRenderer.drawString(name, -halfWidth, 0, 0x20FFFFFF);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        // Render name text fully opaque (in front of depth)
        fontRenderer.drawString(name, -halfWidth, 0, 0xFFFFFFFF);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }

    private float interpolateRotation(float prev, float next, float partial) {
        float diff = next - prev;
        while (diff < -180.0F) {
            diff += 360.0F;
        }
        while (diff >= 180.0F) {
            diff -= 360.0F;
        }
        return prev + partial * diff;
    }
}
