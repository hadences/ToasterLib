package net.hadences;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hadences.network.ToasterPackets;
import net.hadences.toast.ToastHud;

public class ToasterLibClient implements ClientModInitializer {
    public static final ToastHud TOAST_HUD = new ToastHud();

    @Override
    public void onInitializeClient() {
        ToasterPackets.registerS2CPackets();
        ClientTickEvents.END_WORLD_TICK.register(world -> TOAST_HUD.tick());
        HudRenderCallback.EVENT.register(TOAST_HUD);
    }


}
