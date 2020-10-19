package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;

public class DbConnected extends Command {
    public DbConnected() {
        super("dbconnected");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if (Core.getDatabaseManager().isStarted()) {
            Utils.sendMessage(sender, "", "Connected", "", "commands.cps.dbconnected.connected");
        } else {
            Utils.sendMessage(sender, "", "Disconnected", "", "commands.cps.dbconnected.disconnected");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
