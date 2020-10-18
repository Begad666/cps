package begad.mc.bc.plugin.cps.utils;

import begad.mc.placeholders.Placeholder;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {
    private static Map<String, Placeholder> placeholderMap = new HashMap<>();

    public static void addPlaceholder(Placeholder placeholder) {
        if (!placeholderMap.containsKey(placeholder.name)) {
            placeholderMap.put(placeholder.name, placeholder);
        }
    }

    public static Placeholder getPlaceholder(String name) {
        return placeholderMap.get(name);
    }

    public static String replaceAll(String input) {
        String output = input;
        for (Placeholder placeholder : placeholderMap.values()) {
            output = placeholder.replace(output);
        }
        return output;
    }
}
