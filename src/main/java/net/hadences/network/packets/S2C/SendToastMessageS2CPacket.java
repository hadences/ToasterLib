package net.hadences.network.packets.S2C;

import net.hadences.ToasterLib;
import net.hadences.toast.ToastMessage;
import net.hadences.util.TextSerializationUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record SendToastMessageS2CPacket(ToastMessage message) implements CustomPayload {
    public static final Id<SendToastMessageS2CPacket> PACKET_ID =
            new Id<>(Identifier.of(ToasterLib.MOD_ID, "send_toast_message"));
    public static final PacketCodec<RegistryByteBuf, SendToastMessageS2CPacket> PACKET_CODEC =
            PacketCodec.of(
                    (packet, buffer) -> {
                        // typoe, position, animation, ticksOnScreen, message, ticksOnScreen
                        buffer.writeInt(packet.message.getType().ordinal());
                        buffer.writeInt(packet.message.getPositionType().ordinal());
                        buffer.writeInt(packet.message.getAnimationType().ordinal());
                        buffer.writeInt(packet.message.getTicksOnScreen());
                        buffer.writeString(TextSerializationUtil.serializeText(packet.message.getText()));

                        if(packet.message.getPositionType() == ToastMessage.ToastPositionType.CUSTOM ||
                                packet.message.getPositionType() == ToastMessage.ToastPositionType.CUSTOM_CENTERED){
                            buffer.writeInt(packet.message.getOffsetVector().x);
                            buffer.writeInt(packet.message.getOffsetVector().y);
                        }
                    },
                    buf  -> {
                        ToastMessage.ToastType type = ToastMessage.ToastType.values()[buf.readInt()];
                        ToastMessage.ToastPositionType position = ToastMessage.ToastPositionType.values()[buf.readInt()];
                        ToastMessage.ToastAnimation animation = ToastMessage.ToastAnimation.values()[buf.readInt()];
                        int ticksOnScreen = buf.readInt();
                        Text message = TextSerializationUtil.deserializeText(buf.readString());

                        if(position == ToastMessage.ToastPositionType.CUSTOM ||
                                position == ToastMessage.ToastPositionType.CUSTOM_CENTERED){
                            int x = buf.readInt();
                            int y = buf.readInt();
                            boolean centered = position == ToastMessage.ToastPositionType.CUSTOM_CENTERED;
                            return new SendToastMessageS2CPacket(new ToastMessage(message, centered, ticksOnScreen, x, y, type, animation));
                        }

                        ToastMessage toast = new ToastMessage(message, ticksOnScreen, type, position, animation);

                        return new SendToastMessageS2CPacket(toast);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
