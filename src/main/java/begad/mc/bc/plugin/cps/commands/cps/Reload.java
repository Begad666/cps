package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class Reload extends Command {
    public Reload() {
        super("reload");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                Core.reload(sender);
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
            Core.reload(sender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
