package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Backup;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateBackup extends Command {
    public CreateBackup() {
        super("createbackup");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (Checker.checkPlayer((ProxiedPlayer) sender)) {
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
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
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
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
