package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Map;

public class Fix extends Command {
    public Fix() {
        super("fix");
    }

    private static void replace(Map<Object, Object> map) {
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (entry.getKey() instanceof String) {
                if (Utils.isReplaceBlocked((String) entry.getKey())) {
                    continue;
                }
            }
            if (entry.getValue() instanceof Map) {
                replace((Map) entry.getValue());
            } else if (entry.getValue() instanceof ArrayList) {
                ArrayList value = (ArrayList) entry.getValue();
                for (int j = 0; j < value.size(); j++) {
                    Object i = value.get(j);
                    if (!(i instanceof String)) {
                        break;
                    }
                    value.set(j, Utils.replaceUnicodeCharacters((String) i));
                }
            } else if (entry.getValue() instanceof String) {
                entry.setValue(Utils.replaceUnicodeCharacters((String) entry.getValue()));
            }
        }
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (Checker.checkPlayer((ProxiedPlayer) sender)) {
                Map<Object, Object> map = Utils.dump(Core.getConfig().get());
                replace(map);
                Utils.undump(Core.getConfig().get(), map);
                Core.getConfig().save();
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
            Map<Object, Object> map = Utils.dump(Core.getConfig().get());
            replace(map);
            Utils.undump(Core.getConfig().get(), map);
            Core.getConfig().save();
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>();
    }
}
