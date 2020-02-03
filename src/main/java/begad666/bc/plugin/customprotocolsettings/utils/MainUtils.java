package begad666.bc.plugin.customprotocolsettings.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainUtils {
	public static String replaceunicode(String string) {
		String newstring = "";
		Collection<String> replace = Config.getconfig().getSection("replace").getKeys();
		for (String rk : replace) {
			if (string.contains(rk)) {
				newstring = string.replace(rk, Config.getconfig().getSection("replace").getString(rk));
			}
		}
		return newstring == "" ? newstring : string;
	}

	public static String replacecodesandcolors(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String replaceplaceholders(String string) {
		String max_players = Integer.toString(Config.getconfig().getInt("network-info.max-players"));
		String online = Integer.toString(ProxyServer.getInstance().getOnlineCount());
		String newstring;
		newstring = string.replace("%online%", online).replace("%max%", max_players).replace("%net-name%", Config.getconfig().getString("network-info.name"));
		Matcher matcher = Pattern.compile("%bungee_(.*)%", Pattern.CASE_INSENSITIVE).matcher(newstring);
		while (matcher.find()) {
			String server_name = matcher.group(1);
			if (ProxyServer.getInstance().getServers().containsKey(server_name)) {
				int size = ProxyServer.getInstance().getServers().get(server_name).getPlayers().size();
				newstring = newstring.replace("%bungee_" + server_name + "%", Integer.toString(size));
			} else {
				newstring = newstring.replace("%bungee_" + server_name + "%", "&4Not Found");
			}
		}
		return newstring;
	}

	public static String replaceall(String string) {
		String stringnew;
		stringnew = replaceplaceholders(string);
		stringnew = replacecodesandcolors(stringnew);
		return stringnew;
	}

	private static ServerPing.PlayerInfo[] hovermessage;

	public static PlayerInfo[] processhovermessage() {
		ArrayList<String> list = (ArrayList<String>) Config.getconfig().getStringList("hover-messages.default-hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];

		for (int i = 0; i < hovermessage.length; i++) {
			hovermessage[i] = new ServerPing.PlayerInfo(MainUtils.replacecodesandcolors(MainUtils.replaceplaceholders(list.get(i))), java.util.UUID.fromString("0-0-0-0-0"));
		}
		return hovermessage;
	}

	public static ServerPing.PlayerInfo[] processmaintenancehovermessage() {
		ArrayList<String> list = (ArrayList<String>) Config.getconfig().getStringList("hover-messages.maintenance-hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];

		for (int i = 0; i < hovermessage.length; i++) {
			hovermessage[i] = new ServerPing.PlayerInfo(MainUtils.replacecodesandcolors(MainUtils.replaceplaceholders(list.get(i))), java.util.UUID.fromString("0-0-0-0-0"));
		}
		return hovermessage;
	}

	public static int processversion(int playerversion) {

		ArrayList<Integer> list = (ArrayList<Integer>) Config.getconfig().getIntList("settings.allowed-protocols");
		int returnedversion;
		if (list.contains(playerversion))
			returnedversion = playerversion;
		else
			returnedversion = Config.getconfig().getInt("settings.default-protocol");
		return returnedversion;
	}
}
