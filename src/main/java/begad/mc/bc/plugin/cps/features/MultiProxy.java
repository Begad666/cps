package begad.mc.bc.plugin.cps.features;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Backup;
import begad.mc.bc.plugin.cps.utils.Utils;
import com.google.gson.*;
import net.md_5.bungee.api.CommandSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

public class MultiProxy {
    public MultiProxy() {
    }

    public static void pullData(CommandSender sender) {
        if (!Core.getDatabaseManager().isStarted()) {
            if (sender != null) {
                Utils.sendMessage(sender, "", "Not connected to the database", "", "database.not-connected");
            }
            return;
        }
        boolean stoponerror = Core.getConfig().get().getBoolean("multiproxy.backup.stoponerror");
        boolean autoSave = Core.getConfig().get().getBoolean("multiproxy.autosaveconfig");
        if (sender != null) {
            Utils.sendMessage(sender, "", "Pulling...", "", "multiproxy.pull.start");
        }
        Core.getInstance().getLogger().info(Utils.getMessage("", "Pulling...", "", "multiproxy.pull.start", false));
        JsonObject configObject;
        try (Connection connection = Core.getDatabaseManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet data = statement.executeQuery("SELECT configjson FROM cps WHERE groupId='" + Core.getConfig().get().getString("multiproxy.groupid") + "'");
                if (data == null) {
                    if (sender != null) {
                        Utils.sendMessage(sender, "", "No data available", "", "multiproxy.pull.error-no-data");
                    }
                    Core.getInstance().getLogger().info(Utils.getMessage("", "No data available", "", "multiproxy.pull.error-no-data", false));
                    return;
                }
                data.next();
                try {
                    configObject = new Gson().fromJson(data.getString("configjson"), JsonObject.class);
                } catch (JsonSyntaxException e) {
                    if (sender != null) {
                        Utils.sendMessage(sender, "", "Error processing data", "", "multiproxy.pull.error-process-data");
                    }
                    Core.getInstance().getLogger().info(Utils.getMessage("", "Error processing data", "", "multiproxy.pull.error-process-data", false));
                    return;
                }
            }
        } catch (SQLException e) {
            if (sender != null) {
                Utils.sendMessage(sender, "", "Couldn't execute statement", "", "database.statement-execute-error");
            }
            Core.getInstance().getLogger().info(Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false));
            return;
        }

        try {
            if (Core.getConfig().get().getBoolean("multiproxy.backup.enable")) {
                if (sender != null) {
                    Utils.sendMessage(sender, "", "Creating backup...", "", "backup.start");
                }
                Core.getInstance().getLogger().info(Utils.getMessage("", "Creating backup...", "", "backup.start", false));
                Calendar cal = Calendar.getInstance();
                int result = Backup.saveBackup(Core.getConfig().get().getString("backup.name").replace("{time}", (new SimpleDateFormat("hh_mma")).format(cal.getTime())).replace("{date}", (new SimpleDateFormat("yyyy_MM_dd")).format(cal.getTime())));
                switch (result) {
                    case 0:
                        if (sender != null) {
                            Utils.sendMessage(sender, "", "Done backing up config", "", "backup.done");
                        }
                        Core.getInstance().getLogger().info(Utils.getMessage("", "Done backing up config", "", "backup.done", false));
                        break;
                    case 1:
                        if (stoponerror) {
                            if (sender != null) {
                                Utils.sendMessage(sender, "", "Cannot create directory for backups, canceling pull", "", "multiproxy.pull.backup.error-directory-create");
                            }
                            Core.getInstance().getLogger().severe(Utils.getMessage("", "Cannot create directory for backups, canceling pull", "", "multiproxy.pull.backup.error-directory-create", false));
                            return;
                        }

                        if (sender != null) {
                            Utils.sendMessage(sender, "", "Cannot create directory for backups, canceling backup", "", "backup.error-directory-create");
                        }
                        Core.getInstance().getLogger().severe(Utils.getMessage("", "Cannot create directory for backups, canceling backup", "", "backup.error-directory-create", false));
                        break;
                    case 2:
                        if (stoponerror) {
                            if (sender != null) {
                                Utils.sendMessage(sender, "", "Cannot write to the backup file, canceling pull", "", "multiproxy.pull.backup.error-file-write");
                            }
                            Core.getInstance().getLogger().severe(Utils.getMessage("", "Cannot write to the backup file, canceling pull", "", "multiproxy.pull.backup.error-file-write", false));
                            return;
                        }

                        if (sender != null) {
                            Utils.sendMessage(sender, "", "Cannot write to the backup file, canceling backup", "", "backup.error-file-write");
                        }
                        Core.getInstance().getLogger().severe(Utils.getMessage("", "Cannot write to the backup file, canceling backup", "", "backup.error-file-write", false));
                        break;
                    case 3:
                        Core.getInstance().getLogger().severe(Utils.getMessage("", "Cannot find the config file, is the file there?, canceling pull", "", "multiproxy.pull.backup.error-config-not-found", false));
                        return;
                }
            }

            ArrayList allowed_protocols;
            JsonArray ap;
            int i;
            JsonObject defaultmotd;
            JsonObject maintenancemotd;
            JsonObject hovermessages;
            ArrayList defaulthovermessage;
            JsonArray dhm;
            JsonArray mhm;
            JsonObject messages;
            ArrayList allowed_players;
            JsonArray aps;
            JsonObject netinfo;
            JsonObject settings;
            JsonObject motds;
            ArrayList maintenancehovermessage;
            if (!Core.getConfig().get().getBoolean("multiproxy.autoreplace")) {
                Core.getConfig().get().set("update-checker-enabled", configObject.get("update-checker-enabled").getAsBoolean());
                Core.getConfig().get().set("language", configObject.get("language").getAsString());
                netinfo = configObject.get("network-info").getAsJsonObject();
                Core.getConfig().get().set("network-info.name", netinfo.get("name").getAsString());
                Core.getConfig().get().set("network-info.server-version", netinfo.get("server-version").getAsString());
                Core.getConfig().get().set("network-info.maintenance-version", netinfo.get("maintenance-version").getAsString());
                Core.getConfig().get().set("network-info.max-players", netinfo.get("max-players").getAsInt());
                settings = configObject.get("settings").getAsJsonObject();
                allowed_protocols = new ArrayList();
                ap = settings.get("allowed-protocols").getAsJsonArray();

                for (i = 0; i < ap.size(); ++i) {
                    allowed_protocols.add(ap.get(i).getAsInt());
                }

                Core.getConfig().get().set("settings.allowed-protocols", allowed_protocols);
                Core.getConfig().get().set("settings.maintenance-enabled", settings.get("maintenance-enabled").getAsBoolean());
                Core.getConfig().get().set("settings.ping-enabled", settings.get("ping-enabled").getAsBoolean());
                Core.getConfig().get().set("settings.check-type", settings.get("check-type").getAsString());
                motds = configObject.get("motds").getAsJsonObject();
                defaultmotd = motds.get("default-motd").getAsJsonObject();
                Core.getConfig().get().set("motds.default-motd.1", defaultmotd.get("1").getAsString());
                Core.getConfig().get().set("motds.default-motd.2", defaultmotd.get("2").getAsString());
                maintenancemotd = motds.get("maintenance-motd").getAsJsonObject();
                Core.getConfig().get().set("motds.maintenance-motd.1", maintenancemotd.get("1").getAsString());
                Core.getConfig().get().set("motds.maintenance-motd.2", maintenancemotd.get("2").getAsString());
                hovermessages = configObject.get("hover-messages").getAsJsonObject();
                defaulthovermessage = new ArrayList();
                dhm = hovermessages.get("default-hover-message").getAsJsonArray();

                for (i = 0; i < dhm.size(); ++i) {
                    defaulthovermessage.add(dhm.get(i).getAsString());
                }

                Core.getConfig().get().set("hover-messages.default-hover-message", defaulthovermessage);
                maintenancehovermessage = new ArrayList();
                mhm = hovermessages.get("maintenance-hover-message").getAsJsonArray();

                for (i = 0; i < mhm.size(); ++i) {
                    maintenancehovermessage.add(mhm.get(i).getAsString());
                }

                Core.getConfig().get().set("hover-messages.maintenance-hover-message", maintenancehovermessage);
                Core.getConfig().get().set("plugin-prefix", configObject.get("plugin-prefix").getAsString());
                messages = configObject.get("messages").getAsJsonObject();
                Core.getConfig().get().set("messages.not-supported-client-message", messages.get("not-supported-client-message").getAsString());
                Core.getConfig().get().set("messages.maintenance-message", messages.get("maintenance-message").getAsString());
                Core.getConfig().get().set("messages.full-message", messages.get("full-message").getAsString());
                allowed_players = new ArrayList();
                aps = configObject.get("allowed-players").getAsJsonArray();

                for (i = 0; i < aps.size(); ++i) {
                    allowed_players.add(aps.get(i).getAsString());
                }

                Core.getConfig().get().set("allowed-players", allowed_players);
            } else {
                Core.getConfig().get().set("update-checker-enabled", configObject.get("update-checker-enabled").getAsBoolean());
                Core.getConfig().get().set("language", configObject.get("language").getAsString());
                netinfo = configObject.get("network-info").getAsJsonObject();
                Core.getConfig().get().set("network-info.name", Utils.replaceUnicodeCharacters(netinfo.get("name").getAsString()));
                Core.getConfig().get().set("network-info.server-version", Utils.replaceUnicodeCharacters(netinfo.get("server-version").getAsString()));
                Core.getConfig().get().set("network-info.maintenance-version", Utils.replaceUnicodeCharacters(netinfo.get("maintenance-version").getAsString()));
                Core.getConfig().get().set("network-info.max-players", netinfo.get("max-players").getAsInt());
                settings = configObject.get("settings").getAsJsonObject();
                allowed_protocols = new ArrayList();
                ap = settings.get("allowed-protocols").getAsJsonArray();

                for (i = 0; i < ap.size(); ++i) {
                    allowed_protocols.add(ap.get(i).getAsInt());
                }

                Core.getConfig().get().set("settings.allowed-protocols", allowed_protocols);
                Core.getConfig().get().set("settings.maintenance-enabled", settings.get("maintenance-enabled").getAsBoolean());
                Core.getConfig().get().set("settings.ping-enabled", settings.get("ping-enabled").getAsBoolean());
                Core.getConfig().get().set("settings.check-type", settings.get("check-type").getAsString());
                motds = configObject.get("motds").getAsJsonObject();
                defaultmotd = motds.get("default-motd").getAsJsonObject();
                Core.getConfig().get().set("motds.default-motd.1", Utils.replaceUnicodeCharacters(defaultmotd.get("1").getAsString()));
                Core.getConfig().get().set("motds.default-motd.2", Utils.replaceUnicodeCharacters(defaultmotd.get("2").getAsString()));
                maintenancemotd = motds.get("maintenance-motd").getAsJsonObject();
                Core.getConfig().get().set("motds.maintenance-motd.1", Utils.replaceUnicodeCharacters(maintenancemotd.get("1").getAsString()));
                Core.getConfig().get().set("motds.maintenance-motd.2", Utils.replaceUnicodeCharacters(maintenancemotd.get("2").getAsString()));
                hovermessages = configObject.get("hover-messages").getAsJsonObject();
                defaulthovermessage = new ArrayList();
                dhm = hovermessages.get("default-hover-message").getAsJsonArray();

                for (i = 0; i < dhm.size(); ++i) {
                    defaulthovermessage.add(Utils.replaceUnicodeCharacters(dhm.get(i).getAsString()));
                }

                Core.getConfig().get().set("hover-messages.default-hover-message", defaulthovermessage);
                maintenancehovermessage = new ArrayList();
                mhm = hovermessages.get("maintenance-hover-message").getAsJsonArray();

                for (i = 0; i < mhm.size(); ++i) {
                    maintenancehovermessage.add(Utils.replaceUnicodeCharacters(mhm.get(i).getAsString()));
                }

                Core.getConfig().get().set("hover-messages.maintenance-hover-message", maintenancehovermessage);
                Core.getConfig().get().set("plugin-prefix", Utils.replaceUnicodeCharacters(configObject.get("plugin-prefix").getAsString()));
                messages = configObject.get("messages").getAsJsonObject();
                Core.getConfig().get().set("messages.not-supported-client-message", Utils.replaceUnicodeCharacters(messages.get("not-supported-client-message").getAsString()));
                Core.getConfig().get().set("messages.maintenance-message", Utils.replaceUnicodeCharacters(messages.get("maintenance-message").getAsString()));
                Core.getConfig().get().set("messages.full-message", Utils.replaceUnicodeCharacters(messages.get("full-message").getAsString()));
                allowed_players = new ArrayList();
                aps = configObject.get("allowed-players").getAsJsonArray();

                for (i = 0; i < aps.size(); ++i) {
                    allowed_players.add(aps.get(i).getAsString());
                }

                Core.getConfig().get().set("allowed-players", allowed_players);
            }

            if (autoSave) {
                Core.getConfig().save();
            }
            if (sender != null) {
                Utils.sendMessage(sender, "", "Done", "", "multiproxy.pull.done");
            }
            Core.getInstance().getLogger().info(Utils.getMessage("", "Done", "", "multiproxy.pull.done", false));
        } catch (JsonParseException e) {
            Core.getInstance().getLogger().severe("\n*****ERROR*****\nThere was an error while parsing the config from the database, reloading the plugin, Exception:\n" + e);
            Core.reload(null);
        }

    }

    public static void pushData(CommandSender sender) {
        if (!Core.getDatabaseManager().isStarted()) {
            if (sender != null) {
                Utils.sendMessage(sender, "", "Not connected to the database", "", "database.not-connected");
            }
            return;
        }
        if (sender != null) {
            Utils.sendMessage(sender, "", "Pushing...", "", "multiproxy.push.start");
        }
        Core.getInstance().getLogger().info(Utils.getMessage("", "Pushing...", "", "multiproxy.push.start", false));
        JsonObject configobject = new JsonObject();
        configobject.addProperty("update-checker-enabled", Core.getConfig().get().getBoolean("update-checker-enabled"));
        configobject.addProperty("language", Core.getConfig().get().getBoolean("language"));
        JsonObject netinfo = new JsonObject();
        netinfo.addProperty("name", Core.getConfig().get().getString("network-info.name"));
        netinfo.addProperty("server-version", Core.getConfig().get().getString("network-info.server-version"));
        netinfo.addProperty("maintenance-version", Core.getConfig().get().getString("network-info.maintenance-version"));
        netinfo.addProperty("max-players", Core.getConfig().get().getInt("network-info.max-players"));
        configobject.add("network-info", netinfo);
        JsonObject settings = new JsonObject();
        ArrayList<Integer> ap = (ArrayList) Core.getConfig().get().getIntList("settings.allowed-protocols");
        JsonArray allowedprotocols = new JsonArray();

        for (Integer integer : ap) {
            allowedprotocols.add(integer);
        }

        settings.add("allowed-protocols", allowedprotocols.getAsJsonArray());
        settings.addProperty("maintenance-enabled", Core.getConfig().get().getBoolean("settings.maintenance-enabled"));
        settings.addProperty("ping-enabled", Core.getConfig().get().getBoolean("settings.ping-enabled"));
        settings.addProperty("check-type", Core.getConfig().get().getString("settings.check-type"));
        configobject.add("settings", settings);
        JsonObject motds = new JsonObject();
        JsonObject defaultmotd = new JsonObject();
        defaultmotd.addProperty("1", Core.getConfig().get().getString("motds.default-motd.1"));
        defaultmotd.addProperty("2", Core.getConfig().get().getString("motds.default-motd.2"));
        motds.add("default-motd", defaultmotd);
        JsonObject maintenancemotd = new JsonObject();
        maintenancemotd.addProperty("1", Core.getConfig().get().getString("motds.maintenance-motd.1"));
        maintenancemotd.addProperty("2", Core.getConfig().get().getString("motds.maintenance-motd.2"));
        motds.add("maintenance-motd", maintenancemotd);
        configobject.add("motds", motds);
        JsonObject hovermessages = new JsonObject();
        ArrayList<String> dhm = (ArrayList) Core.getConfig().get().getStringList("hover-messages.default-hover-message");
        JsonArray defaulthovermessage = new JsonArray();

        for (String string : dhm) {
            defaulthovermessage.add(string);
        }

        hovermessages.add("default-hover-message", defaulthovermessage.getAsJsonArray());
        ArrayList<String> mhm = (ArrayList) Core.getConfig().get().getStringList("hover-messages.maintenance-hover-message");
        JsonArray maintenancehovermessage = new JsonArray();

        for (String string : mhm) {
            maintenancehovermessage.add(string);
        }

        hovermessages.add("maintenance-hover-message", maintenancehovermessage.getAsJsonArray());
        configobject.add("hover-messages", hovermessages);
        configobject.addProperty("plugin", Core.getConfig().get().getString("plugin-prefix"));
        JsonObject messages = new JsonObject();
        messages.addProperty("not-supported-client-message", Core.getConfig().get().getString("messages.not-supported-client-message"));
        messages.addProperty("maintenance-message", Core.getConfig().get().getString("messages.maintenance-message"));
        messages.addProperty("full-message", Core.getConfig().get().getString("messages.full-message"));
        configobject.add("messages", messages);
        ArrayList<String> aps = (ArrayList) Core.getConfig().get().getStringList("allowed-players");
        JsonArray allowedplayers = new JsonArray();

        for (String string : aps) {
            allowedplayers.add(string);
        }

        configobject.add("allowed-players", allowedplayers.getAsJsonArray());

        try (Connection connection = Core.getDatabaseManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM cps WHERE groupid='" + Core.getConfig().get().getString("multiproxy.groupid") + "'");
                statement.executeUpdate("INSERT INTO cps VALUES ('" + Core.getConfig().get().getString("multiproxy.groupid") + "', '" + (new Gson()).toJson(configobject) + "')");
            } catch (SQLException exception) {
                Core.getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), exception);
            }
        } catch (SQLException e) {
            Core.getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), e);
        }

        Core.getInstance().getLogger().info(Utils.getMessage("", "Done", "", "multiproxy.push.done", false));
    }
}
