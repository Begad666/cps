package begad.mc.bc.plugin.cps;

import begad.mc.bc.plugin.cps.commands.CPS;
import begad.mc.bc.plugin.cps.commands.Ping;
import begad.mc.bc.plugin.cps.features.ChangePingData;
import begad.mc.bc.plugin.cps.features.DisconnectNotAllowedUsers.AllowedPlayersBased.UUIDBased;
import begad.mc.bc.plugin.cps.features.DisconnectNotAllowedUsers.AllowedPlayersBased.UsernameBased;
import begad.mc.bc.plugin.cps.features.DisconnectNotAllowedUsers.PermBased;
import begad.mc.bc.plugin.cps.integration.premiumvanish.PremiumVanishIntegration;
import begad.mc.bc.plugin.cps.integration.redisbungee.RedisBungeeIntegration;
import begad.mc.bc.plugin.cps.utils.CheckType;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.ScheduledTasks;
import begad.mc.bc.plugin.cps.utils.Utils;
import begad.mc.bc.plugin.cps.vanish.VanishListener;
import begad.mc.bc.plugin.cps.vanish.VanishManager;
import begad.mc.bc.utils.BungeeConfig;
import begad.mc.bc.utils.BungeeUpdates;
import begad.mc.utils.Database;
import begad.mc.utils.Database.Factory;
import begad.mc.utils.UpdateAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.bstats.bungeecord.MetricsLite;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Core extends Plugin {
    public static String Language;
    private static Core instance;
    private static BungeeUpdates updates;
    private static BungeeConfig config;
    private static Database databaseManager;
    public static final VanishManager vanishManager = new VanishManager();
    public static final RedisBungeeIntegration redisBungeeIntegration = new RedisBungeeIntegration();
    public static final PremiumVanishIntegration premiumVanishIntegration = new PremiumVanishIntegration();
    private static MetricsLite metrics;

    public static void reload(CommandSender sender, boolean full) {
        PluginManager pluginManager = getInstance().getProxy().getPluginManager();
        redisBungeeIntegration.stop();
        premiumVanishIntegration.stop();
        pluginManager.unregisterListeners(getInstance());
        pluginManager.unregisterCommands(getInstance());
        if (sender != null) {
            Utils.sendMessage(sender, "", "Reloading...", "", "plugin.reload");
        }
        getInstance().getLogger().info(Utils.getMessage("", "Reloading...", "", "plugin.reload", false));

        if (full) {
            databaseManager.stopDataSource();
            boolean result = config.check();
            if (result) {
                config.loadMessagesFile(config.get().getString("language"));
                Language = config.get().getString("language");
                if (config.get().getBoolean("multiproxy.enable")) {
                    getInstance().getLogger().info("Connecting To Database...");
                    ProxyServer.getInstance().getScheduler().runAsync(getInstance(), () -> {
                        databaseManager = Factory.setupWithDataSource("begad.libs.mariadb.jdbc.MariaDbDataSource", config.get().getString("connectionsettings.host"), config.get().getInt("connectionsettings.port"), config.get().getString("connectionsettings.database"), "CustomProtocolSettings Pool", 4);
                        databaseManager.set(config.get().getString("connectionsettings.user"), config.get().getString("connectionsettings.password"));
                        databaseManager.startDataSource();
                        try (Connection connection = databaseManager.getConnection()) {
                            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `cps` ( `groupId` VARCHAR(25) NOT NULL , `config` LONGTEXT NOT NULL , PRIMARY KEY (`groupId`))")) {
                                statement.executeUpdate();
                            } catch (SQLException exception) {
                                getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), exception);
                            }
                        } catch (SQLException e) {
                            getInstance().getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), e);
                        }
                    });
                } else {
                    databaseManager = Factory.setupNothing();
                }
            } else {
                Language = config.get().getString("language");
            }
            if (config.get().getBoolean("update-checker-enabled")) {
                ScheduledTasks.updateTask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
                    String current = getInstance().getDescription().getVersion();
                    String latest = updates.getLatestVersion();
                    if (latest == null) {
                        getInstance().getLogger().warning(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                        updates.setMessage(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                    } else if (current.compareTo(latest) < 0) {
                        getInstance().getLogger().info(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                        updates.setMessage(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                    } else if (current.compareTo(latest) == 0) {
                        getInstance().getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                    } else if (current.compareTo(latest) > 0) {
                        if (current.compareTo(updates.getCompileCurrentVersion()) != 0) {
                            getInstance().getLogger().warning(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                            updates.setMessage(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                        } else {
                            getInstance().getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                            updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        }
                    }

                }, 8, 8, TimeUnit.HOURS);
            } else {
                updates.setMessage(Utils.getMessage("", "Updates are disabled", "", "updates.disabled", false));
            }

            getInstance().RegisterCommands();
            getInstance().RegisterListeners();
            if (sender != null) {
                Utils.sendMessage(sender, "", "Done", "", "plugin.done");
            }
            getInstance().getLogger().info(Utils.getMessage("", "Done", "", "plugin.done", false));
            redisBungeeIntegration.init();
            premiumVanishIntegration.init();
        }
    }

    public static Core getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        PluginManager pluginmanager = ProxyServer.getInstance().getPluginManager();
        this.getLogger().info("Started disable process");
        this.getLogger().info("Unregistering commands...");
        pluginmanager.unregisterCommands(getInstance());
        this.getLogger().info("Unregistering listeners...");
        pluginmanager.unregisterListeners(getInstance());
        if (config.get().getBoolean("multiproxy.enable") && databaseManager.isStarted()) {
            this.getLogger().info("Disconnecting from database...");
            databaseManager.stopDataSource();
        }

        this.getLogger().info("Canceling scheduled tasks...");
        ProxyServer.getInstance().getScheduler().cancel(getInstance());
        boolean vanishDone = vanishManager.dump(new File(getInstance().getDataFolder(), "vanish.dat"));
        if (!vanishDone) {
            this.getLogger().severe(Utils.getMessage("", "An error occurred while saving vanish data to file", "", "vanish.save-error", false));
        }
        this.getLogger().info(this.getDescription().getVersion() + " is now disabled!");
        redisBungeeIntegration.stop();
        premiumVanishIntegration.stop();
    }

    @Override
    public void onLoad() {
        instance = this;
        updates = new BungeeUpdates(getInstance(), "CustomProtocolSettings", "69385", "v8.1.4", "v4.1.5", UpdateAPI.SPIGET);
        updates.setMessage("....");
    }

    private void RegisterListeners() {
        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerListener(getInstance(), new ChangePingData());

        try {
            switch (CheckType.valueOf(config.get().getString("settings.check-type"))) {
                case PERM: {
                    pluginManager.registerListener(getInstance(), new PermBased());
                    this.getLogger().info(Utils.getMessage("", "Using permission checking", "", "checker.perm", false));
                    Checker.Type = CheckType.PERM;
                    break;
                }
                case CONFIG_ALLOWED_PLAYERS_UUID: {
                    pluginManager.registerListener(getInstance(), new UUIDBased());
                    this.getLogger().info(Utils.getMessage("", "Using UUID for checking with allowed-players config section", "", "checker.allowed-players-uuid", false));
                    Checker.Type = CheckType.CONFIG_ALLOWED_PLAYERS_UUID;
                    break;
                }
                case CONFIG_ALLOWED_PLAYERS_USERNAMES: {
                    pluginManager.registerListener(getInstance(), new UsernameBased());
                    this.getLogger().info(Utils.getMessage("", "Using username for checking with allowed-players config section", "", "checker.allowed-players-username", false));
                    Checker.Type = CheckType.CONFIG_ALLOWED_PLAYERS_USERNAMES;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            this.getLogger().warning(Utils.getMessage("", "Couldn't process settings.check-type, using permission checking (default)", "", "checker.error-perm", false));
            pluginManager.registerListener(getInstance(), new PermBased());
            Checker.Type = CheckType.PERM;
        }

        pluginManager.registerListener(getInstance(), new VanishListener());

    }

    public static BungeeConfig getConfig() {
        return config;
    }

    public static BungeeUpdates getUpdates() {
        return updates;
    }

    public static Database getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Started enable process");
        this.getLogger().info("Loading config...");
        config = new BungeeConfig(getInstance(), updates, true, "", "messages");
        boolean result = config.check();
        if (result) {
            config.loadMessagesFile(config.get().getString("language"));
            Language = config.get().getString("language");
            this.getLogger().info("Registering commands...");
            this.RegisterCommands();
            this.getLogger().info("Registering listeners...");
            this.RegisterListeners();
            if (config.get().getBoolean("multiproxy.enable")) {
                this.getLogger().info("Connecting to database...");
                ProxyServer.getInstance().getScheduler().runAsync(getInstance(), () -> {
                    databaseManager = Factory.setupWithDataSource("begad.libs.mariadb.jdbc.MariaDbDataSource", config.get().getString("connectionsettings.host"), config.get().getInt("connectionsettings.port"), config.get().getString("connectionsettings.database"), "CustomProtocolSettings Pool", 4);
                    databaseManager.set(config.get().getString("connectionsettings.user"), config.get().getString("connectionsettings.password"));
                    databaseManager.startDataSource();
                    try (Connection connection = databaseManager.getConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `cps` ( `groupId` VARCHAR(25) NOT NULL , `config` LONGTEXT NOT NULL , PRIMARY KEY (`groupId`))")) {
                            statement.executeUpdate();
                        } catch (SQLException exception) {
                            this.getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), exception);
                        }
                    } catch (SQLException e) {
                        this.getLogger().log(Level.SEVERE, Utils.getMessage("", "Couldn't execute statement", "", "database.statement-execute-error", false), e);
                    }
                });
            } else {
                databaseManager = Factory.setupNothing();
            }

            metrics = new MetricsLite(this, 5145);
            if (config.get().getBoolean("update-checker-enabled")) {
                ProxyServer.getInstance().getScheduler().runAsync(getInstance(), () -> {
                    String current = this.getDescription().getVersion();
                    String latest = updates.getLatestVersion();
                    if (latest == null) {
                        this.getLogger().warning(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                        updates.setMessage(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                    } else if (current.compareTo(latest) < 0) {
                        this.getLogger().info(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                        updates.setMessage(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                    } else if (current.compareTo(latest) == 0) {
                        this.getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                    } else if (current.compareTo(latest) > 0) {
                        if (current.compareTo(updates.getCompileCurrentVersion()) != 0) {
                            this.getLogger().warning(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                            updates.setMessage(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                        } else {
                            this.getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                            updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        }
                    }

                });
                ScheduledTasks.updateTask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
                    String current = this.getDescription().getVersion();
                    String latest = updates.getLatestVersion();
                    if (latest == null) {
                        this.getLogger().warning(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                        updates.setMessage(Utils.getMessage("", "Couldn't check for updates", "", "updates.error-check", false));
                    } else if (current.compareTo(latest) < 0) {
                        this.getLogger().info(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                        updates.setMessage(Utils.getMessage("", "New version: " + latest, latest, "updates.new", false));
                    } else if (current.compareTo(latest) == 0) {
                        this.getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                    } else if (current.compareTo(latest) > 0) {
                        if (current.compareTo(updates.getCompileCurrentVersion()) != 0) {
                            this.getLogger().warning(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                            updates.setMessage(Utils.getMessage("", "Some of the plugin files are changed, reinstall the plugin from https://www.spigotmc.org/resources/customprotocolsettings.69385/", "https://www.spigotmc.org/resources/customprotocolsettings.69385/", "updates.error-changed", false));
                        } else {
                            this.getLogger().info(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                            updates.setMessage(Utils.getMessage("", "You are up to date", "", "updates.up-to-date", false));
                        }
                    }

                }, 8, 8, TimeUnit.HOURS);
            } else {
                updates.setMessage(Utils.getMessage("", "Updates are disabled", "", "updates.disabled", false));
            }
            File vanishData = new File(getInstance().getDataFolder(), "vanish.dat");
            if (vanishData.exists()) {
                boolean done = vanishManager.loadFile(vanishData);
                if (!done) {
                    this.getLogger().severe(Utils.getMessage("", "An error occurred while loading vanish data from file. Continuing without loading", "", "vanish.load-error", false));
                }
            }
            if (config.get().getInt("settings.save-time") > 0) {
                ScheduledTasks.saveTask = ProxyServer.getInstance().getScheduler().schedule(getInstance(), () -> {
                    boolean vanishDone = vanishManager.dump(vanishData);
                    if (!vanishDone) {
                        this.getLogger().severe(Utils.getMessage("", "An error occurred while saving vanish data to file", "", "vanish.save-error", false));
                    }
                }, config.get().getInt("settings.save-time"), config.get().getInt("settings.save-time"), TimeUnit.MINUTES);
            }
            redisBungeeIntegration.init();
            premiumVanishIntegration.init();
        }
    }

    private void RegisterCommands() {
        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerCommand(getInstance(), new CPS());
        if (config.get().getBoolean("settings.ping-enabled")) {
            pluginManager.registerCommand(getInstance(), new Ping());
        }

    }
}
