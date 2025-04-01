package com.myname.mymodid.mixins.early;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.myname.mymodid.MyMod;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "tick", at = @At("HEAD"))
    private void mymodid$tick(CallbackInfo ci) {
        MyMod.LOG.info("Hello From Tick Mixin!");
    }

}
