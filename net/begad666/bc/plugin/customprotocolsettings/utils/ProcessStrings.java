package net.begad666.bc.plugin.customprotocolsettings.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class ProcessStrings {
	public static String replacecodesandcolors(String string)
	 {
	return ChatColor.translateAlternateColorCodes('&', string.replace(">>", "»")
	.replace("<<", "«"));
	}
	
	public static String replaceplaceholders(String string)
	{
	String max_players = Integer.toString(Config.getconfig().getInt("max-players"));
	String online = Integer.toString(ProxyServer.getInstance().getOnlineCount());
	return string.replace("%online%", online).replace("%max%", max_players);
	}

}
