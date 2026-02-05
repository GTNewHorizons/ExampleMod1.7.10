package com.myname.mymodid.character;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.myname.mymodid.network.CharacterSyncPacket;
import com.myname.mymodid.network.PacketHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CharacterEventHandler {

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            CharacterData.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        // Preserve character data across death/dimension change
        CharacterData oldData = CharacterData.get(event.original);
        CharacterData newData = CharacterData.get(event.entityPlayer);
        if (oldData != null && newData != null) {
            newData.copyFrom(oldData);
        }
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntityPlayer)) {
            return;
        }

        // Server side: sync this player's data to all nearby players
        if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            CharacterData data = CharacterData.get(player);
            if (data != null && data.isCreated()) {
                CharacterSyncPacket syncPacket = new CharacterSyncPacket(
                    player.getEntityId(),
                    data.getRace(),
                    data.getHairStyle(),
                    data.getHairColor(),
                    data.getBodyColor(),
                    true);

                PacketHandler.INSTANCE.sendToAllAround(
                    syncPacket,
                    new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 256));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        // When a player starts tracking another player, send the tracked player's character data
        if (event.target instanceof EntityPlayer && event.entityPlayer instanceof EntityPlayerMP) {
            EntityPlayer trackedPlayer = (EntityPlayer) event.target;
            CharacterData data = CharacterData.get(trackedPlayer);
            if (data != null && data.isCreated()) {
                CharacterSyncPacket syncPacket = new CharacterSyncPacket(
                    trackedPlayer.getEntityId(),
                    data.getRace(),
                    data.getHairStyle(),
                    data.getHairColor(),
                    data.getBodyColor(),
                    true);
                PacketHandler.INSTANCE.sendTo(syncPacket, (EntityPlayerMP) event.entityPlayer);
            }
        }
    }
}
