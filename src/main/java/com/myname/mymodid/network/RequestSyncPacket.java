package com.myname.mymodid.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.myname.mymodid.character.CharacterData;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class RequestSyncPacket implements IMessage {

    public RequestSyncPacket() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<RequestSyncPacket, IMessage> {

        @Override
        public IMessage onMessage(RequestSyncPacket message, MessageContext ctx) {
            EntityPlayerMP requester = ctx.getServerHandler().playerEntity;

            // Send character data for all online players to the requester
            for (Object obj : MinecraftServer.getServer()
                .getConfigurationManager().playerEntityList) {
                if (obj instanceof EntityPlayer) {
                    EntityPlayer otherPlayer = (EntityPlayer) obj;
                    CharacterData data = CharacterData.get(otherPlayer);
                    if (data != null && data.isCreated()) {
                        CharacterSyncPacket syncPacket = new CharacterSyncPacket(
                            otherPlayer.getEntityId(),
                            data.getRace(),
                            data.getHairStyle(),
                            data.getHairColor(),
                            data.getBodyColor(),
                            true);
                        PacketHandler.INSTANCE.sendTo(syncPacket, requester);
                    }
                }
            }

            return null;
        }
    }
}
