package com.rhghunter.rhgmodpackextra;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = RHGModpackExtra.MODID, version = Tags.version, name = "RHG Modpack Extra", acceptedMinecraftVersions = "[1.7.10]")
public class RHGModpackExtra {

    public static final String MODID = "rhgmodpackextra";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.rhghunter.rhgmodpackextra.ClientProxy", serverSide = "com.rhghunter.rhgmodpackextra.CommonProxy")
    public static CommonProxy proxy;

    public static ItemAutoFishingRod autoRod;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        // Item Registery
        autoRod = new ItemAutoFishingRod();
        GameRegistry.registerItem(autoRod, "auto_fishing_rod");

        // Register Evemt Handler
        FMLCommonHandler.instance().bus().register(new AutoFishingEventHandler());
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
