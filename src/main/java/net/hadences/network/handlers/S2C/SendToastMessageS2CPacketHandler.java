package net.hadences.network.handlers.S2C;

import net.hadences.ToasterLibClient;
import net.hadences.network.packets.S2C.SendToastMessageS2CPacket;
import net.hadences.toast.ToastMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class SendToastMessageS2CPacketHandler {
    public static void receive(SendToastMessageS2CPacket packet, ClientPlayerEntity player, MinecraftClient client){
        if(player == null) return; // check if player exist

        ToastMessage message = packet.message();
        client.execute(() -> {
            ToasterLibClient.TOAST_HUD.addToast(message);
        });

    }
}
