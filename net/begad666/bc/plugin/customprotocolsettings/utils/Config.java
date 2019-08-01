package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.begad666.bc.plugin.customprotocolsettings.Main;

public class Config {
	private static File file;
	private static Configuration config;
	private static String cv;
	public static void check(String method)
	{
	file = new File(Main.getInstance().getDataFolder(), "config.yml");

		   
	if (!file.exists()) 
	{
	load();
	}
	if (file.exists())
	{
        try {
        	config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
        	cv = config.getString("config-version");
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
        if (config.getString("config-version") != null)
        {
        if (cv.compareTo(Updates.getConfigVersion()) < 0)
        {
        ProxyServer.getInstance().getLogger().warning("CustomProtocolSettings: You must delete the config then set it up again to refresh the config");
        ScheduledTasks.configversionerrorremindertask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {public void run() {ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: You must delete the config then set it up again to refresh the config");}}, 10,10, TimeUnit.SECONDS);
        }
        if (cv.compareTo(Updates.getConfigVersion()) == 0)
        {
        if(method == "Load")
        load();
        if(method == "Reload")
        reload();
        }
        if (cv.compareTo(Updates.getConfigVersion()) > 0)
        {
        ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: THE CONFIG VERSION IS HIGHER THAN THE PLUGIN VERSION , PLUGIN IS GOING TO BE HANG");
   	    PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
   	    pluginmanager.unregisterCommands(Main.getInstance());
   	    pluginmanager.unregisterListeners(Main.getInstance());
   	    ProxyServer.getInstance().getScheduler().cancel(ScheduledTasks.updatetask2);
   	    ScheduledTasks.configversionerrorremindertask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {public void run() {ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: The Plugin is HANGED because the Config version is Higher Than The Plugin version");}}, 10,10, TimeUnit.SECONDS);
        }
        if (config.getString("config-version") == null)
        {
        ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: THE CONFIG VERSION IS NOT AVAILABLE NOW ,PLEASE DELETE THE CONFIG THEN RESTART THE SERVER, PLUGIN IS GOING TO BE HANG");
   	    PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
   	    pluginmanager.unregisterCommands(Main.getInstance());
   	    pluginmanager.unregisterListeners(Main.getInstance());
   	    ProxyServer.getInstance().getScheduler().cancel(ScheduledTasks.updatetask2);
   	    ScheduledTasks.configversionerrorremindertask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {public void run() {ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: The Plugin is HANGED because the Config version is not accessible, if this an update from v1.1 or v1.0 (Was Named Final) add config-version: 'v1' to the config file");}}, 10,10, TimeUnit.SECONDS);
        }
        
        }
    }
    
	}
	
	private static void load()
	{
		 if (!Main.getInstance().getDataFolder().exists())
			 Main.getInstance().getDataFolder().mkdir();

	        file = new File(Main.getInstance().getDataFolder(), "config.yml");

	   
	        if (!file.exists()) {
	            try (InputStream in = Main.getInstance().getResourceAsStream("config.yml")) {
	                Files.copy(in, file.toPath());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        }
	        if (file.exists())
	        {
	            try {
	            	config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
	            } catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        
	        
	     
	       

	}
	
	 private static void reload()
	 {   
		 file = null;
		 config = null;
		 if (!Main.getInstance().getDataFolder().exists())
			 Main.getInstance().getDataFolder().mkdir();

	        file = new File(Main.getInstance().getDataFolder(), "config.yml");

	   
	        if (!file.exists()) {
	            try (InputStream in = Main.getInstance().getResourceAsStream("config.yml")) {
	                Files.copy(in, file.toPath());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        }
	        if(file.exists())
	        {
	            try {
	            	config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
	            } catch (IOException e) {
					e.printStackTrace();
				}
	        }
	 }
	 public static Configuration getconfig()
	 {
     return config;
	 }

}
