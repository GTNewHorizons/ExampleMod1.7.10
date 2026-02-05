package com.myname.mymodid.network;

import com.myname.mymodid.MyMod;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MyMod.MODID);

    private static int packetId = 0;

    public static void init() {
        INSTANCE.registerMessage(
            CharacterCreationPacket.Handler.class,
            CharacterCreationPacket.class,
            packetId++,
            Side.SERVER);

        INSTANCE.registerMessage(CharacterSyncPacket.Handler.class, CharacterSyncPacket.class, packetId++, Side.CLIENT);

        INSTANCE.registerMessage(RequestSyncPacket.Handler.class, RequestSyncPacket.class, packetId++, Side.SERVER);
    }
}
