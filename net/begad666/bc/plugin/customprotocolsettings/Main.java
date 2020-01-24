package net.begad666.bc.plugin.customprotocolsettings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.begad666.bc.plugin.customprotocolsettings.commands.CPS;
import net.begad666.bc.plugin.customprotocolsettings.commands.Ping;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.DatabaseConnectionManager;
import net.begad666.bc.plugin.customprotocolsettings.utils.MetricsLite;
import net.begad666.bc.plugin.customprotocolsettings.utils.MainUtils;
import net.begad666.bc.plugin.customprotocolsettings.utils.ScheduledTasks;
import net.begad666.bc.plugin.customprotocolsettings.utils.Updates;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Main extends Plugin{
	private static Main instance;
	public void onEnable()
    {   
		instance = this;
		getInstance().getLogger().info("Started Enable Process");
		getInstance().getLogger().info("Loading Config...");
		boolean bool = Config.check("Load");
		if (!bool) 
		{
			return;
		}
	    getInstance().getLogger().info("Registering Commands...");
	    RegisterCommands();
	    getInstance().getLogger().info("Registering Listeners...");
	    RegisterListeners();
	    if (Config.getconfig().getBoolean("multiproxy.enable"))
	    {
	    	getInstance().getLogger().info("Connecting To Database...");
	    	DatabaseConnectionManager.connect();
	    	DatabaseConnectionManager.executeUpdate(
	    			"CREATE TABLE IF NOT EXISTS `cps` "
	    			+ "( `groupId` VARCHAR(25) NOT NULL ," 
	    			+ " `configjson` LONGTEXT NOT NULL ,"
	    			+ "	PRIMARY KEY (`groupId`)" 
	    			+ ")" );
	    	ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable() 
	    	{
	    		public void run() 
	    		{ 
	    			ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Reconnecting to database..."); 
	    			DatabaseConnectionManager.disconnect(); 
	    			DatabaseConnectionManager.connect();
	    		} 
	    	}, 8,8, TimeUnit.HOURS);
	    	if (Config.getconfig().getBoolean("multiproxy.autopull"))
	    		ScheduledTasks.autopulltask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable()
	    		{
	    			public void run() 
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
	    		} ,Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
	    }
	    MetricsLite metrics = new MetricsLite(getInstance());
	    Updates.setUpdateInfo("true", "", "Please Wait...");
	    if(Config.getconfig().getBoolean("update-checker-enabled") == true)
	    {
	    	ScheduledTasks.updatetask1 = ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable()
		    {
    			public void run()
    			{
    				String current = Updates.getCurrentVersion();
    				String latest = Updates.getLatestVersion();
    				if (latest == null)
    				{
    					ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't Check for Updates, Check Your Connection");
    					Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
    				}
    				if (latest != null)
    				{
    					if(current.compareTo(latest) < 0) 
    					{
    						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " You are on: " + current);
    						Updates.setUpdateInfo("true", latest, " New Version:");
    					}
    					if(current.compareTo(latest) == 0)
    					{
    						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
    						Updates.setUpdateInfo("false", null, null);
    					}
    					if(current.compareTo(latest) > 0)
    					{
    						if (getInstance().getDescription().getVersion().compareTo(Updates.getCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(Updates.getCurrentVersion()) < 0) 
    						{
    							ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
    							Updates.setUpdateInfo("true", latest, " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is ");
    						}
    						else 
    						{
        						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
        						Updates.setUpdateInfo("false", null, null);
    						}
    					}
    				}
	           }
		    }, 4 , TimeUnit.SECONDS);
	    	ScheduledTasks.updatetask2  = ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable()
	    	{

    			public void run()
    			{
    				String current = Updates.getCurrentVersion();
    				String latest = Updates.getLatestVersion();
    				if (latest == null)
    				{
    					ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't Check for Updates, Check Your Connection");
    					Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
    				}
    				if (latest != null)
    				{
    					if(current.compareTo(latest) < 0) 
    					{
    						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " You are on: " + current);
    						Updates.setUpdateInfo("true", latest, " New Version:");
    					}
    					if(current.compareTo(latest) == 0)
    					{
    						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
    						Updates.setUpdateInfo("false", null, null);
    					}
    					if(current.compareTo(latest) > 0)
    					{
    						if (getInstance().getDescription().getVersion().compareTo(Updates.getCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(Updates.getCurrentVersion()) < 0) 
    						{
    							ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
    							Updates.setUpdateInfo("true", latest, " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is ");
    						}
    						else 
    						{
        						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
        						Updates.setUpdateInfo("false", null, null);
    						}
    					}
    				}
	           }
	    	}, 30 , 30 , TimeUnit.MINUTES);
	    }
	    else
	    {
	    	Updates.setUpdateInfo("true", "", "Updates Are Disabled");
	    }
	    CPS.isEnabled = true;
	    getInstance().getLogger().info(Updates.getCurrentVersion() + " Is now enabled!");
	   
    }
	public void onDisable()
	{
		PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
		getInstance().getLogger().info("Started Disable Process...");
		getInstance().getLogger().info("Unregistering Commands...");
		pluginmanager.unregisterCommands(Main.getInstance());
		getInstance().getLogger().info("Unregistering Listeners...");
		pluginmanager.unregisterListeners(Main.getInstance());
		if (Config.getconfig().getBoolean("multiproxy.enable"))
		{
			getInstance().getLogger().info("Disconnecting From Database...");
			DatabaseConnectionManager.disconnect();
		}
		getInstance().getLogger().info("Canceling Scheduled Tasks...");
		ProxyServer.getInstance().getScheduler().cancel(getInstance());
		getInstance().getLogger().info(Updates.getCurrentVersion() + " Is now disabled!");
		CPS.isEnabled = false;
	}
	public static Main getInstance()
	{
		return instance;
	}

    private void RegisterListeners()
    {
    	PluginManager pluginManager = getProxy().getPluginManager();
	
    	pluginManager.registerListener(getInstance(), new ChangePingData());
    	pluginManager.registerListener(getInstance(), new DisconnectNotAllowedUsers());
    }
	
   private void RegisterCommands()
   {
	  PluginManager pluginManager = getProxy().getPluginManager();
	  
	  pluginManager.registerCommand(getInstance(), new CPS());
	  pluginManager.registerCommand(getInstance(), new Ping());
   }
}
