package begad.mc.bc.plugin.cps.commands;

import begad.mc.bc.commands.CommandGroup;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.commands.cps.*;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CPS extends CommandGroup {
    private final static Supplier<String> NotFound = () -> Utils.getMessage("", "Command not found, use /cps help for all commands", "", "commands.cps.not-found-use-help", true);

    public CPS() {
        super("cps", "cps.admin", null, NotFound.get(), true);
        this.commands.put("help", new Help());
        this.commands.put("reload", new Reload());
        this.commands.put("pull", new Pull());
        this.commands.put("push", new Push());
        this.commands.put("dbconnected", new DbConnected());
        this.commands.put("createbackup", new CreateBackup());
        this.commands.put("loadbackup", new LoadBackup());
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        Utils.sendMessage(sender, "------------------------------");
        Utils.sendMessage(sender, "", "Compile Version: " + Core.getUpdates().getCompileCurrentVersion(), Core.getUpdates().getCompileCurrentVersion(), "commands.cps.default.compile-version");
        Utils.sendMessage(sender, "", "Version: " + Core.getInstance().getDescription().getVersion(), Core.getInstance().getDescription().getVersion(), "commands.cps.default.current-version");
        Utils.sendMessage(sender, "", "Compile Config Version: " + Core.getUpdates().getCompileConfigVersion(), Core.getUpdates().getCompileConfigVersion(), "commands.cps.default.compile-config-version");
        Utils.sendMessage(sender, "", "Config Version: " + Core.getConfig().get().getString("config-version"), Core.getConfig().get().getString("config-version"), "commands.cps.default.current-config-version");
        Utils.sendMessage(sender, "", "Updates: " + Core.getUpdates().getMessage(), Core.getUpdates().getMessage(), "commands.cps.default.updates");
        TextComponent plug;
        if (sender instanceof ProxiedPlayer) {
            plug = new TextComponent(Utils.getMessage("", "Plugin Page: Click Here", "", "commands.cps.default.plugin-page-click", true));
            plug.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/69385/"));
        } else {
            plug = new TextComponent(Utils.getMessage("", "Plugin Page: https://www.spigotmc.org/resources/69385/", "https://www.spigotmc.org/resources/69385/", "commands.cps.default.plugin-page-link", true));
        }
        sender.sendMessage(plug);
        Utils.sendMessage(sender, "------------------------------");
    }
}
