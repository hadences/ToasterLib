package net.hadences.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class TextSerializationUtil {

    public static String serializeText(Text text) {
        JsonElement jsonElement = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text).getOrThrow();
        return jsonElement.toString();
    }

    public static Text deserializeText(String json) {
        JsonElement jsonElement = JsonParser.parseString(json); // Convert String to JsonElement
        return TextCodecs.CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow();
    }
}
