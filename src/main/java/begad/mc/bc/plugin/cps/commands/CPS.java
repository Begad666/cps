package begad.mc.bc.plugin.cps.commands;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.features.MultiProxy;
import begad.mc.bc.plugin.cps.utils.Backup;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CPS extends Command implements TabExecutor {
    public static boolean isEnabled;
    public final String[] commands = new String[]{
            "help", "reload", "pull", "push", "dbconnected", "createbackup", "loadbackup"
    };

    public CPS() {
        super("cps", "cps.admin");
    }

    public void execute(CommandSender sender, String[] args) {
        ArrayList<String> nargs = new ArrayList<>(Arrays.asList(args));
        if (nargs.isEmpty()) {
            Utils.sendMessage(sender, "------------------------------");
            Utils.sendMessage(sender, "", "Compile Version: " + Core.getUpdates().getCompileCurrentVersion(), Core.getUpdates().getCompileCurrentVersion(), "commands.cps.default.compile-version");
            Utils.sendMessage(sender, "", "Version: " + Core.getInstance().getDescription().getVersion(), Core.getInstance().getDescription().getVersion(), "commands.cps.default.current-version");
            Utils.sendMessage(sender, "", "Compile Config Version: " + Core.getUpdates().getCompileConfigVersion(), Core.getUpdates().getCompileConfigVersion(), "commands.cps.default.compile-config-version");
            Utils.sendMessage(sender, "", "Config Version: " + Core.getConfig().get().getString("config-version"), Core.getConfig().get().getString("config-version"), "commands.cps.default.current-config-version");
            Utils.sendMessage(sender, "", "Updates: " + Core.getUpdates().getMessage(), Core.getUpdates().getMessage(), "commands.cps.default.updates");
            TextComponent plug;
            if (sender instanceof ProxiedPlayer) {
                plug = new TextComponent(Utils.getMessage("", "Plugin Page: Click Here", "", "commands.cps.default.plugin-page-click", true));
                plug.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.spigotmc.org/resources/69385/"));
            } else {
                plug = new TextComponent(Utils.getMessage("", "Plugin Page: https://www.spigotmc.org/resources/69385/", "https://www.spigotmc.org/resources/69385/", "commands.cps.default.plugin-page-link", true));
            }
            sender.sendMessage(plug);
            Utils.sendMessage(sender, "------------------------------");
        } else {
            String cmdname = nargs.get(0);
            nargs.remove(0);
            switch (cmdname) {
                case "help": {
                    help(sender);
                    break;
                }

                case "reload": {
                    if ((sender instanceof ProxiedPlayer)) {
                        if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                            reload(sender);
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        reload(sender);
                    }
                    break;
                }

                case "pull": {
                    if ((sender instanceof ProxiedPlayer)) {
                        if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                            pull(sender);
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        pull(sender);
                    }
                    break;
                }

                case "push": {
                    if ((sender instanceof ProxiedPlayer)) {
                        if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                            push(sender);
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        push(sender);
                    }
                    break;
                }

                case "dbconnected": {
                    dbconnected(sender);
                    break;
                }

                case "createbackup": {
                    if ((sender instanceof ProxiedPlayer)) {
                        if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                            createbackup(sender);
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        createbackup(sender);
                    }
                    break;
                }

                case "loadbackup": {
                    if ((sender instanceof ProxiedPlayer)) {
                        if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                            loadbackup(sender, nargs);
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        loadbackup(sender, nargs);
                    }
                    break;
                }

                default: {
                    Utils.sendMessage(sender, "", "Command not found, use /cps help for all commands", "", "commands.cps.not-found-use-help");
                    break;
                }
            }
        }
    }

    private void help(CommandSender sender) {
        Utils.sendMessage(sender, "", "Help (CR means you will need to be on allowed-players config section or have cps.bypass permission, the check is based on settings.check-type config section)", "", "commands.cps.help.title");
        Utils.sendMessage(sender, "/cps help", "/cps help - Shows this help", "", "commands.cps.help.help");
        Utils.sendMessage(sender, "/cps reload", "/cps reload - Reloads the config from the file without restarting BungeeCord (CR)", "(CR)", "commands.cps.reload.help");
        Utils.sendMessage(sender, "/cps pull", "/cps pull - Pulls config data from the database and processes it (CR)", "(CR)", "commands.cps.pull.help");
        Utils.sendMessage(sender, "/cps push", "/cps push - Pushes current config data to the database (CR)", "(CR)", "commands.cps.push.help");
        Utils.sendMessage(sender, "/cps dbconnected", "/cps dbconnected - Sends what state is the database connection", "", "commands.cps.dbconnected.help");
        Utils.sendMessage(sender, "/cps createbackup", "/cps createbackup - Creates a config backup (CR)", "(CR)", "commands.cps.createbackup.help");
        Utils.sendMessage(sender, "/cps loadbackup", "/cps loadbackup <file name> - Loads the backup with that name, no need for adding .yml at the end (CR)", "(CR)", "commands.cps.loadbackup.help");
        Utils.sendMessage(sender, "/ping", "/ping [player name] - The name describes it all", "", "commands.ping.help");
    }

    private void reload(CommandSender sender) {
        Core.reload(sender);
    }

    private void pull(CommandSender sender) {
        MultiProxy.pullData(sender);
    }

    private void push(CommandSender sender) {
        MultiProxy.pushData(sender);
    }

    private void dbconnected(CommandSender sender) {
        if (Core.getDatabaseManager().isStarted()) {
            Utils.sendMessage(sender, "", "Connected", "", "commands.cps.dbconnected.connected");
        } else {
            Utils.sendMessage(sender, "", "Disconnected", "", "commands.cps.dbconnected.disconnected");
        }
    }

    private void createbackup(CommandSender sender) {
        Utils.sendMessage(sender, "", "Creating backup...", "", "backup.start");
        Calendar cal = Calendar.getInstance();
        int result = Backup.saveBackup(Core.getConfig().get().getString("backup.name").replace("{time}", (new SimpleDateFormat("hh_mma")).format(cal.getTime())).replace("{date}", (new SimpleDateFormat("MM_dd_yyyy")).format(cal.getTime())));
        switch (result) {
            case 0:
                Utils.sendMessage(sender, "", "Done backing up config", "", "backup.done");
                break;
            case 1:
                Utils.sendMessage(sender, "", "Cannot create directory for backups, canceling backup", "", "backup.error-directory-create");
                break;
            case 2:
                Utils.sendMessage(sender, "", "Cannot write to the backup file, canceling backup", "", "backup.error-file-write");
                break;
            case 3:
                Utils.sendMessage(sender, "", "Cannot find the config file, is the file there?, canceling backup", "", "backup.error-config-not-found");
        }

    }

    private void loadbackup(CommandSender sender, ArrayList<String> args) {
        if (args.isEmpty()) {
            Utils.sendMessage(sender, "", "Please provide a file name", "", "backup.load.error-args");
            return;
        }
        String file = args.get(0);
        int result = Backup.loadBackup(file);
        switch (result) {
            case 0:
                Utils.sendMessage(sender, "", "Done loading config backup", "", "backup.load.done");
                Core.reload(sender);
                break;
            case 1:
                Utils.sendMessage(sender, "", "No backup directory is available and couldn't create backup directory", "", "backup.load.error-no-directory-cannot-create");
                break;
            case 2:
                Utils.sendMessage(sender, "", "Directory created but no backup files", "", "backup.load.directory-create-no-files");
                break;
            case 3:
                Utils.sendMessage(sender, "", "No file with that name was found", "", "backup.load.error-not-found");
                break;
            case 4:
                Utils.sendMessage(sender, "", "Cannot write to config file", "", "backup.load.error-file-write");
        }

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        ArrayList<String> matches = new ArrayList<>();
        if (args.length == 0) {
            return matches;
        } else {
            switch (args.length) {
                case 1: {
                    matches = new ArrayList<>();
                    for (String command : commands) {
                        if (command.startsWith(args[0])) {
                            matches.add(command);
                        }
                    }
                    break;
                }

                case 2: {
                    if (args[0].equals("loadbackup")) {
                        File folder = new File(Core.getInstance().getDataFolder() + "/" + Core.getConfig().get().getString("backup.folder"));
                        File[] listOfFiles = folder.listFiles();
                        if (listOfFiles == null) {
                            break;
                        }

                        for (File file : listOfFiles) {
                            if (file.isFile() && file.getName().endsWith(".yml")) {
                                matches.add(file.getName());
                            }
                        }
                    }
                    break;
                }
            }
        }
        return matches;
    }
}
