package begad.bc.plugin.customprotocolsettings.features;

import begad.bc.plugin.customprotocolsettings.Main;
import begad.bc.plugin.customprotocolsettings.utils.Backup;
import begad.bc.plugin.customprotocolsettings.utils.Config;
import begad.bc.plugin.customprotocolsettings.utils.DatabaseConnectionManager;
import begad.bc.plugin.customprotocolsettings.utils.MainUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MultiProxy {

    public static void ApplyData(JsonObject configobject, boolean autosaveconfig) {
	    Main.getInstance().getLogger().info("Pull data ready, starting config changes...");
	    try {
		    if (Config.getconfig().getBoolean("multiproxy.backup.enable")) {
			    Main.getInstance().getLogger().info("Backing up config...");
			    Calendar cal = Calendar.getInstance();
			    int result = Backup.SaveBackup(Config.getconfig().getString("backup.name").replace("{time}", new SimpleDateFormat("hh_mma").format(cal.getTime())).replace("{date}", new SimpleDateFormat("yyyy_MM_dd").format(cal.getTime())));
			    switch (result) {
				    case 0:
					    Main.getInstance().getLogger().info("Done backing up config");
					    break;
				    case 1:
					    if (Config.getconfig().getBoolean("multiproxy.backup.stoponerror")) {
						    Main.getInstance().getLogger().severe("Cannot create directory for backups, canceling pulling");
						    return;
					    } else {
						    Main.getInstance().getLogger().severe("Cannot create directory for backups, canceling backup");
					    }
					    break;
				    case 2:
					    if (Config.getconfig().getBoolean("multiproxy.backup.stoponerror")) {
						    Main.getInstance().getLogger().severe("Cannot write to the backup file, canceling pulling");
						    return;
					    } else {
						    Main.getInstance().getLogger().severe("Cannot write to the backup file, canceling backup");
					    }
					    break;
				    case 3:
					    Main.getInstance().getLogger().severe("Cannot find the config file, is the file there?, canceling pulling");
					    return;
			    }
		    }
		    if (!Config.getconfig().getBoolean("multiproxy.autoreplace")) {
			    Config.getconfig().set("update-checker-enabled", configobject.get("update-checker-enabled").getAsBoolean());
			    JsonObject netinfo = configobject.get("network-info").getAsJsonObject();
			    Config.getconfig().set("network-info.name", netinfo.get("name").getAsString());
			    Config.getconfig().set("network-info.max-players", netinfo.get("max-players").getAsInt());
			    JsonObject settings = configobject.get("settings").getAsJsonObject();
			    ArrayList<Integer> allowed_protocols = new ArrayList<>();
			    JsonArray ap = settings.get("allowed-protocols").getAsJsonArray();
			    for (int i = 0; i < ap.size(); i++) {
				    allowed_protocols.add(ap.get(i).getAsInt());
			    }
			    Config.getconfig().set("settings.allowed-protocols", allowed_protocols);
			    Config.getconfig().set("settings.maintenance-enabled", settings.get("maintenance-enabled").getAsBoolean());
			    JsonObject motds = configobject.get("motds").getAsJsonObject();
			    JsonObject defaultmotd = motds.get("default-motd").getAsJsonObject();
			    Config.getconfig().set("motds.default-motd.1", defaultmotd.get("1").getAsString());
			    Config.getconfig().set("motds.default-motd.2", defaultmotd.get("2").getAsString());
			    JsonObject maintenancemotd = motds.get("maintenance-motd").getAsJsonObject();
			    Config.getconfig().set("motds.maintenance-motd.1", maintenancemotd.get("1").getAsString());
			    Config.getconfig().set("motds.maintenance-motd.2", maintenancemotd.get("2").getAsString());
			    JsonObject hovermessages = configobject.get("hover-messages").getAsJsonObject();
			    ArrayList<String> defaulthovermessage = new ArrayList<>();
			    JsonArray dhm = hovermessages.get("default-hover-message").getAsJsonArray();
			    for (int i = 0; i < dhm.size(); i++) {
				    defaulthovermessage.add(dhm.get(i).getAsString());
			    }
			    Config.getconfig().set("hover-messages.default-hover-message", defaulthovermessage);
			    ArrayList<String> maintenancehovermessage = new ArrayList<>();
			    JsonArray mhm = hovermessages.get("maintenance-hover-message").getAsJsonArray();
			    for (int i = 0; i < mhm.size(); i++) {
				    maintenancehovermessage.add(mhm.get(i).getAsString());
			    }
			    Config.getconfig().set("hover-messages.maintenance-hover-message", maintenancehovermessage);
			    JsonObject prefixs = configobject.get("prefixs").getAsJsonObject();
			    Config.getconfig().set("prefixs.plugin", prefixs.get("plugin").getAsString());
			    Config.getconfig().set("prefixs.ping", prefixs.get("ping").getAsString());
			    JsonObject messages = configobject.get("messages").getAsJsonObject();
			    Config.getconfig().set("messages.not-supported-client-message", messages.get("not-supported-client-message").getAsString());
			    Config.getconfig().set("messages.maintenance-message", messages.get("maintenance-message").getAsString());
			    Config.getconfig().set("messages.full-message", messages.get("full-message").getAsString());
			    ArrayList<String> allowed_players = new ArrayList<>();
			    JsonArray aps = configobject.get("allowed-players").getAsJsonArray();
			    for (int i = 0; i < aps.size(); i++) {
				    allowed_players.add(aps.get(i).getAsString());
			    }
			    Config.getconfig().set("allowed-players", allowed_players);
		    } else {
			    Config.getconfig().set("update-checker-enabled", configobject.get("update-checker-enabled").getAsBoolean());
			    JsonObject netinfo = configobject.get("network-info").getAsJsonObject();
			    Config.getconfig().set("network-info.name", MainUtils.replaceunicode(netinfo.get("name").getAsString()));
			    Config.getconfig().set("network-info.max-players", netinfo.get("max-players").getAsInt());
			    JsonObject settings = configobject.get("settings").getAsJsonObject();
			    ArrayList<Integer> allowed_protocols = new ArrayList<>();
			    JsonArray ap = settings.get("allowed-protocols").getAsJsonArray();
			    for (int i = 0; i < ap.size(); i++) {
				    allowed_protocols.add(ap.get(i).getAsInt());
			    }
			    Config.getconfig().set("settings.allowed-protocols", allowed_protocols);
			    Config.getconfig().set("settings.maintenance-enabled", settings.get("maintenance-enabled").getAsBoolean());
			    JsonObject motds = configobject.get("motds").getAsJsonObject();
			    JsonObject defaultmotd = motds.get("default-motd").getAsJsonObject();
			    Config.getconfig().set("motds.default-motd.1", MainUtils.replaceunicode(defaultmotd.get("1").getAsString()));
			    Config.getconfig().set("motds.default-motd.2", MainUtils.replaceunicode(defaultmotd.get("2").getAsString()));
			    JsonObject maintenancemotd = motds.get("maintenance-motd").getAsJsonObject();
			    Config.getconfig().set("motds.maintenance-motd.1", MainUtils.replaceunicode(maintenancemotd.get("1").getAsString()));
			    Config.getconfig().set("motds.maintenance-motd.2", MainUtils.replaceunicode(maintenancemotd.get("2").getAsString()));
			    JsonObject hovermessages = configobject.get("hover-messages").getAsJsonObject();
			    ArrayList<String> defaulthovermessage = new ArrayList<>();
			    JsonArray dhm = hovermessages.get("default-hover-message").getAsJsonArray();
			    for (int i = 0; i < dhm.size(); i++) {
				    defaulthovermessage.add(MainUtils.replaceunicode(dhm.get(i).getAsString()));
			    }
			    Config.getconfig().set("hover-messages.default-hover-message", defaulthovermessage);
			    ArrayList<String> maintenancehovermessage = new ArrayList<>();
			    JsonArray mhm = hovermessages.get("maintenance-hover-message").getAsJsonArray();
			    for (int i = 0; i < mhm.size(); i++) {
				    maintenancehovermessage.add(MainUtils.replaceunicode(mhm.get(i).getAsString()));
			    }
			    Config.getconfig().set("hover-messages.maintenance-hover-message", maintenancehovermessage);
			    JsonObject prefixs = configobject.get("prefixs").getAsJsonObject();
			    Config.getconfig().set("prefixs.plugin", MainUtils.replaceunicode(prefixs.get("plugin").getAsString()));
			    Config.getconfig().set("prefixs.ping", MainUtils.replaceunicode(prefixs.get("ping").getAsString()));
			    JsonObject messages = configobject.get("messages").getAsJsonObject();
			    Config.getconfig().set("messages.not-supported-client-message", MainUtils.replaceunicode(messages.get("not-supported-client-message").getAsString()));
			    Config.getconfig().set("messages.maintenance-message", MainUtils.replaceunicode(messages.get("maintenance-message").getAsString()));
			    Config.getconfig().set("messages.full-message", MainUtils.replaceunicode(messages.get("full-message").getAsString()));
			    ArrayList<String> allowed_players = new ArrayList<>();
			    JsonArray aps = configobject.get("allowed-players").getAsJsonArray();
			    for (int i = 0; i < aps.size(); i++) {
				    allowed_players.add(aps.get(i).getAsString());
			    }
			    Config.getconfig().set("allowed-players", allowed_players);
		    }
		    if (autosaveconfig) {
			    try {
				    ConfigurationProvider.getProvider(YamlConfiguration.class).save(Config.getconfig(), new File(Main.getInstance().getDataFolder(), "config.yml"));
			    } catch (IOException e) {
				    Main.getInstance().getLogger().severe("There was an error while saving the config to file");
			    }
		    }
		    Main.getInstance().getLogger().info("Done!");
	    } catch (JsonParseException e) {
            Main.getInstance().getLogger().severe("\n*****ERROR*****\nThere was an error while parsing the config from the database, reloading the config from file, Exception:\n" + e);
            boolean result = Config.check();
            if (!result) {
                PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
                pluginmanager.unregisterCommands(Main.getInstance());
                pluginmanager.unregisterListeners(Main.getInstance());
                return;
            }
            if (Config.getconfig().getBoolean("multiproxy.enable")) {
                if (DatabaseConnectionManager.getConnected()) {
                    Main.getInstance().getLogger().info("Disconnecting From Database...");
                    DatabaseConnectionManager.disconnect();
                }
                Main.getInstance().getLogger().info("Connecting to Database...");
                DatabaseConnectionManager.connect();
            }
            Main.getInstance().getLogger().info("Done reloading from config file");
        }
    }

    public static void PushData() {
        Main.getInstance().getLogger().info("Started pushing data...");
        JsonObject configobject = new JsonObject();
        configobject.addProperty("update-checker-enabled", Config.getconfig().getBoolean("update-checker-enabled"));
        JsonObject netinfo = new JsonObject();
        netinfo.addProperty("name", Config.getconfig().getString("network-info.name"));
        netinfo.addProperty("max-players", Config.getconfig().getInt("network-info.max-players"));
        configobject.add("network-info", netinfo);
        JsonObject settings = new JsonObject();
        ArrayList<Integer> ap = (ArrayList<Integer>)Config.getconfig().getIntList("settings.allowed-protocols");
        JsonArray allowedprotocols = new JsonArray();
        for (Integer integer : ap) {
            allowedprotocols.add(integer);
        }
        settings.add("allowed-protocols", allowedprotocols.getAsJsonArray());
        settings.addProperty("maintenance-enabled", Config.getconfig().getBoolean("settings.maintenance-enabled"));
        configobject.add("settings", settings);
        JsonObject motds = new JsonObject();
        JsonObject defaultmotd = new JsonObject();
        defaultmotd.addProperty("1", Config.getconfig().getString("motds.default-motd.1"));
        defaultmotd.addProperty("2", Config.getconfig().getString("motds.default-motd.2"));
        motds.add("default-motd", defaultmotd);
        JsonObject maintenancemotd = new JsonObject();
        maintenancemotd.addProperty("1", Config.getconfig().getString("motds.maintenance-motd.1"));
        maintenancemotd.addProperty("2", Config.getconfig().getString("motds.maintenance-motd.2"));
        motds.add("maintenance-motd", maintenancemotd);
        configobject.add("motds", motds);
        JsonObject hovermessages = new JsonObject();
        ArrayList<String> dhm = (ArrayList<String>)Config.getconfig().getStringList("hover-messages.default-hover-message");
        JsonArray defaulthovermessage = new JsonArray();
        for (String string : dhm) {
            defaulthovermessage.add(string);
        }
        hovermessages.add("default-hover-message", defaulthovermessage.getAsJsonArray());
        ArrayList<String> mhm = (ArrayList<String>)Config.getconfig().getStringList("hover-messages.maintenance-hover-message");
        JsonArray maintenancehovermessage = new JsonArray();
        for (String string : mhm) {
            maintenancehovermessage.add(string);
        }
        hovermessages.add("maintenance-hover-message", maintenancehovermessage.getAsJsonArray());
        configobject.add("hover-messages", hovermessages);
        JsonObject prefixs = new JsonObject();
        prefixs.addProperty("plugin", Config.getconfig().getString("prefixs.plugin"));
        prefixs.addProperty("ping", Config.getconfig().getString("prefixs.ping"));
        configobject.add("prefixs", prefixs);
        JsonObject messages = new JsonObject();
        messages.addProperty("not-supported-client-message", Config.getconfig().getString("messages.not-supported-client-message"));
        messages.addProperty("maintenance-message", Config.getconfig().getString("messages.maintenance-message"));
        messages.addProperty("full-message", Config.getconfig().getString("messages.full-message"));
        configobject.add("messages", messages);
        ArrayList<String> aps = (ArrayList<String>)Config.getconfig().getStringList("allowed-players");
        JsonArray allowedplayers = new JsonArray();
        for (String string : aps) {
            allowedplayers.add(string);
        }
        configobject.add("allowed-players", allowedplayers.getAsJsonArray());
        int result = DatabaseConnectionManager.executeUpdate("DELETE FROM cps WHERE groupid='" + Config.getconfig().getString("multiproxy.groupid") + "'");
        if (result == 5) {
            Main.getInstance().getLogger().severe("Cannot delete old rows, canceling pushing");
            return;
        }
        result = DatabaseConnectionManager.executeUpdate("INSERT INTO cps VALUES ('" + Config.getconfig().getString("multiproxy.groupid") +"', '" + new Gson().toJson(configobject) + "')");
        if (result == 5) {
	        Main.getInstance().getLogger().severe("Cannot insert rows, canceling pushing");
	        return;
        }
        Main.getInstance().getLogger().info("Done!");
    }
}
