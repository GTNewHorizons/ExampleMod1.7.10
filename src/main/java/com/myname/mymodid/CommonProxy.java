package com.myname.mymodid;

import net.minecraftforge.common.MinecraftForge;

import com.myname.mymodid.character.CharacterEventHandler;
import com.myname.mymodid.network.PacketHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        MyMod.LOG.info(Config.greeting);
        MyMod.LOG.info("I am MyMod at version " + Tags.VERSION);

        PacketHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        CharacterEventHandler characterEventHandler = new CharacterEventHandler();
        MinecraftForge.EVENT_BUS.register(characterEventHandler);
        FMLCommonHandler.instance()
            .bus()
            .register(characterEventHandler);
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}
}
