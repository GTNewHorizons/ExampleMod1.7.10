package com.myname.mymodid;

import net.minecraftforge.common.MinecraftForge;

import com.myname.mymodid.client.KeybindHandler;
import com.myname.mymodid.client.render.PlayerRenderHandler;
import com.myname.mymodid.notification.ClientEventHandler;
import com.myname.mymodid.notification.NotificationRenderer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Notification system
        MinecraftForge.EVENT_BUS.register(new NotificationRenderer());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        // Character creation keybind
        KeybindHandler.register();
        FMLCommonHandler.instance()
            .bus()
            .register(new KeybindHandler());

        // Custom player model rendering
        MinecraftForge.EVENT_BUS.register(new PlayerRenderHandler());
    }
}
