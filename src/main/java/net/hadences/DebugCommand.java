package net.hadences;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hadences.network.packets.S2C.SendToastMessageS2CPacket;
import net.hadences.toast.ToastMessage;
import net.hadences.util.TextUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public class DebugCommand {

    public static final Identifier USB_IMAGE = Identifier.of(ToasterLib.MOD_ID, "toaster/usb.png");

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("debug")
                    .executes(context -> {
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        if(player == null) return 0;

                        ToastMessage message =
                                new ToastMessage(Text.literal("<< ᴛᴀꜱᴋ ᴄᴏᴍᴘʟᴇᴛᴇ >>").formatted(Formatting.BOLD).formatted(Formatting.GREEN), false, 20*5, -380, -80,
                                        ToastMessage.ToastAnimation.FADE);

                        ToastMessage message2 =
                                new ToastMessage(Text.literal(TextUtil.toSmallCaps("You have successfully extracted the data!")).formatted(Formatting.GREEN), false, 20*5, -380, -70,
                                        ToastMessage.ToastAnimation.FADE);


                        ServerPlayNetworking.send(player, new SendToastMessageS2CPacket(message));
                        ServerPlayNetworking.send(player, new SendToastMessageS2CPacket(message2));

                        return 1;
                    })
            );
        });
    }
}
