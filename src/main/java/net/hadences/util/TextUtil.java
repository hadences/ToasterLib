package net.hadences.util;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {
    private static final Map<Character, Character> SMALL_CAPS_MAP = new HashMap<>();

    static {
        String normal = "abcdefghijklmnopqrstuvwxyz";
        String smallCaps = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";

        for (int i = 0; i < normal.length(); i++) {
            SMALL_CAPS_MAP.put(normal.charAt(i), smallCaps.charAt(i));
        }
    }

    public static String toSmallCaps(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append(SMALL_CAPS_MAP.getOrDefault(c, c));
        }
        return result.toString();
    }
}
