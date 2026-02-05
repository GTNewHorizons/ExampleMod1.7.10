package com.myname.mymodid.client.gui;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.myname.mymodid.character.BodyColor;
import com.myname.mymodid.character.CharacterData;
import com.myname.mymodid.character.HairColor;
import com.myname.mymodid.character.HairStyle;
import com.myname.mymodid.character.Race;
import com.myname.mymodid.client.model.ModelSaiyan;
import com.myname.mymodid.client.texture.SaiyanTextureGenerator;
import com.myname.mymodid.network.CharacterCreationPacket;
import com.myname.mymodid.network.PacketHandler;
import com.myname.mymodid.notification.Notification;
import com.myname.mymodid.notification.NotificationManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCharacterCreation extends GuiScreen {

    private static final int BUTTON_RACE_LEFT = 0;
    private static final int BUTTON_RACE_RIGHT = 1;
    private static final int BUTTON_HAIR_STYLE_LEFT = 2;
    private static final int BUTTON_HAIR_STYLE_RIGHT = 3;
    private static final int BUTTON_HAIR_COLOR_LEFT = 4;
    private static final int BUTTON_HAIR_COLOR_RIGHT = 5;
    private static final int BUTTON_BODY_COLOR_LEFT = 6;
    private static final int BUTTON_BODY_COLOR_RIGHT = 7;
    private static final int BUTTON_CONFIRM = 8;
    private static final int BUTTON_CANCEL = 9;

    private Race selectedRace = Race.SAIYAN;
    private HairStyle selectedHairStyle = HairStyle.BASE;
    private HairColor selectedHairColor = HairColor.BLACK;
    private BodyColor selectedBodyColor = BodyColor.LIGHT;

    private final ModelSaiyan previewModel = new ModelSaiyan();
    private ResourceLocation previewTexture;
    private float previewRotation = 0.0F;
    private boolean textureNeedsUpdate = true;

    private final boolean isEdit;

    public GuiCharacterCreation(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        buttonList.clear();

        int centerX = width / 2;
        int panelX = centerX + 40;
        int startY = 40;
        int rowHeight = 28;

        // If editing, load current data
        if (isEdit) {
            CharacterData data = CharacterData.get(Minecraft.getMinecraft().thePlayer);
            if (data != null && data.isCreated()) {
                selectedRace = data.getRace();
                selectedHairStyle = data.getHairStyle();
                selectedHairColor = data.getHairColor();
                selectedBodyColor = data.getBodyColor();
            }
        }

        // Race selector
        buttonList.add(new GuiButton(BUTTON_RACE_LEFT, panelX, startY, 20, 20, "<"));
        buttonList.add(new GuiButton(BUTTON_RACE_RIGHT, panelX + 120, startY, 20, 20, ">"));

        // Hair Style selector
        int row2 = startY + rowHeight;
        buttonList.add(new GuiButton(BUTTON_HAIR_STYLE_LEFT, panelX, row2, 20, 20, "<"));
        buttonList.add(new GuiButton(BUTTON_HAIR_STYLE_RIGHT, panelX + 120, row2, 20, 20, ">"));

        // Hair Color selector
        int row3 = startY + rowHeight * 2;
        buttonList.add(new GuiButton(BUTTON_HAIR_COLOR_LEFT, panelX, row3, 20, 20, "<"));
        buttonList.add(new GuiButton(BUTTON_HAIR_COLOR_RIGHT, panelX + 120, row3, 20, 20, ">"));

        // Body Color selector
        int row4 = startY + rowHeight * 3;
        buttonList.add(new GuiButton(BUTTON_BODY_COLOR_LEFT, panelX, row4, 20, 20, "<"));
        buttonList.add(new GuiButton(BUTTON_BODY_COLOR_RIGHT, panelX + 120, row4, 20, 20, ">"));

        // Confirm / Cancel buttons
        int bottomY = startY + rowHeight * 5;
        buttonList.add(new GuiButton(BUTTON_CONFIRM, panelX, bottomY, 60, 20, "Confirm"));
        buttonList.add(new GuiButton(BUTTON_CANCEL, panelX + 70, bottomY, 60, 20, "Cancel"));

        // Only Saiyan is available, disable race buttons for now
        ((GuiButton) buttonList.get(0)).enabled = false; // left race
        ((GuiButton) buttonList.get(1)).enabled = false; // right race

        textureNeedsUpdate = true;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BUTTON_RACE_LEFT:
            case BUTTON_RACE_RIGHT:
                // Only Saiyan available for now
                break;

            case BUTTON_HAIR_STYLE_LEFT:
                selectedHairStyle = HairStyle
                    .fromOrdinal((selectedHairStyle.ordinal() - 1 + HairStyle.values().length) % HairStyle.values().length);
                textureNeedsUpdate = true;
                break;
            case BUTTON_HAIR_STYLE_RIGHT:
                selectedHairStyle = HairStyle.fromOrdinal((selectedHairStyle.ordinal() + 1) % HairStyle.values().length);
                textureNeedsUpdate = true;
                break;

            case BUTTON_HAIR_COLOR_LEFT:
                selectedHairColor = HairColor
                    .fromOrdinal((selectedHairColor.ordinal() - 1 + HairColor.values().length) % HairColor.values().length);
                textureNeedsUpdate = true;
                break;
            case BUTTON_HAIR_COLOR_RIGHT:
                selectedHairColor = HairColor.fromOrdinal((selectedHairColor.ordinal() + 1) % HairColor.values().length);
                textureNeedsUpdate = true;
                break;

            case BUTTON_BODY_COLOR_LEFT:
                selectedBodyColor = BodyColor
                    .fromOrdinal((selectedBodyColor.ordinal() - 1 + BodyColor.values().length) % BodyColor.values().length);
                textureNeedsUpdate = true;
                break;
            case BUTTON_BODY_COLOR_RIGHT:
                selectedBodyColor = BodyColor.fromOrdinal((selectedBodyColor.ordinal() + 1) % BodyColor.values().length);
                textureNeedsUpdate = true;
                break;

            case BUTTON_CONFIRM:
                confirmCharacter();
                break;
            case BUTTON_CANCEL:
                mc.displayGuiScreen(null);
                break;
        }
    }

    private void confirmCharacter() {
        // Send character creation packet to server
        PacketHandler.INSTANCE
            .sendToServer(new CharacterCreationPacket(selectedRace, selectedHairStyle, selectedHairColor, selectedBodyColor));

        // Update local client data immediately for responsiveness
        CharacterData data = CharacterData.get(mc.thePlayer);
        if (data != null) {
            data.setRace(selectedRace);
            data.setHairStyle(selectedHairStyle);
            data.setHairColor(selectedHairColor);
            data.setBodyColor(selectedBodyColor);
            data.setCreated(true);
        }

        NotificationManager.getInstance()
            .enqueue(new Notification("Character Created", selectedRace.getDisplayName() + " warrior ready!"));

        mc.displayGuiScreen(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // Title
        String title = isEdit ? "Edit Character" : "Character Creation";
        drawCenteredString(fontRendererObj, title, width / 2, 10, 0xFFFF00);
        drawCenteredString(fontRendererObj, "Dragon Block X", width / 2, 22, 0xFF6A00);

        int centerX = width / 2;
        int panelX = centerX + 40;
        int startY = 40;
        int rowHeight = 28;

        // Draw labels and values
        drawString(fontRendererObj, "Race:", panelX + 24, startY + 6, 0xFFFFFF);
        drawCenteredString(fontRendererObj, selectedRace.getDisplayName(), panelX + 70, startY + 6, 0x00FF00);

        drawString(fontRendererObj, "Hair:", panelX + 24, startY + rowHeight + 6, 0xFFFFFF);
        drawCenteredString(
            fontRendererObj,
            selectedHairStyle.getDisplayName(),
            panelX + 70,
            startY + rowHeight + 6,
            0x00FF00);

        drawString(fontRendererObj, "Color:", panelX + 22, startY + rowHeight * 2 + 6, 0xFFFFFF);
        drawCenteredString(
            fontRendererObj,
            selectedHairColor.getDisplayName(),
            panelX + 70,
            startY + rowHeight * 2 + 6,
            0x00FF00);

        drawString(fontRendererObj, "Skin:", panelX + 24, startY + rowHeight * 3 + 6, 0xFFFFFF);
        drawCenteredString(
            fontRendererObj,
            selectedBodyColor.getDisplayName(),
            panelX + 70,
            startY + rowHeight * 3 + 6,
            0x00FF00);

        // Draw 3D model preview on the left side
        drawModelPreview(centerX - 60, startY + 30, partialTicks);

        // Draw color preview swatches
        drawColorSwatch(panelX + 80, startY + rowHeight * 2 + 3, selectedHairColor.toPackedRGB());
        drawColorSwatch(panelX + 80, startY + rowHeight * 3 + 3, selectedBodyColor.toPackedRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawModelPreview(int centerX, int centerY, float partialTicks) {
        if (textureNeedsUpdate) {
            updatePreviewTexture();
            textureNeedsUpdate = false;
        }

        previewRotation += 0.5F;
        if (previewRotation >= 360.0F) {
            previewRotation -= 360.0F;
        }

        previewModel.setHairStyle(selectedHairStyle.ordinal());

        // Draw a dark background panel for the preview
        drawRect(centerX - 50, centerY - 40, centerX + 50, centerY + 90, 0x88000000);
        drawRect(centerX - 51, centerY - 41, centerX + 51, centerY - 40, 0xFFFF6A00);
        drawRect(centerX - 51, centerY + 90, centerX + 51, centerY + 91, 0xFFFF6A00);
        drawRect(centerX - 51, centerY - 40, centerX - 50, centerY + 90, 0xFFFF6A00);
        drawRect(centerX + 50, centerY - 40, centerX + 51, centerY + 90, 0xFFFF6A00);

        GL11.glPushMatrix();

        GL11.glTranslatef(centerX, centerY + 65, 100.0F);
        float scale = 30.0F;
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(previewRotation, 0.0F, 1.0F, 0.0F);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        if (previewTexture != null) {
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(previewTexture);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float ageInTicks = Minecraft.getMinecraft().thePlayer.ticksExisted + partialTicks;
        previewModel.setRotationAngles(0.0F, 0.0F, ageInTicks, 0.0F, 0.0F, 0.0625F, null);
        previewModel.render(null, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F, 0.0625F);

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();
    }

    private void updatePreviewTexture() {
        BufferedImage image = SaiyanTextureGenerator.generate(selectedBodyColor, selectedHairColor);
        DynamicTexture dynamicTexture = new DynamicTexture(image);
        previewTexture = Minecraft.getMinecraft()
            .getTextureManager()
            .getDynamicTextureLocation("dbx_preview", dynamicTexture);
    }

    private void drawColorSwatch(int x, int y, int color) {
        int argb = 0xFF000000 | color;
        drawRect(x, y, x + 14, y + 14, argb);
        // Border
        drawRect(x - 1, y - 1, x + 15, y, 0xFF888888);
        drawRect(x - 1, y + 14, x + 15, y + 15, 0xFF888888);
        drawRect(x - 1, y, x, y + 14, 0xFF888888);
        drawRect(x + 14, y, x + 15, y + 14, 0xFF888888);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
