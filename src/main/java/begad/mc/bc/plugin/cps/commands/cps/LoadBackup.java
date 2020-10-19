package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Backup;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Supplier;

public class LoadBackup extends Command {
    private static final Supplier<String> ArgsRequired = () -> Utils.getMessage("", "Please provide a file name", "", "backup.load.error-args", true);

    public LoadBackup() {
        super("loadbackup", null, 1, ArgsRequired.get());
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                String file = arrayList.get(0);
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
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
            String file = arrayList.get(0);
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
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        ArrayList<String> matches = new ArrayList<>();
        File folder = new File(Core.getInstance().getDataFolder() + "/" + Core.getConfig().get().getString("backup.folder"));
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            return matches;
        }

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                matches.add(file.getName());
            }
        }
        return matches;
    }
}