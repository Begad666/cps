package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.features.MultiProxy;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class Pull extends Command {
    public Pull() {
        super("pull");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                MultiProxy.pullData(sender);
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
            MultiProxy.pullData(sender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
