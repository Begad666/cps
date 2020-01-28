package net.begad666.bc.plugin.customprotocolsettings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.begad666.bc.plugin.customprotocolsettings.commands.CPS;
import net.begad666.bc.plugin.customprotocolsettings.commands.Ping;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import net.begad666.bc.plugin.customprotocolsettings.features.MultiProxy;
import net.begad666.bc.plugin.customprotocolsettings.utils.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
	private static Main instance;
	public static boolean OnlineMode;

	public void onEnable() {
		instance = this;
		getInstance().getLogger().info("Started Enable Process");
		getInstance().getLogger().info("Loading Config...");
		boolean result = Config.check();
		if (!result) {
			return;
		}
		getInstance().getLogger().info("Registering Commands...");
		RegisterCommands();
		getInstance().getLogger().info("Registering Listeners...");
		RegisterListeners();
		if (Config.getconfig().getBoolean("multiproxy.enable")) {
			getInstance().getLogger().info("Connecting To Database...");
			ProxyServer.getInstance().getScheduler().runAsync(getInstance(), () -> {
				DatabaseConnectionManager.connect();
				DatabaseConnectionManager.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `cps` "
								+ "( `groupId` VARCHAR(25) NOT NULL ,"
								+ " `configjson` LONGTEXT NOT NULL ,"
								+ "	PRIMARY KEY (`groupId`)"
								+ ")");
			});

			ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Reconnecting to database...");
				DatabaseConnectionManager.disconnect();
				DatabaseConnectionManager.connect();
			}, 8, 8, TimeUnit.HOURS);
			if (Config.getconfig().getBoolean("multiproxy.autopull") && Config.getconfig().getBoolean("multiproxy.enable"))
				ScheduledTasks.autopulltask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
					ResultSet data;
					Gson gson = new Gson();
					JsonObject configjson = new JsonObject();
					try {
						data = DatabaseConnectionManager.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Config.getconfig().getString("multiproxy.groupid") + "'");
						while (data != null && data.next())
							configjson = gson.fromJson(data.getString("configjson"), JsonObject.class);
					} catch (JsonSyntaxException e) {
						ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while processing pulled json data from database, most likely because there is no data, check your table, no changes will be made"));
						return;
					} catch (SQLException e) {
						ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while pulling data from database, no changes will be made"));
						return;
					}
					if (configjson != null)
						MultiProxy.ApplyData(configjson, Config.getconfig().getBoolean("multiproxy.autosaveconfig"));
					else
						getInstance().getLogger().warning("No data was found in the database");
				}, Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
		}
		MetricsLite metrics = new MetricsLite(getInstance(), 5145);
		Updates.setMessage("Please wait...");
		if (Config.getconfig().getBoolean("update-checker-enabled")) {
			ScheduledTasks.updatetask1 = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				String current = Updates.getCompileCurrentVersion();
				String latest = Updates.getLatestVersion();
				if (latest == null) {
					ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't check for updates, check Your Connection");
					Updates.setMessage("Couldn't check for updates");
				}
				if (latest != null) {
					if (current.compareTo(latest) < 0) {
						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " you are on: " + current);
						Updates.setMessage("New version is out: " + latest);
					}
					if (current.compareTo(latest) == 0) {
						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are up to date");
						Updates.setMessage("You are up to date");
					}
					if (current.compareTo(latest) > 0) {
						if (getInstance().getDescription().getVersion().compareTo(Updates.getCompileCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(Updates.getCompileCurrentVersion()) < 0) {
							ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
							Updates.setMessage("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is " + latest);
						} else {
							ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are up to date");
							Updates.setMessage("You are up to date");
						}
					}
				}
			}, 4, TimeUnit.SECONDS);
			ScheduledTasks.updatetask2 = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				String current = Updates.getCompileCurrentVersion();
				String latest = Updates.getLatestVersion();
				if (latest == null) {
					ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Couldn't check for updates, check Your Connection");
					Updates.setMessage("Couldn't check for updates");
				}
				if (latest != null) {
					if (current.compareTo(latest) < 0) {
						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " There is a new version: " + latest + " you are on: " + current);
						Updates.setMessage("New version is out: " + latest);
					}
					if (current.compareTo(latest) == 0) {
						ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are up to date");
						Updates.setMessage("You are up to date");
					}
					if (current.compareTo(latest) > 0) {
						if (getInstance().getDescription().getVersion().compareTo(Updates.getCompileCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(Updates.getCompileCurrentVersion()) < 0) {
							ProxyServer.getInstance().getLogger().warning(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
							Updates.setMessage("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is " + latest);
						} else {
							ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You are up to date");
							Updates.setMessage("You are up to date");
						}
					}
				}
			}, 30, 30, TimeUnit.MINUTES);
		} else {
			Updates.setMessage("Updates Are disabled");
		}
		CPS.isEnabled = true;
		Main.OnlineMode = getInstance().getProxy().getConfig().isOnlineMode();
	}

	public void onDisable() {
		PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
		getInstance().getLogger().info("Started Disable Process");
		getInstance().getLogger().info("Unregistering Commands...");
		pluginmanager.unregisterCommands(Main.getInstance());
		getInstance().getLogger().info("Unregistering Listeners...");
		pluginmanager.unregisterListeners(Main.getInstance());
		if (Config.getconfig().getBoolean("multiproxy.enable")) {
			getInstance().getLogger().info("Disconnecting From Database...");
			DatabaseConnectionManager.disconnect();
		}
		getInstance().getLogger().info("Canceling Scheduled Tasks...");
		ProxyServer.getInstance().getScheduler().cancel(getInstance());
		getInstance().getLogger().info(getInstance().getDescription().getVersion() + " Is now disabled!");
		CPS.isEnabled = false;
	}

	public static Main getInstance() {
		return instance;
	}

	private void RegisterListeners() {
		PluginManager pluginManager = getProxy().getPluginManager();

		pluginManager.registerListener(getInstance(), new ChangePingData());
		pluginManager.registerListener(getInstance(), new DisconnectNotAllowedUsers());
	}

	private void RegisterCommands() {
		PluginManager pluginManager = getProxy().getPluginManager();

		pluginManager.registerCommand(getInstance(), new CPS());
		pluginManager.registerCommand(getInstance(), new Ping());
	}
}
