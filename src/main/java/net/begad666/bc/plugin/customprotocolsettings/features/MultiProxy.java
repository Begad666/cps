package net.begad666.bc.plugin.customprotocolsettings.features;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.begad666.bc.plugin.customprotocolsettings.Main;
import net.begad666.bc.plugin.customprotocolsettings.utils.Config;
import net.begad666.bc.plugin.customprotocolsettings.utils.DatabaseConnectionManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MultiProxy {

    public static void ApplyData(JsonObject configobject, boolean autosaveconfig) {
        Main.getInstance().getLogger().info("AutoPull data ready, starting config changes");
        try {
            Config.getconfig().set("update-checker-enabled", configobject.get("update-checker-enabled").getAsBoolean());
            Config.getconfig().set("password", configobject.get("password").getAsString());
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
            if (autosaveconfig) {
                try {
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(Config.getconfig(), new File(Main.getInstance().getDataFolder(), "config.yml"));
                } catch (IOException e) {
                    Main.getInstance().getLogger().severe("There was an error while saving the config to file");
                }
            }
        } catch (JsonParseException e) {
            Main.getInstance().getLogger().severe("CustomProtocolSettings: \n*****ERRROR*****\nThere was an error while parsing the config from the database, reloading the config from file, Exception:\n" + e);
            boolean result = Config.check();
            if (!result) {
                PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
                pluginmanager.unregisterCommands(Main.getInstance());
                pluginmanager.unregisterListeners(Main.getInstance());
                return;
            }
            if (Config.getconfig().getBoolean("multiproxy.enable")) {
                Main.getInstance().getLogger().info("Disconnecting From Database...");
                DatabaseConnectionManager.disconnect();
                Main.getInstance().getLogger().info("Reconnecting to Database...");
                DatabaseConnectionManager.connect();
            }
            Main.getInstance().getLogger().info("Done reloading from config file");
        }


    }
}
