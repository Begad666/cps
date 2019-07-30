package net.begad666.bc.plugin.customprotocolsettings;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.begad666.bc.plugin.customprotocolsettings.commands.CPS;
import net.begad666.bc.plugin.customprotocolsettings.commands.Ping;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectBlockedProtocols;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.MetricsLite;
import net.begad666.bc.plugin.customprotocolsettings.utils.ProcessStrings;
import net.begad666.bc.plugin.customprotocolsettings.utils.ScheduledTasks;
import net.begad666.bc.plugin.customprotocolsettings.utils.Updates;

public class Main extends Plugin{
	private static Main instance;
	public void onEnable()
   {   
		instance = this;
		getInstance().getLogger().info("Started Enable Process");
		getInstance().getLogger().info("Loading Config...");
		Config.check("Load");
	    getInstance().getLogger().info("Registering Commands...");
	    RegisterCommands();
	    getInstance().getLogger().info("Registering Listeners...");
	    RegisterListeners();
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
		    	ProxyServer.getInstance().getLogger().warning(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't Check for Updates , Check Your Connection");
		    	Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
		       }
		       if(latest != null)
		       {
		    	if(current.compareTo(latest) < 0) 
		    	{
		    	 ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " You are on: " + current);
		    	 Updates.setUpdateInfo("true", latest, "New Version:");
		    	}
		    	if(current.compareTo(latest) == 0)
		    	{
		    		ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
		    		Updates.setUpdateInfo("false", null, null);
		    	}
		    	if(current.compareTo(latest) > 0)
		    	{
		    		ProxyServer.getInstance().getLogger().severe(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are using an unoffical version, please consider downloading an offical version from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
		    		Updates.setUpdateInfo("true", latest, " You are using an unoffical version, download an offical version here: https://www.spigotmc.org/resources/customprotocolsettings.69385/ Version:");
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
 	ProxyServer.getInstance().getLogger().warning(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't Check for Updates , Check Your Connection");
 	Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
    }
    if(latest != null)
    {
 	if(current.compareTo(latest) < 0) 
 	{
 	 ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " You are on: " + current);
 	 Updates.setUpdateInfo("true", latest, "New Version:");
 	}
 	if(current.compareTo(latest) == 0)
 	{
 		ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are Up to Date");
 		Updates.setUpdateInfo("false", null, null);
 	}
 	if(current.compareTo(latest) > 0)
 	{
 		ProxyServer.getInstance().getLogger().severe(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are using an unoffical version, please consider downloading an offical version from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
 		Updates.setUpdateInfo("true", latest, " You are using an unoffical version, download an offical version here: https://www.spigotmc.org/resources/customprotocolsettings.69385/ Version:");
 	}
   
	     }
         }
    }, 30 , 30 , TimeUnit.MINUTES);
	    }
	    if(Config.getconfig().getBoolean("update-checker-enabled") == false)
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
	pluginManager.registerListener(getInstance(), new DisconnectBlockedProtocols());
	}
	
  private void RegisterCommands()
  {
	  PluginManager pluginManager = getProxy().getPluginManager();
	  
	  pluginManager.registerCommand(getInstance(), new CPS());
	  pluginManager.registerCommand(getInstance(), new Ping());
  }
}
