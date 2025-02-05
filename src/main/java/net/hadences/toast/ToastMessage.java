package net.hadences.toast;

import net.minecraft.client.gui.DrawContext;

public class ToastMessage {

    public enum ToastType{
        FADE,
        SLIDE,
        POPUP,
        NONE
    }

    public enum ToastPosition{
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    private int ticksOnScreen;


}
