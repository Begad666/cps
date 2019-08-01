package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.util.ArrayList;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;

public class Processarraylists {
	private static ServerPing.PlayerInfo[] hovermessage;
	public static PlayerInfo[] processhovermessage()
	{
		ArrayList<String> list = (ArrayList)Config.getconfig().getStringList("protocolsettings.hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];
		
		 for (int i = 0; i < hovermessage.length; i++)
		 {
		 hovermessage[i] = new ServerPing.PlayerInfo(ProcessStrings.replacecodesandcolors(ProcessStrings.replaceplaceholders((String)list.get(i))), java.util.UUID.fromString("0-0-0-0-0")); }
		 return hovermessage;
	}
	public static ServerPing.PlayerInfo[] processmaintenancehovermessage()
	{
		ArrayList<String> list = (ArrayList)Config.getconfig().getStringList("maintenancesettings.hover-message");
		hovermessage = new ServerPing.PlayerInfo[list.size()];
		
		 for (int i = 0; i < hovermessage.length; i++)
		 {
		 hovermessage[i] = new ServerPing.PlayerInfo(ProcessStrings.replacecodesandcolors(ProcessStrings.replaceplaceholders((String)list.get(i))), java.util.UUID.fromString("0-0-0-0-0")); }
		 return hovermessage;
	}
	public static int processversion(int playerversion)
	{

		ArrayList<Integer> list = (ArrayList)Config.getconfig().getIntList("protocolsettings.allowed-protocols");
		
		               int returnedversion;
		               if(list.contains(playerversion))
		               returnedversion = playerversion;
		               else
		               returnedversion = Config.getconfig().getInt("protocolsettings.default-allowed-protocol");
					   return returnedversion;
	}
	public static ArrayList<Integer> processversionforchecking()
	{
		ArrayList<Integer> list = (ArrayList)Config.getconfig().getIntList("protocolsettings.allowed-protocols");
	    return list;
	}
	

}
