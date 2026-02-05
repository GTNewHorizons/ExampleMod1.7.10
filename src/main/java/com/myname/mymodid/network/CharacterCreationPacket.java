package com.myname.mymodid.network;

import net.minecraft.entity.player.EntityPlayerMP;

import com.myname.mymodid.MyMod;
import com.myname.mymodid.character.BodyColor;
import com.myname.mymodid.character.CharacterData;
import com.myname.mymodid.character.HairColor;
import com.myname.mymodid.character.HairStyle;
import com.myname.mymodid.character.Race;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class CharacterCreationPacket implements IMessage {

    private int race;
    private int hairStyle;
    private int hairColor;
    private int bodyColor;

    public CharacterCreationPacket() {}

    public CharacterCreationPacket(Race race, HairStyle hairStyle, HairColor hairColor, BodyColor bodyColor) {
        this.race = race.ordinal();
        this.hairStyle = hairStyle.ordinal();
        this.hairColor = hairColor.ordinal();
        this.bodyColor = bodyColor.ordinal();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        race = buf.readInt();
        hairStyle = buf.readInt();
        hairColor = buf.readInt();
        bodyColor = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(race);
        buf.writeInt(hairStyle);
        buf.writeInt(hairColor);
        buf.writeInt(bodyColor);
    }

    public static class Handler implements IMessageHandler<CharacterCreationPacket, IMessage> {

        @Override
        public IMessage onMessage(CharacterCreationPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            CharacterData data = CharacterData.get(player);

            if (data == null) {
                MyMod.LOG.warn("CharacterData was null for player " + player.getCommandSenderName());
                return null;
            }

            data.setRace(Race.fromOrdinal(message.race));
            data.setHairStyle(HairStyle.fromOrdinal(message.hairStyle));
            data.setHairColor(HairColor.fromOrdinal(message.hairColor));
            data.setBodyColor(BodyColor.fromOrdinal(message.bodyColor));
            data.setCreated(true);

            MyMod.LOG.info(
                "Character created for " + player.getCommandSenderName() + ": " + data.getRace()
                    .getDisplayName());

            // Sync to all nearby players
            CharacterSyncPacket syncPacket = new CharacterSyncPacket(
                player.getEntityId(),
                data.getRace(),
                data.getHairStyle(),
                data.getHairColor(),
                data.getBodyColor(),
                true);

            PacketHandler.INSTANCE
                .sendToAllAround(syncPacket, new cpw.mods.fml.common.network.NetworkRegistry.TargetPoint(
                    player.dimension,
                    player.posX,
                    player.posY,
                    player.posZ,
                    256));

            return null;
        }
    }
}
