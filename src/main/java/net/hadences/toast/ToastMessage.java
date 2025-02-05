package net.hadences.toast;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.joml.Vector2i;

public class ToastMessage {

    public enum ToastType {
        TEXT,
        IMAGE
    }

    public enum ToastAnimation{
        FADE,
        SLIDE,
        POPUP,
        NONE
    }

    public enum ToastPositionType {
        CUSTOM,
        CUSTOM_CENTERED,
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    private int ticksOnScreen;
    private final ToastType type;
    private final ToastPositionType positionType;
    private final ToastAnimation animationType;
    private final Text text;
    private Vector2i positionVector;
    private Vector2i offsetVector;

    public ToastMessage(Text text, int ticksOnScreen, ToastType type, ToastPositionType position, ToastAnimation animationType){
        this.text = text;
        this.type = type;
        this.positionType = position;
        this.animationType = animationType;
        this.ticksOnScreen = ticksOnScreen;
    }

    public ToastMessage(Text text, boolean centeredText, int ticksOnScreen, int centerOffsetX, int centerOffsetY, ToastType type, ToastAnimation animationType){
        // call main constructor with custom position
        this(text, ticksOnScreen, type, centeredText ? ToastPositionType.CUSTOM_CENTERED : ToastPositionType.CUSTOM, animationType);
        this.offsetVector = new Vector2i(centerOffsetX, centerOffsetY);

    }

    public void render(DrawContext context, int screenWidth, int screenHeight){
        updatePosition(screenWidth, screenHeight);

        // Render the toast message
        switch (animationType) {
            case FADE -> renderFade(context);
            case SLIDE -> renderSlide(context);
            case POPUP -> renderPopup(context);
            case NONE -> renderNone(context);
        }
    }

    private void updatePosition(int screenWidth, int screenHeight) {

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(text);

        int x = 0, y = 0;
        switch (positionType) {
            case CUSTOM, CUSTOM_CENTERED -> { x = (screenWidth /2) + offsetVector.x; y = (screenHeight / 2) + offsetVector.y; }
            case TOP_LEFT -> { x = 10; y = 10; }
            case TOP_CENTER -> { x = (screenWidth / 2); y = 10; }
            case TOP_RIGHT -> { x = screenWidth - textWidth - 8; y = 10; }
            case BOTTOM_LEFT -> { x = 10; y = screenHeight - 30; }
            case BOTTOM_CENTER -> { x = (screenWidth / 2); y = screenHeight - 30; }
            case BOTTOM_RIGHT -> { x = screenWidth - textWidth - 8; y = screenHeight - 30; }
        }

        this.positionVector = new Vector2i(x, y);
    }

    private void renderFade(DrawContext context) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int fadeDuration = 5;
        int totalDuration = ticksOnScreen;

        float alpha;
        if (totalDuration > fadeDuration) {
            alpha = 1.0F - ((float) (totalDuration - ticksOnScreen - fadeDuration) / fadeDuration);
        } else {
            alpha = (float) totalDuration / fadeDuration;
        }

        alpha = Math.max(0.0F, Math.min(1.0F, alpha));

        int colorWithAlpha = applyAlpha(alpha);

        if (shouldRenderCenter()) {
            context.drawCenteredTextWithShadow(textRenderer, text, positionVector.x, positionVector.y, colorWithAlpha);
        } else {
            context.drawText(textRenderer, text, positionVector.x, positionVector.y, colorWithAlpha, true);
        }
    }

    // Utility function to apply alpha to color
    private int applyAlpha(float alpha) {
        int a = (int) (alpha * 255) << 24;
        return (0xFFFFFF) | a;
    }

    private void renderSlide(DrawContext context){
        // Render the toast message with a slide effect
    }

    private void renderPopup(DrawContext context){
        // Render the toast message with a popup effect
    }

    private void renderNone(DrawContext context){
        // Render the toast message with no effect
    }

    private boolean shouldRenderCenter(){
        return positionType == ToastPositionType.TOP_CENTER
                || positionType == ToastPositionType.BOTTOM_CENTER
                || positionType == ToastPositionType.CUSTOM_CENTERED;
    }

    public Vector2i getOffsetVector() {
        return offsetVector;
    }

    public Vector2i getPositionVector() {
        return positionVector;
    }

    public ToastType getType() {
        return type;
    }

    public ToastPositionType getPositionType() {
        return positionType;
    }

    public ToastAnimation getAnimationType() {
        return animationType;
    }

    public Text getText() {
        return text;
    }

    public int getTicksOnScreen() {
        return ticksOnScreen;
    }

    public void setTicksOnScreen(int ticksOnScreen) {
        this.ticksOnScreen = ticksOnScreen;
    }

    public boolean isExpired(){
        return ticksOnScreen <= 0;
    }

}
