package net.begad666.bc.plugin.customprotocolsettings.commands;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import net.begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import net.begad666.bc.plugin.customprotocolsettings.features.MultiProxy;
import net.begad666.bc.plugin.customprotocolsettings.utils.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class CPS extends Command {
	public static boolean isEnabled;

	public CPS() {
		super("cps", "cps.admin");
	}

	public void execute(CommandSender sender, final String[] args) {
		if (args.length != 1) {
			if (args.length != 2) {
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ------------------------------"));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Version: " + Updates.getCompileCurrentVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Version: " + Main.getInstance().getDescription().getVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Config Version: " + Updates.getCompileConfigVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config Version: " + Config.getconfig().getString("config-version")));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Updates: " + Updates.getMessage()));
				TextComponent pluglink = new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Plugin Page: Click Here");
				pluglink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/customprotocolsettings.69385/"));
				sender.sendMessage(pluglink);
				TextComponent devlink = new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Developer: Click Here");
				devlink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/members/begad.478174/"));
				sender.sendMessage(devlink);
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " ------------------------------"));
			}
		} else {
			switch (args[0].toLowerCase()) {
				case "reload":
					reload(sender);
					break;
				case "ps":
				case "ms":
				case "pf":
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " That command was deleted in v2.0, sorry for the inconvenience"));
					break;
				case "license":
					license(sender);
					break;
				case "enable":
					if (!(sender instanceof ProxiedPlayer)) {
						enable(sender);
					} else {
						if (Main.OnlineMode ? Config.getconfig().getStringList("allowed-players").contains(((ProxiedPlayer) sender).getUniqueId().toString()) : Config.getconfig().getStringList("allowed-players").contains(sender.getName())) {
							enable(sender);
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You can't use that command"));
						}
					}
					break;
				case "disable":
					if (!(sender instanceof ProxiedPlayer)) {
						disable(sender);
					} else {
						if (Main.OnlineMode ? Config.getconfig().getStringList("allowed-players").contains(((ProxiedPlayer) sender).getUniqueId().toString()) : Config.getconfig().getStringList("allowed-players").contains(sender.getName())) {
							disable(sender);
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You can't use that command"));
						}
					}
					break;
				case "checkdbconnection":
					checkdbconnection(sender);
					break;
				case "pullnow":
					if (Config.getconfig().getBoolean("multiproxy.enable")) {
						if (DatabaseConnectionManager.getConnected()) {
							pullnow(sender);
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
						}
					} else {
						sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
					}
					break;
				case "push":
					if (!(sender instanceof ProxiedPlayer)) {
						push(sender);
					} else {
						if (Main.OnlineMode ? Config.getconfig().getStringList("allowed-players").contains(((ProxiedPlayer) sender).getUniqueId().toString()) : Config.getconfig().getStringList("allowed-players").contains(sender.getName())) {
							push(sender);
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You can't use that command"));
						}
					}
					break;
				default:
					sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Unknown argument"));
					break;
			}

		}
	}

	public static void reload(CommandSender sender) {
		boolean result = Config.check();
		if (!result) {
			PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
			pluginManager.unregisterListeners(Main.getInstance());
			pluginManager.unregisterCommands(Main.getInstance());
			return;
		}
		if (Config.getconfig().getBoolean("multiproxy.enable")) {
			Main.getInstance().getLogger().info("Disconnecting From Database...");
			DatabaseConnectionManager.disconnect();
			Main.getInstance().getLogger().info("Reconnecting to Database...");
			DatabaseConnectionManager.connect();
		}
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config reload process finished!"));
	}

	public static void license(CommandSender sender) {
		InputStream inputStream = Main.class.getResourceAsStream("/license.txt");
		StringBuilder license = new StringBuilder();
		int character = 0;
		try {
			while ((character = inputStream.read()) != -1) {
				license.append((char) character);
			}
		} catch (IOException e) {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Cannot read license file"));
			return;
		}
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + "\n" + license));
	}

	private void enable(CommandSender sender) {
		if (CPS.isEnabled) {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already enabled!"));
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Enabling Plugin Features..."));
			PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
			pluginManager.registerListener(Main.getInstance(), new ChangePingData());
			pluginManager.registerListener(Main.getInstance(), new DisconnectNotAllowedUsers());
			pluginManager.registerCommand(Main.getInstance(), new Ping());
			if (Config.getconfig().getBoolean("multiproxy.enable")) {
				Main.getInstance().getLogger().info("Connecting To Database...");
				DatabaseConnectionManager.connect();
				ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
					ProxyServer.getInstance().getLogger().info(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Reconnecting to database...");
					DatabaseConnectionManager.disconnect();
					DatabaseConnectionManager.connect();
				}, 8, 8, TimeUnit.HOURS);
				if (Config.getconfig().getBoolean("multiproxy.autopull"))
					ScheduledTasks.autopulltask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {

					}, Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
			}
			isEnabled = true;
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Enabled"));
		}
	}

	private void disable(CommandSender sender) {
		if (!CPS.isEnabled) {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Features is already disabled!"));
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Disabling Plugin Features..."));
			PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
			pluginManager.unregisterListeners(Main.getInstance());
			pluginManager.unregisterCommand(new Ping());
			if (Config.getconfig().getBoolean("multiproxy.enable")) {
				Main.getInstance().getLogger().info("Disconnecting From Database...");
				DatabaseConnectionManager.disconnect();
			}
			isEnabled = false;
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Plugin Features Has been Disabled"));
		}
	}

	private void checkdbconnection(CommandSender sender) {
		if (!DatabaseConnectionManager.getConnected())
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Not Connected"));
		else
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Connected"));
	}

	private void pullnow(CommandSender sender) {
		ResultSet data;
		Gson gson = new Gson();
		JsonObject configjson = null;
		try {
			data = DatabaseConnectionManager.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Config.getconfig().getString("multiproxy.groupid") + "'");
			while (data != null && data.next())
				configjson = gson.fromJson(data.getString("configjson"), JsonObject.class);
		} catch (JsonSyntaxException e) {
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while processing pulled json data from database, no changes will be made"));
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while processing pulled json data from database, no changes will be made")));
			return;
		} catch (SQLException e) {
			ProxyServer.getInstance().getLogger().severe(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while pulling data from database, no changes will be made"));
			return;
		}
		if (configjson != null) {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Check the console to know did it work or not, starting process...")));
			MultiProxy.ApplyData(configjson, Config.getconfig().getBoolean("multiproxy.autosaveconfig"));
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " No data was found in the database"));
		}
	}
	private void push(CommandSender sender) {
		if (Config.getconfig().getBoolean("multiproxy.enable")) {
			if (DatabaseConnectionManager.getConnected()) {
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Check the console to know did it work or not, starting process...")));
				MultiProxy.PushData();
			} else {
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
			}
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
		}
	}

}
