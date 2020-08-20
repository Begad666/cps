package begad.mc.bc.plugin.cps.utils;

import begad.mc.bc.plugin.cps.Core;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public Utils() {
    }

    public static String replaceUnicodeCharacters(String string) {
        String newstring = "";
        String old = string;
        Collection<String> replace = Core.getConfig().get().getSection("replace").getKeys();

        for (Iterator stringIterator = replace.iterator(); stringIterator.hasNext(); newstring = old) {
            String rk = (String) stringIterator.next();
            old = old.replace(rk, Core.getConfig().get().getSection("replace").getString(rk));
        }

        return newstring;
    }

    public static String replaceColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String replacePlaceHolders(String string) {
        String max_players = Integer.toString(Core.getConfig().get().getInt("network-info.max-players"));
        String online = Integer.toString(ProxyServer.getInstance().getOnlineCount());
        String newstring = string.replace("%online%", online).replace("%max%", max_players).replace("%net-name%", Core.getConfig().get().getString("network-info.name"));
        Matcher matcher = Pattern.compile("%bungee_(.*):players%").matcher(newstring);

        while (matcher.find()) {
            String server_name = matcher.group(1);
            if (ProxyServer.getInstance().getServers().containsKey(server_name)) {
                int size = ProxyServer.getInstance().getServers().get(server_name).getPlayers().size();
                newstring = newstring.replace("%bungee_" + server_name + ":players%", Integer.toString(size));
            } else {
                newstring = newstring.replace("%bungee_" + server_name + ":players%", "&4Not Found");
            }
        }

        return newstring;
    }

    public static String replaceEveryThing(String string) {
        String stringnew = replacePlaceHolders(string);
        stringnew = replaceColorCodes(stringnew);
        return stringnew;
    }

    public static PlayerInfo[] getHoverMessage() {
        ArrayList<String> list = (ArrayList) Core.getConfig().get().getStringList("hover-messages.default-hover-message");
        PlayerInfo[] hovermessage = new PlayerInfo[list.size()];

        for (int i = 0; i < hovermessage.length; ++i) {
            hovermessage[i] = new PlayerInfo(replaceColorCodes(replacePlaceHolders(list.get(i))), UUID.fromString("0-0-0-0-0"));
        }

        return hovermessage;
    }

    public static PlayerInfo[] getMaintenanceHoverMessage() {
        ArrayList<String> list = (ArrayList) Core.getConfig().get().getStringList("hover-messages.maintenance-hover-message");
        PlayerInfo[] hovermessage = new PlayerInfo[list.size()];

        for (int i = 0; i < hovermessage.length; ++i) {
            hovermessage[i] = new PlayerInfo(replaceColorCodes(replacePlaceHolders(list.get(i))), UUID.fromString("0-0-0-0-0"));
        }

        return hovermessage;
    }

    public static int getVersion(int playerversion) {
        ArrayList<Integer> list = (ArrayList) Core.getConfig().get().getIntList("settings.allowed-protocols");
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
