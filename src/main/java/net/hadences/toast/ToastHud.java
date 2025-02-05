package net.hadences.toast;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ToastHud implements HudRenderCallback {

    private final Map<Vector2i, ToastMessage> activeToasts = new HashMap<>();

    public void addToast(ToastMessage toast){
        activeToasts.put(toast.getOffsetVector(), toast);
    }

    public void tick() {
        Iterator<Map.Entry<Vector2i, ToastMessage>> iterator = activeToasts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2i, ToastMessage> entry = iterator.next();
            ToastMessage toast = entry.getValue();

            toast.setTicksOnScreen(toast.getTicksOnScreen() - 1);
            if (toast.getTicksOnScreen() <= 0) {
                iterator.remove();
            }
        }
    }


    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        for (ToastMessage toast : activeToasts.values()) {
            toast.render(drawContext, screenWidth, screenHeight);
        }
    }
}
