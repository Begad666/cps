package begad.mc.bc.plugin.cps.vanish;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.integration.redisbungee.RedisBungeeCommands;
import begad.mc.bc.plugin.cps.integration.redisbungee.RedisBungeeMessage;
import begad.mc.bc.plugin.cps.utils.Permissions;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.stream.Collectors;

import static begad.mc.bc.plugin.cps.utils.Utils.getName;

public class VanishListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onStatusChange(PlayerVanishStatusEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (Core.getConfig().get().getBoolean("settings.vanish-log-console")) {
            if (event.nextStatus) {
                Core.getInstance().getLogger().info(Utils.getMessage(ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName(), ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName() + " has vanished by " + getName(event.otherUUID), getName(event.otherUUID), "vanish.log-vanished", false));
            } else {
                Core.getInstance().getLogger().info(Utils.getMessage(ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName(), ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName() + " has unvanished by " + getName(event.otherUUID), getName(event.otherUUID), "vanish.log-unvanished", false));
            }
        }
        if (Core.redisBungeeIntegration.isDetected()) {
            Core.redisBungeeIntegration.sendMessage(RedisBungeeMessage.construct(RedisBungeeCommands.VanishStatus, new String[]{event.uuid.toString(), event.nextStatus ? "vanish" : "unvanish"}, Core.redisBungeeIntegration));
        }
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers().stream().filter((p) -> p.hasPermission(Permissions.Vanish.LOG)).collect(Collectors.toList())) {
            if (event.nextStatus) {
                Utils.sendMessage(player, ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName(), ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName() + " has vanished by " + getName(event.otherUUID), getName(event.otherUUID), "vanish.log-vanished");
            } else {
                Utils.sendMessage(player, ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName(), ProxyServer.getInstance().getPlayer(event.uuid).getDisplayName() + " has unvanished by " + getName(event.otherUUID), getName(event.otherUUID), "vanish.log-unvanished");
            }
        }
    }
}
