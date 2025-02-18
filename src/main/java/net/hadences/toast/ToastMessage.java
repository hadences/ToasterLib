package net.hadences.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public class ToastMessage {

    public enum ToastType {
        TEXT,
        IMAGE
    }

    public enum ToastAnimation{
        FADE,
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

    private Identifier imageIdentifier;
    private Vector2i imageSize;

    public ToastMessage(Text text, int ticksOnScreen, ToastType type, ToastPositionType position, ToastAnimation animationType){
        this.text = text;
        this.type = type;
        this.positionType = position;
        this.animationType = animationType;
        this.ticksOnScreen = ticksOnScreen;
    }

    public ToastMessage(Text text, boolean centeredText, int ticksOnScreen, int centerOffsetX, int centerOffsetY, ToastAnimation animationType){
        // call main constructor with custom position
        this(text, ticksOnScreen, ToastType.TEXT, centeredText ? ToastPositionType.CUSTOM_CENTERED : ToastPositionType.CUSTOM, animationType);
        this.offsetVector = new Vector2i(centerOffsetX, centerOffsetY);
    }

    public ToastMessage(Identifier imageIdentifier, Vector2i imageSize, int ticksOnScreen,
                        int centerOffsetX, int centerOffsetY, ToastAnimation animationType,
                        ToastPositionType positionType){
        this.type = ToastType.IMAGE;
        this.positionType = positionType;
        this.animationType = animationType;
        this.ticksOnScreen = ticksOnScreen;
        this.text = Text.literal("");
        this.offsetVector = new Vector2i(centerOffsetX, centerOffsetY);
        this.imageIdentifier = imageIdentifier;
        this.imageSize = imageSize;
    }

    public void render(DrawContext context, int screenWidth, int screenHeight){
        updatePosition(screenWidth, screenHeight);

        // Render the toast message
        switch (animationType) {
            case FADE -> renderFade(context);
            case NONE -> renderNone(context);
        }
    }

    private void updatePosition(int screenWidth, int screenHeight) {

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(text);

        int x = 0, y = 0;
        switch (positionType) {
            case CUSTOM -> { x = offsetVector.x; y = offsetVector.y; }
            case CUSTOM_CENTERED -> { x = (screenWidth /2) + offsetVector.x; y = (screenHeight / 2) + offsetVector.y; }
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
            alpha = 1.0f;
        } else {
            alpha = (float) totalDuration / fadeDuration;
        }

        alpha = Math.max(0.0F, Math.min(1.0F, alpha));

        int colorWithAlpha = applyAlpha(alpha);

        if(type == ToastType.IMAGE){
            RenderSystem.enableBlend();
            context.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            context.drawTexture(imageIdentifier, positionVector.x, positionVector.y, 0, 0, imageSize.x, imageSize.y, imageSize.x, imageSize.y);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            return;
        }

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

    private void renderNone(DrawContext context){
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        if(type == ToastType.IMAGE){
            RenderSystem.enableBlend();
            context.drawTexture(imageIdentifier, positionVector.x, positionVector.y, 0, 0, imageSize.x, imageSize.y, imageSize.x, imageSize.y);
            RenderSystem.disableBlend();
            return;
        }

        if (shouldRenderCenter()) {
            context.drawCenteredTextWithShadow(textRenderer, text, positionVector.x, positionVector.y, 0xFFFFFF);
        } else {
            context.drawText(textRenderer, text, positionVector.x, positionVector.y, 0xFFFFFF, true);
        }
    }

    private boolean shouldRenderCenter(){
        return positionType == ToastPositionType.TOP_CENTER
                || positionType == ToastPositionType.BOTTOM_CENTER
                || positionType == ToastPositionType.CUSTOM_CENTERED;
    }

    public Identifier getImageIdentifier() {
        return imageIdentifier;
    }

    public Vector2i getImageSize() {
        return imageSize;
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
