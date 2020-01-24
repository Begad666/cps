package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;

public class MainUtils {
	public static String replacecodesandcolors(String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string.replace(">>", "»")
				.replace("<<", "«"));
	}
	
	public static String replaceplaceholders(String string)
	{
		String max_players = Integer.toString(Config.getconfig().getInt("network-info.max-players"));
		String online = Integer.toString(ProxyServer.getInstance().getOnlineCount());
		return string.replace("%online%", online).replace("%max%", max_players);
	}
	public static String replaceall(String string) 
	{
		String stringnew = replaceplaceholders(string);
		stringnew = replacecodesandcolors(string);
		return stringnew;
	}
	private static ServerPing.PlayerInfo[] hovermessage;
	public static PlayerInfo[] processhovermessage()
	{
		ArrayList<String> list = (ArrayList<String>)Config.getconfig().getStringList("hover-messages.default-hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];
		
		 for (int i = 0; i < hovermessage.length; i++)
		 {
			 hovermessage[i] = new ServerPing.PlayerInfo(MainUtils.replacecodesandcolors(MainUtils.replaceplaceholders(list.get(i))), java.util.UUID.fromString("0-0-0-0-0")); 
		 }
		 return hovermessage;
	}

	public static ServerPing.PlayerInfo[] processmaintenancehovermessage()
	{
		ArrayList<String> list = (ArrayList<String>)Config.getconfig().getStringList("hover-messages.maintenance-hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];
		
		for (int i = 0; i < hovermessage.length; i++)
		{
			hovermessage[i] = new ServerPing.PlayerInfo(MainUtils.replacecodesandcolors(MainUtils.replaceplaceholders(list.get(i))), java.util.UUID.fromString("0-0-0-0-0")); 
		}
		return hovermessage;
	}

	public static int processversion(int playerversion)
	{

		ArrayList<Integer> list = (ArrayList<Integer>)Config.getconfig().getIntList("settings.allowed-protocols");
		int returnedversion;
		if(list.contains(playerversion))
			returnedversion = playerversion;
		else
		   	returnedversion = Config.getconfig().getInt("settings.server-protocol");
		return returnedversion;
	}
}
