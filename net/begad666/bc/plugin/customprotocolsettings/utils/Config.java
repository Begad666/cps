package net.begad666.bc.plugin.customprotocolsettings.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
	private static File file;
	private static Configuration config;
	private static String cv = null;
	public static boolean check()
	{
		file = new File(Main.getInstance().getDataFolder(), "config.yml");

		if (!file.exists()) 
		{
			load();
			return true;
		}
		else
		{
			try 
			{
				cv = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml")).getString("config-version");
        	} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			if (cv != null)
			{
				if (cv.compareTo(Updates.getConfigVersion()) < 0)
				{
					ProxyServer.getInstance().getLogger().warning("CustomProtocolSettings: Config isn't up-to-date, config will load but some features will break");
					load();
					return true;
				}
				else if (cv.compareTo(Updates.getConfigVersion()) == 0)
				{
					load();
					return true;
				}
				else if(cv.compareTo(Updates.getConfigVersion()) > 0)
				{
					ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: You are using a version of a config which is higher than this version requires, cannot load the plugin");
					return false;
				}
			}
			else if (cv == null)
			{
				ProxyServer.getInstance().getLogger().severe("CustomProtocolSettings: Cannot access the config version, cannot load the plugin");
				return false;
			}
		}
		return false;
	}
	
	private static void load()
	{
		 if (!Main.getInstance().getDataFolder().exists())
			 Main.getInstance().getDataFolder().mkdir();

	        file = new File(Main.getInstance().getDataFolder(), "config.yml");

	   
	        if (!file.exists()) 
	        {
	            try (InputStream in = Main.getInstance().getResourceAsStream("config.yml")) 
	            {
	                Files.copy(in, file.toPath());
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }

	        }
	        if (file.exists())
	        {
	            try 
	            {
	            	config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
	            } 
	            catch (IOException e) 
	            {
					e.printStackTrace();
				}
	        }
	}	
	public static Configuration getconfig()
	{
		return config;
	}

}
