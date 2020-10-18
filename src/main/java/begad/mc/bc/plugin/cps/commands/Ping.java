package begad.mc.bc.plugin.cps.commands;

import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping extends Command {
    public Ping() {
        super("ping", "cps.ping");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                int ping = ((ProxiedPlayer) sender).getPing();
                Utils.sendMessage(sender, "", "Your ping is " + ping + "ms", ping + "ms", "commands.ping.own");
            } else {
                Utils.sendMessage(sender, "", "You must be a player", "", "commands.ping.error-not-player");
            }
        } else {
            try {
                int ping = ProxyServer.getInstance().getPlayer(args[0]).getPing();
                Utils.sendMessage(sender, args[0], args[0] + " ping is " + ping + " ms", ping + " ms", "commands.ping.others");
            } catch (Exception e) {
                Utils.sendMessage(sender, "", "That player is not online", "", "commands.ping.error-not-online");
            }
        }

    }
}
