package net.begad666.bc.plugin.customprotocolsettings.commands;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.DatabaseConnectionManager;
import net.begad666.bc.plugin.customprotocolsettings.utils.MainUtils;
import net.begad666.bc.plugin.customprotocolsettings.utils.ScheduledTasks;
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
		{
			if (args.length != 2) 
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
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ------------------------------"));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Version: " + Updates.getCurrentVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Version: " + Main.getInstance().getDescription().getVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Config Version: " + Updates.getConfigVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config Version: " + Config.getconfig().getString("config-version")));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Updates:" + messagefromMessageinfo + " " + versionforupdating));
				TextComponent pluglink = new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Plugin Page: Click Here" );
				pluglink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/customprotocolsettings.69385/"));
				sender.sendMessage(pluglink);
				TextComponent devlink = new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Developer: Click Here" );
				devlink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/members/begad.478174/"));
				sender.sendMessage(devlink);
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ------------------------------"));
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
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " That command was deleted in v2.0, sorry for the inconvenience"));
			}
			if ((args[0].equalsIgnoreCase("ms")))
			{	
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " That command was deleted in v2.0, sorry for the inconvenience"));
			}
			if ((args[0].equalsIgnoreCase("pf")))
			{
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " That command was deleted in v2.0, sorry for the inconvenience"));
			}
			if ((args[0].equalsIgnoreCase("license")))
			{
				license(sender);
			}
			if ((args[0].equalsIgnoreCase("enable")))
			{
				try 
				{
					if ((args[1].equals(Config.getconfig().getString("password")))) 
					{
						enable(sender);
					}
					else
					{
						sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Password is wrong , Please Try Again"));
					}
				} 
				catch (Exception e)
				{
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Please type the password"));
				}
			}
			if ((args[0].equalsIgnoreCase("disable")))
			{
				try 
				{
					if ((args[1].equals(Config.getconfig().getString("password")))) 
					{
						disable(sender);
					}
					else
					{
						sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Password is wrong , Please Try Again"));
					}
				}
				catch (Exception e)
				{
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Please type the password"));
				}
			}
			if ((args[0].equalsIgnoreCase("checkdbconnection")))
			{
				checkdbconnection(sender);
			}
			if ((args[0].equalsIgnoreCase("pullnow")))
			{
				if (Config.getconfig().getBoolean("multiproxy.enable"))
				{
					if (DatabaseConnectionManager.getConnected())
					{
						pullnow(sender);
					}
					else
					{
						sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
					}
				}
				else
				{
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
				}
			}

		}
	}
    public static void reload(CommandSender sender)
    {
	    Config.check("Reload");
	    Main.getInstance().getLogger().info("Disconnecting From Database");
	    DatabaseConnectionManager.disconnect();
	    Main.getInstance().getLogger().info("Reconnecting to Database");
	    DatabaseConnectionManager.connect();
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config reload process finished!"));
	}
    public static void license(CommandSender sender)
    {
    	sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + "\r\nMIT License\r\n" + 
  		"\r\n" + 
  		"Copyright (c) 2020 Begad666\r\n" + 
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
    		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already enabled!"));
    	}
    	else
    	{
    		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Enabling Plugin Features..."));
    		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
    		pluginManager.registerListener(Main.getInstance(), new ChangePingData());
    		pluginManager.registerListener(Main.getInstance(), new DisconnectNotAllowedUsers());
    		pluginManager.registerCommand(Main.getInstance(), new Ping());
    		if (Config.getconfig().getBoolean("multiproxy.enable"))
    		{
    			Main.getInstance().getLogger().info("Connecting To Database...");
    			DatabaseConnectionManager.connect();
    			ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {public void run() { ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Reconnecting to database..."); DatabaseConnectionManager.disconnect(); DatabaseConnectionManager.connect();} }, 8,8, TimeUnit.HOURS);
    			if (Config.getconfig().getBoolean("multiproxy.autopull"))
    				ScheduledTasks.autopulltask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable()
    				{
    					public void run() 
    					{
    						
    					}
    				} ,Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
    		}
    		isEnabled = true;
    		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Enabled"));
    	}
  }
  private void disable(CommandSender sender)
  {
	  if (!CPS.isEnabled)
	  {
		 sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already disabled!"));
	  }
	  else
	  {
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Disabling Plugin Features..."));
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.unregisterListeners(Main.getInstance());
        pluginManager.unregisterCommand(new Ping());
   	    if (Config.getconfig().getBoolean("multiproxy.enable"))
   	    {
   	    Main.getInstance().getLogger().info("Disconnecting From Database...");
   	    DatabaseConnectionManager.disconnect();
   	    }
        isEnabled = false;
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Disabled"));
	  }
  }
  private void checkdbconnection(CommandSender sender)
  {
	  if (!DatabaseConnectionManager.getConnected())
		  sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Not Connected"));
	  else
		  sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Connected"));
  }
  private void pullnow(CommandSender sender)
  {
		ResultSet data = null;
		Gson configjson = new Gson();
		try 
		{
			data = DatabaseConnectionManager.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Config.getconfig().getString("multiproxy.groupid") + "'"); 
			while(data.next())
				configjson.toJson(data.getString("configjson"));
		} 
		catch (JsonSyntaxException e) 
		{
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while processing pulled json data from database, most likely because there is no data, check your table, no changes will be made"));
			return;
		} 
		catch (SQLException e) 
		{
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while pulling data from database, no changes will be made"));
			return;
		}
  
  }
  
}
