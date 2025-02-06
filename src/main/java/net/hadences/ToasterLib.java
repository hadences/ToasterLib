package net.hadences;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hadences.network.ToasterPackets;
import net.hadences.network.packets.S2C.SendToastMessageS2CPacket;
import net.hadences.toast.ToastMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterLib implements ModInitializer {

	public static final String MOD_ID = "toasterlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
//		DebugCommand.register();
		ToasterPackets.registerPayloads();
		ToasterPackets.registerC2SPackets();
	}

	/**
	 * Helper function to send a toast message to a player
	 * @param player The player to send the toast to
	 * @param message The message to send
	 */
	public static void sendToast(ServerPlayerEntity player, ToastMessage message){
		ServerPlayNetworking.send(player, new SendToastMessageS2CPacket(message));
	}
}
