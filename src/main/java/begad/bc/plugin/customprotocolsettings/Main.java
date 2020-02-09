package begad.bc.plugin.customprotocolsettings;

import begad.bc.plugin.customprotocolsettings.commands.CPS;
import begad.bc.plugin.customprotocolsettings.commands.Ping;
import begad.bc.plugin.customprotocolsettings.features.ChangePingData;
import begad.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import begad.bc.plugin.customprotocolsettings.features.MultiProxy;
import begad.bc.plugin.customprotocolsettings.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
	private static Main instance;
	public static Updates updates;
	public static boolean OnlineMode;

	public void onEnable() {
		instance = this;
		updates = new Updates(getInstance(), "CustomProtocolSettings", "69385", "v6", "v2.0.1");
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
				if (DatabaseConnectionManager.getConnected())
					DatabaseConnectionManager.executeUpdate(
							"CREATE TABLE IF NOT EXISTS `cps` "
									+ "( `groupId` VARCHAR(25) NOT NULL ,"
									+ " `configjson` LONGTEXT NOT NULL ,"
									+ "	PRIMARY KEY (`groupId`)"
									+ ")");
			});

			ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				getInstance().getLogger().info("Reconnecting to database...");
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
						getInstance().getLogger().severe("Error while processing pulled json data from database, most likely because there is no data, check your table, no changes will be made");
						return;
					} catch (SQLException e) {
						getInstance().getLogger().severe(MainUtils.replacecodesandcolors("Error while pulling data from database, no changes will be made"));
						return;
					}
					if (configjson != null)
						MultiProxy.ApplyData(configjson, Config.getconfig().getBoolean("multiproxy.autosaveconfig"));
					else
						getInstance().getLogger().warning("No data was found in the database");
				}, Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
		}
		MetricsLite metrics = new MetricsLite(getInstance(), 5145);
		updates.setMessage("Please wait...");
		if (Config.getconfig().getBoolean("update-checker-enabled")) {
			ScheduledTasks.updatetask1 = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				String current = getInstance().getDescription().getVersion();
				String latest = updates.getLatestVersion();
				if (latest == null) {
					getInstance().getLogger().warning("Couldn't check for updates, check your connection");
					updates.setMessage("Couldn't check for updates");
				} else {
					if (current.compareTo(latest) < 0) {
						getInstance().getLogger().info("There is a new version: " + latest + ", you are on: " + current);
						updates.setMessage("New version: " + latest);
					} else if (current.compareTo(latest) == 0) {
						getInstance().getLogger().info("You are up to date");
						updates.setMessage("You are up to date");
					} else if (current.compareTo(latest) > 0) {
						if (getInstance().getDescription().getVersion().compareTo(updates.getCompileCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(updates.getCompileCurrentVersion()) < 0) {
							getInstance().getLogger().warning("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
							updates.setMessage("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is " + latest);
						} else {
							getInstance().getLogger().info("You are up to date");
							updates.setMessage("You are up to date");
						}
					}
				}
			}, 4, TimeUnit.SECONDS);
			ScheduledTasks.updatetask2 = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
				String current = updates.getCompileCurrentVersion();
				String latest = updates.getLatestVersion();
				if (latest == null) {
					getInstance().getLogger().warning("Couldn't check for updates, check your connection");
					updates.setMessage("Couldn't check for updates");
				}
				if (latest != null) {
					if (current.compareTo(latest) < 0) {
						getInstance().getLogger().info("There is a new version: " + latest + " you are on: " + current);
						updates.setMessage("New version is out: " + latest);
					}
					if (current.compareTo(latest) == 0) {
						getInstance().getLogger().info("You are up to date");
						updates.setMessage("You are up to date");
					}
					if (current.compareTo(latest) > 0) {
						if (getInstance().getDescription().getVersion().compareTo(updates.getCompileCurrentVersion()) > 0 || getInstance().getDescription().getVersion().compareTo(updates.getCompileCurrentVersion()) < 0) {
							getInstance().getLogger().warning("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/");
							updates.setMessage("Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/, Latest version is " + latest);
						} else {
							getInstance().getLogger().info("You are up to date");
							updates.setMessage("You are up to date");
						}
					}
				}
			}, 30, 30, TimeUnit.MINUTES);
		} else {
			updates.setMessage("Updates are disabled");
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
