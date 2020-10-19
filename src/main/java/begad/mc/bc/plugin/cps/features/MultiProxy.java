package begad.mc.bc.plugin.cps.features;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.commands.cps.Fix;
import begad.mc.bc.plugin.cps.utils.Backup;
import begad.mc.bc.plugin.cps.utils.Utils;
import com.google.gson.Gson;
import net.md_5.bungee.api.CommandSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
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
        String json;
        if (sender != null) {
            Utils.sendMessage(sender, "", "Pulling...", "", "multiproxy.pull.start");
        }
        Core.getInstance().getLogger().info(Utils.getMessage("", "Pulling...", "", "multiproxy.pull.start", false));
        try (Connection connection = Core.getDatabaseManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet data = statement.executeQuery("SELECT config FROM cps WHERE groupId='" + Core.getConfig().get().getString("multiproxy.groupid") + "'");
                if (data == null) {
                    if (sender != null) {
                        Utils.sendMessage(sender, "", "No data available", "", "multiproxy.pull.error-no-data");
                    }
                    Core.getInstance().getLogger().info(Utils.getMessage("", "No data available", "", "multiproxy.pull.error-no-data", false));
                    return;
                }
                data.next();
                json = data.getString("config");
            }
        } catch (SQLException e) {
            if (sender != null) {
                Utils.sendMessage(sender, "", "Couldn't execute statement", "", "database.statement-execute-error");
            }
            Core.getInstance().getLogger().info(Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false));
            return;
        }

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
        Map map = new Gson().fromJson(json, Map.class);
        Utils.removeBlocked(map);
        Utils.undump(Core.getConfig().get(), map);
        if (Core.getConfig().get().getBoolean("multiproxy.autoreplace")) {
            new Fix().run(sender, null);
        }
        if (sender != null) {
            Utils.sendMessage(sender, "", "Done, Reloading...", "", "multiproxy.pull.done");
        }

        if (Core.getConfig().get().getBoolean("multiproxy.autosave")) {
            Core.getConfig().save();
        }
        Core.getInstance().getLogger().info(Utils.getMessage("", "Done, Reloading...", "", "multiproxy.pull.done", false));
        Core.reload(sender, Core.getConfig().get().getBoolean("multiproxy.autosave"));

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
        Map<Object, Object> dumped = Utils.dump(Core.getConfig().get());
        Utils.removeBlocked(dumped);

        try (Connection connection = Core.getDatabaseManager().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM cps WHERE groupid='" + Core.getConfig().get().getString("multiproxy.groupid") + "'");
                statement.executeUpdate("INSERT INTO cps VALUES ('" + Core.getConfig().get().getString("multiproxy.groupid") + "', '" + new Gson().toJson(dumped) + "')");
            } catch (SQLException exception) {
                Core.getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), exception);
                return;
            }
        } catch (SQLException e) {
            Core.getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), e);
            return;
        }

        Core.getInstance().getLogger().info(Utils.getMessage("", "Done", "", "multiproxy.push.done", false));
    }
}
