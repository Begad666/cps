package begad.mc.bc.plugin.cps.utils;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.placeholders.Placeholder;
import begad.mc.placeholders.PlaceholderGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public Utils() {
    }

    static {
        Placeholder max = new Placeholder("MaxPlayers", "%max%", (String input, Matcher matcher) -> input.replaceFirst("%max%", Integer.toString(Core.getConfig().get().getInt("network-info.max-players") > 0 ? Core.getConfig().get().getInt("network-info.max-players") : ProxyServer.getInstance().getOnlineCount() + 1)), 0);
        Placeholder online = new Placeholder("OnlinePlayers", "%online%", (String input, Matcher matcher) -> input.replaceFirst("%online%", Integer.toString(ProxyServer.getInstance().getOnlineCount())), 0);
        Placeholder netName = new Placeholder("NetworkName", "%net-name%", (String input, Matcher matcher) -> input.replaceFirst("%net-name%", Core.getConfig().get().getString("network-info.name")), 0);

        PlaceholderGroup placeholderGroup = new PlaceholderGroup("Bungee", "%bungee_([1-Z]+):([A-Z]+)%", Pattern.CASE_INSENSITIVE);
        placeholderGroup.addPlaceholder(new Placeholder("Players", "%bungee_(.*):players%", (String input, Matcher matcher) -> {
            String output = input;
            if (matcher.find()) {
                String server_name = matcher.group(2);
                if (ProxyServer.getInstance().getServers().containsKey(server_name)) {
                    int size = ProxyServer.getInstance().getServers().get(server_name).getPlayers().size();
                    output = output.replaceFirst("%bungee_" + server_name + ":players%", Integer.toString(size));
                } else {
                    output = output.replaceFirst("%bungee_" + server_name + ":players%", "&4Not Found");
                }
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
            ArrayList<ProxiedPlayer> list = new ArrayList<>(ProxyServer.getInstance().getPlayers());
            if (list.size() > Core.getConfig().get().getInt("hover-messages.show-players-limit")) {
                hovermessage = new PlayerInfo[Core.getConfig().get().getInt("hover-messages.show-players-limit")];
            } else {
                hovermessage = new PlayerInfo[list.size()];
            }

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(list.get(i).getDisplayName(), list.get(i).getUniqueId());
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
            ArrayList<ProxiedPlayer> list = new ArrayList<>(ProxyServer.getInstance().getPlayers());
            if (list.size() > Core.getConfig().get().getInt("hover-messages.show-players-limit")) {
                hovermessage = new PlayerInfo[Core.getConfig().get().getInt("hover-messages.show-players-limit")];
            } else {
                hovermessage = new PlayerInfo[list.size()];
            }

            for (int i = 0; i < hovermessage.length; ++i) {
                hovermessage[i] = new PlayerInfo(list.get(i).getDisplayName(), list.get(i).getUniqueId());
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
        if (addPrefix) {
            m = replaceColorCodes(Core.getConfig().get().getString("plugin-prefix") + " " + m);
        }
        return m;
    }
}
