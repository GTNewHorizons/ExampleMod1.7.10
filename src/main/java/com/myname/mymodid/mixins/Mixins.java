package com.myname.mymodid.mixins;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import javax.annotation.Nonnull;

public enum Mixins implements IMixins {

    // Read the java doc of IMixins and MixinBuilder for further information

    // You should declare all of your mixins early and late in this same enum

    EXAMPLE(new MixinBuilder()
        .setPhase(Phase.EARLY)
        .addClientMixins("MixinMinecraft_Example"));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
