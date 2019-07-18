package net.begad666.bc.plugin.customprotocolsettings.commands;


import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.Processarraylists;
import net.begad666.bc.plugin.customprotocolsettings.utils.ProcessStrings;
import net.begad666.bc.plugin.customprotocolsettings.utils.Updates;

public class CPS extends Command{
	private static String messagefromMessageinfo;
	private static String versionforupdating;
	
	public CPS()
	{
		super("cps", "cps.admin", new String[0]);
	}
	public void execute(CommandSender sender, final String[] args) {
		if(args.length != 1)
		{
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
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " ---------------------------------"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Version: " + Updates.getCurrentVersion()));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Updates: " + messagefromMessageinfo + " " + versionforupdating));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Minium Required version of jre: 1.8.1_191"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " #1417 version of BungeeCord is Minium Required"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " ---------------------------------"));
		}
		if (args.length == 1)
		{
		 if ((args[0].equalsIgnoreCase("reload")))
		{
        reload(sender);
		}
		if ((args[0].equalsIgnoreCase("loaded")))
		{	
        loaded(sender);
		}
		if ((args[0].equalsIgnoreCase("renew")))
		{
		if (Config.getIsRenewRequired())
		renew(sender);	
		else
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " No Renew is required"));
		}

	  }
	}
  public static void loaded(CommandSender sender)
  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " ---------------------------------"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Here are Loaded Config Values"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Config Version:" + Config.getconfig().getString("config-version")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Protocol Name:" + Config.getconfig().getString("protocol-name")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Motd: 1." + Config.getconfig().getString("motd.1")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + "       2." + Config.getconfig().getString("motd.2")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Max Players:" + Config.getconfig().getInt("max-players")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Hover Message: " + Processarraylists.processhovermessage()));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Updates: " + Config.getconfig().getBoolean("update-checker-enabled")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Allowed Protocols: " + Processarraylists.processversionforchecking()));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Blocked Message: " + Config.getconfig().getString("blocked-message")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " ---------------------------------"));
  }
  public static void reload(CommandSender sender)
		  {
	    Config.check("Reload");
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Config reload process finshed!"));
		sender.sendMessage(new TextComponent(""));
		loaded(sender);
		  }
  public static void renew(CommandSender sender)
  {
	  sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Checking what is needed to renew..."));
	  Config.renew(sender, Config.getconfig().getString("config-version"));
  }
}
