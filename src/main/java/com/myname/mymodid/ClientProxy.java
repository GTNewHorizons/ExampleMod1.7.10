package com.myname.mymodid;

import net.minecraftforge.common.MinecraftForge;

import com.myname.mymodid.notification.ClientEventHandler;
import com.myname.mymodid.notification.NotificationRenderer;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new NotificationRenderer());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
