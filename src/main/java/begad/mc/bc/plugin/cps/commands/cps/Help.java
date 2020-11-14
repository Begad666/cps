package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;

public class Help extends Command {
    public Help() {
        super("help");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        Utils.sendMessage(sender, "", "Help (CR means you will need to be on allowed-players config section or have cps.bypass permission, the check is based on settings.check-type config section)", "", "commands.cps.help.title");
        Utils.sendMessage(sender, "/cps help", "/cps help - Shows this help", "", "commands.cps.help.help");
        Utils.sendMessage(sender, "/cps reload", "/cps reload - Reloads the config from the file without restarting BungeeCord (CR)", "(CR)", "commands.cps.reload.help");
        Utils.sendMessage(sender, "/cps pull", "/cps pull - Pulls config data from the database and processes it (CR)", "(CR)", "commands.cps.pull.help");
        Utils.sendMessage(sender, "/cps push", "/cps push - Pushes current config data to the database (CR)", "(CR)", "commands.cps.push.help");
        Utils.sendMessage(sender, "/cps dbconnected", "/cps dbconnected - Sends what state is the database connection", "", "commands.cps.dbconnected.help");
        Utils.sendMessage(sender, "/cps createbackup", "/cps createbackup - Creates a config backup (CR)", "(CR)", "commands.cps.createbackup.help");
        Utils.sendMessage(sender, "/cps loadbackup", "/cps loadbackup <file name> - Loads the backup with that name, no need for adding .yml at the end (CR)", "(CR)", "commands.cps.loadbackup.help");
        Utils.sendMessage(sender, "/cps fix", "/cps fix - Fixes the config by replacing unicode characters (CR)", "(CR)", "commands.cps.fix.help");
        Utils.sendMessage(sender, "/cps vanish", "/cps vanish - Vanish utilities for the plugin. Doesn't manage other plugins. Provide \"show\" to show all vanished players (CR)", "(CR)", "commands.cps.vanish.help");
        Utils.sendMessage(sender, "/ping", "/ping [player name] - The name describes it all", "", "commands.ping.help");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
