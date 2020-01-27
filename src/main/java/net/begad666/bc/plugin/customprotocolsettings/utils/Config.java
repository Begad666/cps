package net.begad666.bc.plugin.customprotocolsettings.utils;

import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {
	private static File file;
	private static Configuration config;
	private static String cv = null;

	public static boolean check() {
		file = new File(Main.getInstance().getDataFolder(), "config.yml");

		if (!file.exists()) {
			return load();
		} else {
			try {
				cv = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml")).getString("config-version");
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().severe(" Cannot access the config version, cannot load the plugin");
				return false;
			}
			if (cv != null) {
				if (cv.compareTo(Updates.getCompileConfigVersion()) < 0) {
					ProxyServer.getInstance().getLogger().warning(" Config isn't up-to-date, config will load but some features might break");
					return load();
				} else if (cv.compareTo(Updates.getCompileConfigVersion()) == 0) {
					return load();
				} else if (cv.compareTo(Updates.getCompileConfigVersion()) > 0) {
					ProxyServer.getInstance().getLogger().severe(" You are using a version of a config which is higher than this version requires, cannot load the plugin");
					return false;
				}
			} else {
				ProxyServer.getInstance().getLogger().severe(" Cannot access the config version, cannot load the plugin");
				return false;
			}
		}
		return false;
	}

	private static boolean load() {
		if (!Main.getInstance().getDataFolder().exists()) {
			boolean result = Main.getInstance().getDataFolder().mkdir();
			if(!result) {
				Main.getInstance().getLogger().severe("Cannot make directory for data, cannot load the plugin");
				return false;
			}
		}

		file = new File(Main.getInstance().getDataFolder(), "config.yml");


		if (!file.exists()) {
			try (InputStream in = Main.getInstance().getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				Main.getInstance().getLogger().severe("Cannot copy config to data directory, cannot load the plugin");
				return false;
			}

		}
		if (file.exists()) {
			try {
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
				return true;
			} catch (IOException e) {
				Main.getInstance().getLogger().severe("Cannot load the config file, cannot load the plugin, Exception:\n" + e);
				return false;
			}
		}
		return false;
	}

	public static Configuration getconfig() {
		return config;
	}

}
