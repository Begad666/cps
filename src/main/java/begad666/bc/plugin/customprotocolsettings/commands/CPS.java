package begad666.bc.plugin.customprotocolsettings.commands;


import begad666.bc.plugin.customprotocolsettings.Main;
import begad666.bc.plugin.customprotocolsettings.features.ChangePingData;
import begad666.bc.plugin.customprotocolsettings.features.DisconnectNotAllowedUsers;
import begad666.bc.plugin.customprotocolsettings.features.MultiProxy;
import begad666.bc.plugin.customprotocolsettings.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Version: " + Main.updates.getCompileCurrentVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Version: " + Main.getInstance().getDescription().getVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Compile Config Version: " + Main.updates.getCompileConfigVersion()));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Config Version: " + Config.getconfig().getString("config-version")));
				sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Updates: " + Main.updates.getMessage()));
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
				case "help":
					help(sender);
					break;
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
				case "pull":
					if (Config.getconfig().getBoolean("multiproxy.enable")) {
						if (DatabaseConnectionManager.getConnected()) {
							pull(sender);
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
						}
					} else {
						sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
					}
					break;
				case "push":
					if (!(sender instanceof ProxiedPlayer)) {
						if (Config.getconfig().getBoolean("multiproxy.enable")) {
							if (DatabaseConnectionManager.getConnected()) {
								push(sender);
							} else {
								sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
							}
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
						}
					} else {
						if (Main.OnlineMode ? Config.getconfig().getStringList("allowed-players").contains(((ProxiedPlayer) sender).getUniqueId().toString()) : Config.getconfig().getStringList("allowed-players").contains(sender.getName())) {
							if (Config.getconfig().getBoolean("multiproxy.enable")) {
								if (DatabaseConnectionManager.getConnected()) {
									push(sender);
								} else {
									sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The Database Isn't Connected"));
								}
							} else {
								sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " MultiProxy Isn't Enabled"));
							}
						} else {
							sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " You can't use that command"));
						}
					}
					break;
				case "createbackup":
					if (!(sender instanceof ProxiedPlayer)) {
						createbackup(sender);
					} else {
						if (Main.OnlineMode ? Config.getconfig().getStringList("allowed-players").contains(((ProxiedPlayer) sender).getUniqueId().toString()) : Config.getconfig().getStringList("allowed-players").contains(sender.getName())) {
							createbackup(sender);
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

	private static void help(CommandSender sender) {
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Help "));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps help - Shows this help"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps reload - Reloads the config from the file without restarting BungeeCord"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps license - Sends you the license"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps enable - Enables the listeners and /ping command  (also requires to be in allowed-players in config or be a console)"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps disable - Opposite of the above with the same additional requirement"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps checkdbconnection - Sends if its connected to the database or not"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps pull - Pulls config data from the database and processes it"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps push - Pushes current config data to the database"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps createbackup - Creates a config backup"));
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " /cps loadbackup - Coming soon!"));
	}

	private static void reload(CommandSender sender) {
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

	private static void license(CommandSender sender) {
		InputStream inputStream = Main.class.getResourceAsStream("/license.txt");
		StringBuilder license = new StringBuilder();
		int character;
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
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The features is already enabled!"));
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Enabling plugin features..."));
			PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
			pluginManager.registerListener(Main.getInstance(), new ChangePingData());
			pluginManager.registerListener(Main.getInstance(), new DisconnectNotAllowedUsers());
			pluginManager.registerCommand(Main.getInstance(), new Ping());
			if (Config.getconfig().getBoolean("multiproxy.enable")) {
				Main.getInstance().getLogger().info("Connecting to database...");
				DatabaseConnectionManager.connect();
				ScheduledTasks.autoreconnecttask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
					Main.getInstance().getLogger().info("Reconnecting to database...");
					DatabaseConnectionManager.disconnect();
					DatabaseConnectionManager.connect();
				}, 8, 8, TimeUnit.HOURS);
				if (Config.getconfig().getBoolean("multiproxy.autopull"))
					ScheduledTasks.autopulltask = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
						ResultSet data;
						Gson gson = new Gson();
						JsonObject configjson = new JsonObject();
						try {
							data = DatabaseConnectionManager.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Config.getconfig().getString("multiproxy.groupid") + "'");
							while (data != null && data.next())
								configjson = gson.fromJson(data.getString("configjson"), JsonObject.class);
						} catch (JsonSyntaxException e) {
							Main.getInstance().getLogger().severe("Error while processing pulled json data from database, most likely because there is no data, check your table, no changes will be made");
							return;
						} catch (SQLException e) {
							Main.getInstance().getLogger().severe("Error while pulling data from database, no changes will be made");
							return;
						}
						if (configjson != null)
							MultiProxy.ApplyData(configjson, Config.getconfig().getBoolean("multiproxy.autosaveconfig"));
						else
							Main.getInstance().getLogger().warning("No data was found in the database");
					}, Config.getconfig().getInt("multiproxy.autopulltime"), Config.getconfig().getInt("multiproxy.autopulltime"), TimeUnit.MINUTES);
			}
			isEnabled = true;
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The plugin features have been enabled"));
		}
	}

	private void disable(CommandSender sender) {
		if (!CPS.isEnabled) {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The features is already disabled!"));
		} else {
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Disabling plugin features..."));
			PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
			pluginManager.unregisterListeners(Main.getInstance());
			pluginManager.unregisterCommand(new Ping());
			if (Config.getconfig().getBoolean("multiproxy.enable")) {
				Main.getInstance().getLogger().info("Disconnecting From Database...");
				DatabaseConnectionManager.disconnect();
			}
			isEnabled = false;
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " The plugin features have been disabled"));
		}
	}

	private void checkdbconnection(CommandSender sender) {
		if (!DatabaseConnectionManager.getConnected())
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Not Connected"));
		else
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Connected"));
	}

	private void pull(CommandSender sender) {
		ResultSet data;
		Gson gson = new Gson();
		JsonObject configjson = null;
		try {
			data = DatabaseConnectionManager.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Config.getconfig().getString("multiproxy.groupid") + "'");
			while (data != null && data.next())
				configjson = gson.fromJson(data.getString("configjson"), JsonObject.class);
		} catch (JsonSyntaxException e) {
			Main.getInstance().getLogger().severe(" Error while processing pulled json data from database, no changes will be made");
			sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Error while processing pulled json data from database, no changes will be made")));
			return;
		} catch (SQLException e) {
			Main.getInstance().getLogger().severe(" Error while pulling data from database, no changes will be made");
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
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin") + " Check the console to know did it work or not, starting process...")));
		MultiProxy.PushData();
	}

	private void createbackup(CommandSender sender) {
		sender.sendMessage(new TextComponent(MainUtils.replacecodesandcolors(Config.getconfig().getString("prefixs.plugin")) + " Creating backup..."));
		Main.getInstance().getLogger().info("Creating backup...");
		Calendar cal = Calendar.getInstance();
		int result = Backup.SaveBackup(Config.getconfig().getString("backup.name").replace("{time}", new SimpleDateFormat("hh_mma").format(cal.getTime())).replace("{date}", new SimpleDateFormat("yyyy_MM_dd").format(cal.getTime())));
		switch (result) {
			case 0:
				Main.getInstance().getLogger().info("Done backing up config");
				sender.sendMessage(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("prefixs.plugin") + " Done backing up config")));
				break;
			case 1:
				Main.getInstance().getLogger().severe("Cannot create directory for backups, canceling backup");
				sender.sendMessage(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("prefixs.plugin") + " Cannot create directory for backups, canceling backup")));
				break;
			case 2:
				Main.getInstance().getLogger().severe("Cannot write to the backup file, canceling backup");
				sender.sendMessage(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("prefixs.plugin") + " Cannot write to the backup file, canceling backup")));
				break;
			case 3:
				Main.getInstance().getLogger().severe("Cannot find the config file, is the file there?, canceling backup");
				sender.sendMessage(new TextComponent(MainUtils.replaceall(Config.getconfig().getString("prefixs.plugin") + " Cannot find the config file, is the file there?, canceling backup")));
				break;
		}
	}

}
