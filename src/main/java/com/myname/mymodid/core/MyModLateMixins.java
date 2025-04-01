package com.myname.mymodid.core;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.mixin.IMixins;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.myname.mymodid.mixins.Mixins;

@LateMixin
public class MyModLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.mymodid.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return IMixins.getLateMixins(Mixins.class, loadedMods);
    }
}
