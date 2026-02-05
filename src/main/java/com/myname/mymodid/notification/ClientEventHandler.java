package com.myname.mymodid.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }
        if (!event.world.isRemote) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player != Minecraft.getMinecraft().thePlayer) {
            return;
        }

        NotificationManager.getInstance()
            .enqueue(new Notification("Dragon Block X", "Create your character with V!"));
    }
}
