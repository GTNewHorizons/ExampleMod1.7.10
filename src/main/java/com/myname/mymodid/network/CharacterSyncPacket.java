package com.myname.mymodid.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.myname.mymodid.character.BodyColor;
import com.myname.mymodid.character.CharacterData;
import com.myname.mymodid.character.HairColor;
import com.myname.mymodid.character.HairStyle;
import com.myname.mymodid.character.Race;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class CharacterSyncPacket implements IMessage {

    private int entityId;
    private int race;
    private int hairStyle;
    private int hairColor;
    private int bodyColor;
    private boolean created;

    public CharacterSyncPacket() {}

    public CharacterSyncPacket(int entityId, Race race, HairStyle hairStyle, HairColor hairColor, BodyColor bodyColor,
        boolean created) {
        this.entityId = entityId;
        this.race = race.ordinal();
        this.hairStyle = hairStyle.ordinal();
        this.hairColor = hairColor.ordinal();
        this.bodyColor = bodyColor.ordinal();
        this.created = created;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        race = buf.readInt();
        hairStyle = buf.readInt();
        hairColor = buf.readInt();
        bodyColor = buf.readInt();
        created = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(race);
        buf.writeInt(hairStyle);
        buf.writeInt(hairColor);
        buf.writeInt(bodyColor);
        buf.writeBoolean(created);
    }

    public static class Handler implements IMessageHandler<CharacterSyncPacket, IMessage> {

        @Override
        public IMessage onMessage(CharacterSyncPacket message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.theWorld == null) {
                return null;
            }

            Entity entity = mc.theWorld.getEntityByID(message.entityId);
            if (!(entity instanceof EntityPlayer)) {
                return null;
            }

            EntityPlayer player = (EntityPlayer) entity;
            CharacterData data = CharacterData.get(player);
            if (data == null) {
                CharacterData.register(player);
                data = CharacterData.get(player);
            }
            if (data == null) {
                return null;
            }

            data.setRace(Race.fromOrdinal(message.race));
            data.setHairStyle(HairStyle.fromOrdinal(message.hairStyle));
            data.setHairColor(HairColor.fromOrdinal(message.hairColor));
            data.setBodyColor(BodyColor.fromOrdinal(message.bodyColor));
            data.setCreated(message.created);

            return null;
        }
    }
}
