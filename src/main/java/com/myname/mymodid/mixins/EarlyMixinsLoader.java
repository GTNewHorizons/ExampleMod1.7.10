package com.myname.mymodid.mixins;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EarlyMixinsLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getMixinConfig() {
        // rename the associated .json file by replacing the "mymodid" with your own mod ID
        // in the .json file edit the "package" and "refmap" properties to match your mod
        // also edit the "refmap" property in the "mixins.mymodid.json" file
        return "mixins.mymodid.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        // Register your mixins here by adding them to the list.
        // The early mixins are mixins that target minecraft/forge classes
        // The early mixins should be placed in the "mixins/early" package
        // If your mixins targets a class from another mod, register it in the LateMixinLoader
        List<String> mixins = new ArrayList<>();

        // The parameter loadedCoreMods contains the name of coremods that are currently loaded
        // you can check this Set to decide to load certain mixins or not.
        //if (!loadedCoreMods.contains("optifine.OptiFineForgeTweaker")) {
        //    // this mixins won't be loaded if Optifine is present
        //    mixins.add("MixinClass");
        //}

        if (FMLLaunchHandler.side().isClient()) {
            // register here your mixins that should only be loaded on the client
            mixins.add("MixinMinecraft_Example");// this is an example you should delete it and the associated mixin class as well
        } else {
            // register here your mixins that should only be loaded on the dedicated server
            // mixins.add("MixinClass");
        }

        // register here your mixins that should be loaded on both sides
        // mixins.add("MixinClass");

        // If you need more complex registration logic consider switching to the IMixins registration
        // system see com.gtnewhorizon.gtnhmixins.builders.IMixins
        return mixins;
    }
}
