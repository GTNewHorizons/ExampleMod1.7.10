package com.myname.mymodid.client.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.myname.mymodid.character.CharacterData;
import com.myname.mymodid.character.Race;
import com.myname.mymodid.client.model.ModelSaiyan;
import com.myname.mymodid.client.texture.DynamicTextureManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Alternative approach for custom player rendering by extending RenderPlayer.
 * Currently unused - the mod uses PlayerRenderHandler (event-based) instead.
 * Kept as a reference for potential future use with RenderManager replacement.
 */
@SideOnly(Side.CLIENT)
public class RenderSaiyanPlayer extends RenderPlayer {

    private final ModelSaiyan saiyanModel;
    private final RenderPlayer originalRenderer;

    public RenderSaiyanPlayer(RenderPlayer original) {
        super();
        this.originalRenderer = original;
        this.saiyanModel = new ModelSaiyan();
        this.renderManager = RenderManager.instance;
    }

    @Override
    public void doRender(AbstractClientPlayer player, double x, double y, double z, float entityYaw,
        float partialTicks) {
        CharacterData data = CharacterData.get(player);

        if (data == null || !data.isCreated() || data.getRace() != Race.SAIYAN) {
            originalRenderer.doRender(player, x, y, z, entityYaw, partialTicks);
            return;
        }

        saiyanModel.setHairStyle(data.getHairStyle().ordinal());

        GL11.glPushMatrix();
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        ResourceLocation texture = DynamicTextureManager.getInstance()
            .getOrCreateSaiyanTexture(data.getBodyColor(), data.getHairColor());
        this.bindTexture(texture);

        float bodyYaw = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
        float headYawAbs = this.interpolateRotation(
            player.prevRotationYawHead,
            player.rotationYawHead,
            partialTicks);

        GL11.glTranslatef((float) x, (float) y, (float) z);
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
        float headYaw = headYawAbs - bodyYaw;
        float headPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        saiyanModel.setRotationAngles(swingPos, swingAmount, ageInTicks, headYaw, headPitch, 0.0625F, player);
        saiyanModel.render(player, swingPos, swingAmount, ageInTicks, headYaw, headPitch, 0.0625F);

        GL11.glPopMatrix();

        this.passSpecialRender(player, x, y, z);
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractClientPlayer player) {
        CharacterData data = CharacterData.get(player);
        if (data != null && data.isCreated() && data.getRace() == Race.SAIYAN) {
            return DynamicTextureManager.getInstance()
                .getOrCreateSaiyanTexture(data.getBodyColor(), data.getHairColor());
        }
        return super.getEntityTexture(player);
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
