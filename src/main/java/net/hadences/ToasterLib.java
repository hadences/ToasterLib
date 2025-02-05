package net.hadences;

import net.fabricmc.api.ModInitializer;

import net.hadences.network.ToasterPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToasterLib implements ModInitializer {

	public static final String MOD_ID = "toasterlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DebugCommand.register();
		ToasterPackets.registerPayloads();
		ToasterPackets.registerC2SPackets();
	}
}
