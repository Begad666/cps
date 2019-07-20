package net.begad666.bc.plugin.customprotocolsettings;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.begad666.bc.plugin.customprotocolsettings.commands.CPS;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectBlockedProtocols;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.MetricsLite;
import net.begad666.bc.plugin.customprotocolsettings.utils.ProcessStrings;
import net.begad666.bc.plugin.customprotocolsettings.utils.Updates;

public class Main extends Plugin{
	private static Main instance;
	public void onEnable()
   {     
		instance = this;          
		Config.check("Load");    
	    getInstance().getLogger().info(Updates.getCurrentVersion() + " Is now enabled!");
	    RegisterListeners();
	    RegisterCommand();
	    MetricsLite metrics = new MetricsLite(getInstance());
	    Updates.setUpdateInfo("true", "", "Please Wait...");
	    if(Config.getconfig().getBoolean("update-checker-enabled") == true)
	    {
	    	ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable()
		       {
		 
		         public void run()
			    {
		       String current = Updates.getCurrentVersion();
		       String latest = Updates.getLatestVersion();
		       if (latest == null)
		       {
		    	ProxyServer.getInstance().getLogger().warning(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Couldn't Check for Updates , Check Your Connection");
		    	Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
		       }
		       if(latest != null)
		       {
		    	if(current.compareTo(latest) < 0) 
		    	{
		    	 ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " There is a new version: " + latest + " You are on: " + current);
		    	 Updates.setUpdateInfo("true", latest, "New Version:");
		    	}
		    	if(current.compareTo(latest) == 0)
		    	{
		    		ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " You are Up to Date");
		    		Updates.setUpdateInfo("false", null, null);
		    	}
		    	if(current.compareTo(latest) > 0)
		    	{
		    		ProxyServer.getInstance().getLogger().severe(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " You are using an unoffical version, please consider downloading an offical version from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
		    		Updates.setUpdateInfo("true", latest, " You are using an unoffical version, download an offical version here: https://www.spigotmc.org/resources/customprotocolsettings.69385/ Version:");
		    	}
		      
			     }
		            }
		       }, 4 , TimeUnit.SECONDS);
	    ProxyServer.getInstance().getScheduler().schedule(getInstance(), new Runnable()
    {

      public void run()
	    {
    String current = Updates.getCurrentVersion();
    String latest = Updates.getLatestVersion();
    if (latest == null)
    {
 	ProxyServer.getInstance().getLogger().warning(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " Couldn't Check for Updates , Check Your Connection");
 	Updates.setUpdateInfo("true", "", "Couldn't Check for Updates");
    }
    if(latest != null)
    {
 	if(current.compareTo(latest) < 0) 
 	{
 	 ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " There is a new version: " + latest + " You are on: " + current);
 	 Updates.setUpdateInfo("true", latest, "New Version:");
 	}
 	if(current.compareTo(latest) == 0)
 	{
 		ProxyServer.getInstance().getLogger().info(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " You are Up to Date");
 		Updates.setUpdateInfo("false", null, null);
 	}
 	if(current.compareTo(latest) > 0)
 	{
 		ProxyServer.getInstance().getLogger().severe(ProcessStrings.replacecodesandcolors(Config.getconfig().getString("prefix")) + " You are using an unoffical version, please consider downloading an offical version from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
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
	   
   }
	public void onDisable()
   {
	 getInstance().getLogger().info(Updates.getCurrentVersion() + " Is now disabled!");
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
	
  private void RegisterCommand()
  {
	  PluginManager pluginManager = getProxy().getPluginManager();
	  
	  pluginManager.registerCommand(getInstance(), new CPS());
  }
}
