package com.myname.mymodid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSaiyan extends ModelBase {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    // Hair parts
    public ModelRenderer hairBase;
    public ModelRenderer hairSpike1;
    public ModelRenderer hairSpike2;
    public ModelRenderer hairSpike3;
    public ModelRenderer hairBack;

    // Tail segments for animation
    public ModelRenderer tail1;
    public ModelRenderer tail2;
    public ModelRenderer tail3;

    private int hairStyleIndex = 0;

    public ModelSaiyan() {
        textureWidth = 64;
        textureHeight = 64;

        // Head
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);

        // Body (torso)
        body = new ModelRenderer(this, 16, 16);
        body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);

        // Right Arm
        rightArm = new ModelRenderer(this, 40, 16);
        rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
        rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

        // Left Arm
        leftArm = new ModelRenderer(this, 40, 16);
        leftArm.mirror = true;
        leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
        leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

        // Right Leg
        rightLeg = new ModelRenderer(this, 0, 16);
        rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        rightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

        // Left Leg
        leftLeg = new ModelRenderer(this, 0, 16);
        leftLeg.mirror = true;
        leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        leftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

        // Hair base (sits on top of head)
        hairBase = new ModelRenderer(this, 0, 32);
        hairBase.addBox(-4.5F, -8.5F, -4.5F, 9, 5, 9);
        hairBase.setRotationPoint(0.0F, 0.0F, 0.0F);

        // Hair spikes - Base style
        hairSpike1 = new ModelRenderer(this, 36, 32);
        hairSpike1.addBox(-1.0F, -12.0F, -1.0F, 2, 4, 2);
        hairSpike1.setRotationPoint(0.0F, 0.0F, 0.0F);

        hairSpike2 = new ModelRenderer(this, 36, 32);
        hairSpike2.addBox(-1.0F, -11.5F, -1.0F, 2, 4, 2);
        hairSpike2.setRotationPoint(3.0F, 0.0F, 0.0F);

        hairSpike3 = new ModelRenderer(this, 36, 32);
        hairSpike3.addBox(-1.0F, -11.5F, -1.0F, 2, 4, 2);
        hairSpike3.setRotationPoint(-3.0F, 0.0F, 0.0F);

        // Back hair
        hairBack = new ModelRenderer(this, 44, 32);
        hairBack.addBox(-3.0F, -6.0F, 4.0F, 6, 8, 2);
        hairBack.setRotationPoint(0.0F, 0.0F, 0.0F);

        // Tail segments - attached at lower back
        tail1 = new ModelRenderer(this, 0, 46);
        tail1.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5);
        tail1.setRotationPoint(0.0F, 11.0F, 2.0F);

        tail2 = new ModelRenderer(this, 0, 52);
        tail2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5);
        tail2.setRotationPoint(0.0F, 0.0F, 5.0F);
        tail1.addChild(tail2);

        tail3 = new ModelRenderer(this, 0, 58);
        tail3.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 4);
        tail3.setRotationPoint(0.0F, 0.0F, 5.0F);
        tail2.addChild(tail3);
    }

    public void setHairStyle(int styleIndex) {
        this.hairStyleIndex = styleIndex;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw,
        float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entity);

        head.render(scale);
        body.render(scale);
        rightArm.render(scale);
        leftArm.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);

        // Render hair
        renderHair(scale);

        // Render tail
        tail1.render(scale);
    }

    private void renderHair(float scale) {
        hairBase.render(scale);

        switch (hairStyleIndex) {
            case 0: // Base
                hairSpike1.render(scale);
                hairSpike2.render(scale);
                hairSpike3.render(scale);
                hairBack.render(scale);
                break;
            case 1: // Spiky (taller spikes)
                hairSpike1.render(scale);
                hairSpike2.render(scale);
                hairSpike3.render(scale);
                break;
            case 2: // Long (more back hair, less spikes)
                hairSpike1.render(scale);
                hairBack.render(scale);
                break;
            case 3: // Wild (all spikes + back)
                hairSpike1.render(scale);
                hairSpike2.render(scale);
                hairSpike3.render(scale);
                hairBack.render(scale);
                break;
            default:
                hairSpike1.render(scale);
                break;
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw,
        float headPitch, float scale, Entity entity) {
        // Head rotation
        head.rotateAngleY = headYaw / (180.0F / (float) Math.PI);
        head.rotateAngleX = headPitch / (180.0F / (float) Math.PI);

        // Hair follows head
        hairBase.rotateAngleY = head.rotateAngleY;
        hairBase.rotateAngleX = head.rotateAngleX;
        hairSpike1.rotateAngleY = head.rotateAngleY;
        hairSpike1.rotateAngleX = head.rotateAngleX;
        hairSpike2.rotateAngleY = head.rotateAngleY;
        hairSpike2.rotateAngleX = head.rotateAngleX;
        hairSpike3.rotateAngleY = head.rotateAngleY;
        hairSpike3.rotateAngleX = head.rotateAngleX;
        hairBack.rotateAngleY = head.rotateAngleY;
        hairBack.rotateAngleX = head.rotateAngleX;

        // Arm swing animation
        rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        rightArm.rotateAngleZ = 0.0F;
        leftArm.rotateAngleZ = 0.0F;

        // Leg swing animation
        rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        // Tail animation - gentle wave
        float tailBaseAngle = MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
        float walkTailSwing = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;

        tail1.rotateAngleX = -0.3F + tailBaseAngle * 0.5F;
        tail1.rotateAngleY = tailBaseAngle + walkTailSwing;

        tail2.rotateAngleX = tailBaseAngle * 0.7F;
        tail2.rotateAngleY = tailBaseAngle * 1.2F;

        tail3.rotateAngleX = tailBaseAngle * 0.9F - 0.1F;
        tail3.rotateAngleY = tailBaseAngle * 1.5F;

        // Spiky hair style has taller spikes - adjust offsets
        if (hairStyleIndex == 1) {
            hairSpike1.offsetY = -0.05F;
            hairSpike2.offsetY = -0.03F;
            hairSpike3.offsetY = -0.03F;
        } else {
            hairSpike1.offsetY = 0.0F;
            hairSpike2.offsetY = 0.0F;
            hairSpike3.offsetY = 0.0F;
        }

        // Long hair style has extended back
        if (hairStyleIndex == 2) {
            hairBack.offsetY = 0.05F;
        } else {
            hairBack.offsetY = 0.0F;
        }
    }
}
