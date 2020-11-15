package begad.mc.bc.plugin.cps.utils;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.placeholders.Placeholder;
import begad.mc.placeholders.PlaceholderGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public Utils() {
    }

    public static final UUID CONSOLE_UUID = new UUID(0, 0);
    public static final Supplier<String> CONSOLE = () -> getMessage("", "the Console", "", "vanish.log-console", false);
    public static final Supplier<String> HIMSELF = () -> getMessage("", "himself", "", "vanish.log-himself", false);

    static {
        Placeholder max = new Placeholder("MaxPlayers", "%max%", (String input, Matcher matcher) -> input.replaceFirst("%max%", Integer.toString(Core.getConfig().get().getInt("network-info.max-players") > 0 ? Core.getConfig().get().getInt("network-info.max-players") : Core.redisBungeeIntegration.getPlayerCount() + 1)), 0);
        Placeholder online = new Placeholder("OnlinePlayers", "%online%", (String input, Matcher matcher) -> input.replaceFirst("%online%", Integer.toString(Core.redisBungeeIntegration.getPlayerCount())), 0);
        Placeholder netName = new Placeholder("NetworkName", "%net-name%", (String input, Matcher matcher) -> input.replaceFirst("%net-name%", Core.getConfig().get().getString("network-info.name")), 0);

        PlaceholderGroup placeholderGroup = new PlaceholderGroup("Bungee", "%bungee_([1-Z]+):([A-Z]+)%", Pattern.CASE_INSENSITIVE);
        placeholderGroup.addPlaceholder(new Placeholder("Players", "%bungee_(.*):players%", (String input, Matcher matcher) -> {
            String output = input;
            if (matcher.find()) {
                String server_name = matcher.group(2);
                try {
                    if (Core.redisBungeeIntegration.isDetected()) {
                        Core.redisBungeeIntegration.getApi().getPlayersOnServer(server_name);
                    } else {
                        ProxyServer.getInstance().getServerInfo(server_name);
                    }
                } catch (IllegalArgumentException | NullPointerException exception) {
                    output = output.replaceFirst("%bungee_" + server_name + ":players%", "&4Not Found");
                    return output;
                }
                int size = Core.redisBungeeIntegration.isDetected() ? Core.redisBungeeIntegration.getApi().getPlayersOnServer(server_name).size() : ProxyServer.getInstance().getServerInfo(server_name).getPlayers().size();
                output = output.replaceFirst("%bungee_" + server_name + ":players%", Integer.toString(size));
            }
            return output;
        }, 0));
        Placeholders.addPlaceholder(max);
        Placeholders.addPlaceholder(online);
        Placeholders.addPlaceholder(netName);
        Placeholders.addPlaceholder(placeholderGroup);
    }

    public static String replaceUnicodeCharacters(String string) {
        String newstring = "";
        String old = string;
        Collection<String> replace = Core.getConfig().get().getSection("replace").getKeys();

        for (Iterator<String> stringIterator = replace.iterator(); stringIterator.hasNext(); newstring = old) {
            String rk = stringIterator.next();
            old = old.replace(rk, Core.getConfig().get().getSection("replace").getString(rk));
        }

        return newstring;
    }

    public static String replaceColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String replacePlaceHolders(String string) {
        return Placeholders.replaceAll(string);
    }

    public static String replaceEveryThing(String string) {
        String stringnew = replacePlaceHolders(string);
        stringnew = replaceColorCodes(stringnew);
        return stringnew;
    }

    public static PlayerInfo[] getHoverMessage() {
        PlayerInfo[] hovermessage;
        if (Core.getConfig().get().getBoolean("hover-messages.show-players")) {
            ArrayList<String> list = Core.redisBungeeIntegration.getPlayerNames().stream().filter((s) -> !Core.vanishManager.isVanished(Core.redisBungeeIntegration.getPlayerUUID(s))).collect(Collectors.toCollection(ArrayList::new));
            if (list.size() > Core.getConfig().get().getInt("hover-messages.show-players-limit")) {
                hovermessage = new PlayerInfo[Core.getConfig().get().getInt("hover-messages.show-players-limit")];
            } else {
                hovermessage = new PlayerInfo[list.size()];
            }

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(list.get(i), Core.redisBungeeIntegration.isDetected() ? Core.redisBungeeIntegration.getApi().getUuidFromName(list.get(i), false) : ProxyServer.getInstance().getPlayer(list.get((i))).getUniqueId());
            }

        } else {
            ArrayList<String> list = (ArrayList<String>) Core.getConfig().get().getStringList("hover-messages.default-hover-message");
            hovermessage = new PlayerInfo[list.size()];

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(replaceEveryThing(replacePlaceHolders(list.get(i))), UUID.fromString("0-0-0-0-0"));
            }

        }
        return hovermessage;
    }

    public static PlayerInfo[] getMaintenanceHoverMessage() {
        PlayerInfo[] hovermessage;
        if (Core.getConfig().get().getBoolean("hover-messages.show-players")) {
            ArrayList<String> list = Core.redisBungeeIntegration.getPlayerNames().stream().filter((s) -> !Core.vanishManager.isVanished(Core.redisBungeeIntegration.getPlayerUUID(s))).collect(Collectors.toCollection(ArrayList::new));
            if (list.size() > Core.getConfig().get().getInt("hover-messages.show-players-limit")) {
                hovermessage = new PlayerInfo[Core.getConfig().get().getInt("hover-messages.show-players-limit")];
            } else {
                hovermessage = new PlayerInfo[list.size()];
            }

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(list.get(i), Core.redisBungeeIntegration.isDetected() ? Core.redisBungeeIntegration.getApi().getUuidFromName(list.get(i), false) : ProxyServer.getInstance().getPlayer(list.get((i))).getUniqueId());
            }

        } else {
            ArrayList<String> list = (ArrayList<String>) Core.getConfig().get().getStringList("hover-messages.maintenance-hover-message");
            hovermessage = new PlayerInfo[list.size()];

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(replaceEveryThing(replacePlaceHolders(list.get(i))), UUID.fromString("0-0-0-0-0"));
            }

        }
        return hovermessage;
    }

    public static int getVersion(int playerversion) {
        if (Core.getConfig().get().getBoolean("settings.allow-all-versions")) {
            return playerversion;
        }
        ArrayList<Integer> list = (ArrayList<Integer>) Core.getConfig().get().getIntList("settings.allowed-protocols");
        int returnedversion;
        if (list.contains(playerversion)) {
            returnedversion = playerversion;
        } else {
            returnedversion = Core.getConfig().get().getInt("settings.default-protocol");
        }

        return returnedversion;
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(new TextComponent(replaceColorCodes(Core.getConfig().get().getString("plugin-prefix") + " " + message)));
    }

    public static void sendMessage(CommandSender sender, String prefix, String defaultMessage, String suffix, String messagePath) {
        String m = getMessage(prefix, defaultMessage, suffix, messagePath, true);

        sender.sendMessage(new TextComponent(m));
    }

    public static String getMessage(String prefix, String defaultMessage, String suffix, String messagePath, boolean addPrefix) {
        String m = Core.getConfig().getMessages(Core.Language).getString(messagePath);
        if (m == null) {
            m = defaultMessage;
        } else {
            m = prefix + m + suffix;
        }
        m = replaceColorCodes(m);
        if (addPrefix) {
            m = replaceColorCodes(Core.getConfig().get().getString("plugin-prefix")) + " " + m;
        }
        return m;
    }

    public static Map<Object, Object> dump(Configuration configuration) {
        Map<Object, Object> ret = new HashMap<>();
        for (String i : configuration.getKeys()) {
            if (configuration.get(i) instanceof Configuration) {
                ret.put(i, dump(configuration.getSection(i)));
            } else {
                ret.put(i, configuration.get(i));
            }
        }
        return ret;
    }

    // Totally good name for a function.
    public static void undump(Configuration configuration, Map<Object, Object> dumped) {
        for (Map.Entry<Object, Object> entry : dumped.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Configuration configuration1 = new Configuration();
                undump(configuration1, (Map<Object, Object>) entry.getValue());
                configuration.set((String) entry.getKey(), configuration1);
            } else {
                configuration.set((String) entry.getKey(), entry.getValue());
            }
        }
    }

    public static String getName(UUID name) {
        if (name == null) {
            return HIMSELF.get();
        }
        if (name.equals(Utils.CONSOLE_UUID)) {
            return CONSOLE.get();
        }
        if (Core.redisBungeeIntegration.isDetected()) {
            if (Core.redisBungeeIntegration.getApi().isPlayerOnline(name)) {
                return Core.redisBungeeIntegration.getApi().getNameFromUuid(name, false);
            } else {
                return name.toString() + " (" + getMessage("", "Player not found in all instances", "", "vanish.log-not-found-redisbungee", false) + ")";
            }
        } else {
            if (ProxyServer.getInstance().getPlayer(name) != null) {
                return ProxyServer.getInstance().getPlayer(name).getName();
            } else {
                return name.toString() + " (" + getMessage("", "Player not found", "", "vanish.log-not-found", false) + ")";
            }
        }
    }

    public static void removeBlocked(Map<Object, Object> map) {
        // Defaults
        map.remove("config-version");
        map.remove("update-checker-enabled");
        map.remove("language");
        map.remove("multiproxy");
        map.remove("connectionsettings");
        map.remove("replace");
        // Custom
        for (String i : Core.getConfig().get().getStringList("multiproxy.blocked")) {
            if (i.contains(".")) {
                List<String> pathList = new LinkedList<>(Arrays.asList(i.split("\\.")));
                String firstNode = pathList.get(0);
                pathList.remove(0);
                if (map.get(firstNode) instanceof Map) {
                    removeRecursive(pathList.size() != 0 ? String.join(".", pathList) : "", (Map<Object, Object>) map.get(firstNode));
                } else {
                    map.remove(firstNode);
                }
            } else {
                map.remove(i);
            }
        }
    }

    public static void removeRecursive(String name, Map<Object, Object> map) {
        if (name.contains(".")) {
            List<String> pathList = new LinkedList<>(Arrays.asList(name.split("\\.")));
            String firstNode = pathList.get(0);
            pathList.remove(0);
            if (map.get(firstNode) instanceof Map) {
                removeRecursive(pathList.size() != 0 ? String.join(".", pathList) : "", (Map<Object, Object>) map.get(firstNode));
            } else {
                map.remove(firstNode);
            }
        } else {
            map.remove(name);
        }
    }

    public static boolean isReplaceBlocked(String name) {
        return name.equals("config-version") || name.equals("update-checker-enabled") || name.equals("language") || name.equals("multiproxy") || name.equals("connectionsettings") || name.equals("replace") || name.equals("allowed-players");
    }
}
