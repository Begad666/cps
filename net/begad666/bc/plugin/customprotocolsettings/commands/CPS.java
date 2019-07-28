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
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " ---------------------------------"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Version: " + Updates.getCurrentVersion()));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Updates: " + messagefromMessageinfo + " " + versionforupdating));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Minimum Required version of jre: 1.8.1_191"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " #1417 version of BungeeCord is Minium Required"));
			sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " ---------------------------------"));
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
		if ((args[0].equalsIgnoreCase("license")))
		{
		license(sender);
		}

	  }
	}
  public static void loaded(CommandSender sender)
  {
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " ---------------------------------"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Here are Loaded Config Values"));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Config Version:" + Config.getconfig().getString("config-version")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Protocol Name:" + Config.getconfig().getString("protocol-name")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Motd: 1." + Config.getconfig().getString("motd.1")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + "       2." + Config.getconfig().getString("motd.2")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Max Players:" + Config.getconfig().getInt("max-players")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Hover Message: " + Processarraylists.processhovermessage()));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Updates: " + Config.getconfig().getBoolean("update-checker-enabled")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Default Allowed Protocol: " + Config.getconfig().getInt("default-allowed-protocol")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Allowed Protocols: " + Processarraylists.processversionforchecking()));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Ping Prefix: " + Config.getconfig().getString("ping_prefix")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Blocked Message: " + Config.getconfig().getString("blocked-message")));
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " ---------------------------------"));
  }
  public static void reload(CommandSender sender)
		  {
	    Config.check("Reload");
		sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + " Config reload process finshed!"));
		sender.sendMessage(new TextComponent(""));
		loaded(sender);
		  }
  public static void license(CommandSender sender)
  {
  sender.sendMessage(new TextComponent(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("plugin_prefix")) + "\r\nMIT License\r\n" + 
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
}
