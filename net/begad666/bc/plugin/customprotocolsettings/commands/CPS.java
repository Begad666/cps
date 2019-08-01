package net.begad666.bc.plugin.customprotocolsettings.commands;


import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.Processarraylists;
import net.begad666.bc.plugin.customprotocolsettings.utils.ProcessStrings;
import net.begad666.bc.plugin.customprotocolsettings.utils.Updates;

public class CPS extends Command{
	private static String messagefromMessageinfo;
	private static String versionforupdating;
	public static boolean isEnabled;
	
	public CPS()
	{
		super("cps", "cps.admin", new String[0]);
	}
	public void execute(CommandSender sender, final String[] args) {
		if(args.length != 1)
		{if (args.length != 2) {
		    ArrayList<String> list = Updates.getUpdateInfo();
		    if(list.get(0) == "true") 
		    {
		    messagefromMessageinfo = list.get(2);
		    versionforupdating = list.get(1);
		    }
		    if(list.get(0) == "false")
		    {
		    messagefromMessageinfo = "No Updates is Required";
		    versionforupdating = "";
		    }
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Version: " + Updates.getCurrentVersion()));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config Version:" + Config.getconfig().getString("config-version")));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Updates: " + messagefromMessageinfo + " " + versionforupdating));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Minimum Required version of jre: 1.8.1_191"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " #1417 version of BungeeCord is Minium Required"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
		}
		}
		if (args.length >= 1)
		{
		 if ((args[0].equalsIgnoreCase("reload")))
		{
        reload(sender);
		}
		if ((args[0].equalsIgnoreCase("ps")))
		{	
        ps(sender);
		}
		if ((args[0].equalsIgnoreCase("ms")))
		{	
        ms(sender);
		}
		if ((args[0].equalsIgnoreCase("pf")))
		{
		pf(sender);
		}
		if ((args[0].equalsIgnoreCase("license")))
		{
		license(sender);
		}
		if ((args[0].equalsIgnoreCase("enable")))
		{
		try {
		if ((args[1].equals(Config.getconfig().getString("password")))) 
	    {
		enable(sender);
		}
		else
		{
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Password is wrong , Please Try Again"));
		}
		} catch (Exception e)
		{
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Please type the password"));
		}
		}
		if ((args[0].equalsIgnoreCase("disable")))
		{
		try {
		if ((args[1].equals(Config.getconfig().getString("password")))) 
	    {
		disable(sender);
		}
		else
		{
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Password is wrong , Please Try Again"));
		}
		}catch (Exception e)
		{
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Please type the password"));
		}
		} 

	  }
	}
  public static void ps(CommandSender sender)
  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Protocol Settings"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Protocol Name:" + Config.getconfig().getString("protocolsettings.protocol-name")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Motd: 1." + Config.getconfig().getString("protocolsettings.motd.1")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + "       2." + Config.getconfig().getString("protocolsettings.motd.2")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Max Players:" + Config.getconfig().getInt("protocolsettings.max-players")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Default Allowed Protocol: " + Config.getconfig().getInt("protocolsettings.default-allowed-protocol")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Allowed Protocols: " + Processarraylists.processversionforchecking()));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Blocked Message: " + Config.getconfig().getString("protocolsettings.blocked-message")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
  }
  public static void ms(CommandSender sender)
  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Maintenance Settings"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Enabled: " + Config.getconfig().getBoolean("maintenancesettings.enabled")));
        sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Motd: 1." + Config.getconfig().getString("maintenancesettings.motd.1")));
        sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + "       2." + Config.getconfig().getString("maintenancesettings.motd.2")));
        sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Blocked Message: " + Config.getconfig().getString("maintenancesettings.blocked-message")));
        sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Allowed Players: " + Config.getconfig().getStringList("maintenancesettings.allowed-players")));
        sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
  }
  public static void pf(CommandSender sender)
  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Prefixs"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Plugin Prefix: " + Config.getconfig().getString("prefixs.plugin")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Ping Prefix: " + Config.getconfig().getString("prefixs.ping")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ---------------------------------"));
  }
  public static void reload(CommandSender sender)
		  {
	    Config.check("Reload");
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config reload process finshed!"));
		sender.sendMessage(new TextComponent(""));
		ps(sender);
		ms(sender);
		pf(sender);
		  }
  public static void license(CommandSender sender)
  {
  sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + "\r\nMIT License\r\n" + 
  		"\r\n" + 
  		"Copyright (c) 2019 Begad666\r\n" + 
  		"\r\n" + 
  		"Permission is hereby granted, free of charge, to any person obtaining a copy\r\n" + 
  		"of this software and associated documentation files (the \"Software\"), to deal\r\n" + 
  		"in the Software without restriction, including without limitation the rights\r\n" + 
  		"to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\r\n" + 
  		"copies of the Software, and to permit persons to whom the Software is\r\n" + 
  		"furnished to do so, subject to the following conditions:\r\n" + 
  		"\r\n" + 
  		"The above copyright notice and this permission notice shall be included in all\r\n" + 
  		"copies or substantial portions of the Software.\r\n" + 
  		"\r\n" + 
  		"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\r\n" + 
  		"IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\r\n" + 
  		"FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\r\n" + 
  		"AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\r\n" + 
  		"LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\r\n" + 
  		"OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\r\n" + 
  		"SOFTWARE."));
  }
  private void enable(CommandSender sender)
  {
  if (CPS.isEnabled)
  {
	 sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already enabled!"));
  }
  else
  {
	sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Enabling Plugin Features..."));
	PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
	pluginManager.registerListener(Main.getInstance(), new ChangePingData());
	pluginManager.registerListener(Main.getInstance(), new DisconnectNotAllowedUsers());
	pluginManager.registerCommand(Main.getInstance(), new Ping());
	isEnabled = true;
	sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Enabled"));
  }
  }
  private void disable(CommandSender sender)
  {
	  if (!CPS.isEnabled)
	  {
		 sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already disabled!"));
	  }
	  else
	  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Disabling Plugin Features..."));
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.unregisterListeners(Main.getInstance());
        pluginManager.unregisterCommand(new Ping());
        isEnabled = false;
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Disabled"));
	  }
  }
  
}
