package com.myname.mymodid.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.myname.mymodid.client.gui.GuiCharacterCreation;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeybindHandler {

    private static final KeyBinding KEY_CHARACTER_CREATION = new KeyBinding(
        "Open Character Creation",
        Keyboard.KEY_V,
        "Dragon Block X");

    public static void register() {
        ClientRegistry.registerKeyBinding(KEY_CHARACTER_CREATION);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) {
            return;
        }

        if (KEY_CHARACTER_CREATION.isPressed()) {
            mc.displayGuiScreen(new GuiCharacterCreation(false));
        }
    }
}
