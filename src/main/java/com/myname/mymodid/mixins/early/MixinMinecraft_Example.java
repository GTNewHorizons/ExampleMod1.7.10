package com.myname.mymodid.mixins.early;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class) // the class targeted by this mixin
public class MixinMinecraft_Example { // This is an example you should delete this class

    @Inject(method = "startGame", at = @At("TAIL"))
    private void example$sayHello(CallbackInfo ci) {
        // this line of code will be injected at the end of the method "startGame" in the Minecraft class
        System.out.println("Example mod says Hello from within Minecraft.startGame()!");
    }
}
