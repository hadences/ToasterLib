package net.hadences.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.hadences.network.handlers.S2C.SendToastMessageS2CPacketHandler;
import net.hadences.network.packets.S2C.SendToastMessageS2CPacket;

public class ToasterPackets {

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(SendToastMessageS2CPacket.PACKET_ID, SendToastMessageS2CPacket.PACKET_CODEC);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(
                SendToastMessageS2CPacket.PACKET_ID,
                (payload, context) -> SendToastMessageS2CPacketHandler.receive(payload, context.player(), context.client())
        );
    }

    public static void registerC2SPackets() {

    }

}
