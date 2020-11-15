package begad.mc.bc.plugin.cps.integration.redisbungee;

import begad.mc.bc.plugin.cps.Core;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisBungeeListener implements Listener {
    private final Map<String, ArrayList<IPingCallback>> pingRequests = new HashMap<>();
    private final RedisBungeeIntegration integration;

    public RedisBungeeListener(RedisBungeeIntegration integration) {
        this.integration = integration;
    }

    @EventHandler
    public void onMessage(final PubSubMessageEvent event) {
        if (!event.getChannel().equals(integration.channel)) return;
        RedisBungeeMessage message = RedisBungeeMessage.decode(event.getMessage(), integration.splitter);
        if (message != null && !message.serverId.equals(integration.getApi().getServerId())) {
            switch (message.command) {
                case RedisBungeeCommands.Ping: {
                    try {
                        int ping = ProxyServer.getInstance().getPlayer(message.body.get(2)).getPing();
                        integration.sendMessage(RedisBungeeMessage.construct(RedisBungeeCommands.PingResult, new String[]{message.body.get(0), message.body.get(2), String.valueOf(ping)}, integration));
                    } catch (Exception e) {
                        return;
                    }
                    break;
                }
                case RedisBungeeCommands.PingResult: {
                    if (message.body.get(2).equalsIgnoreCase(integration.getApi().getServerId())) {
                        for (IPingCallback callback : pingRequests.get(message.body.get(4))) {
                            callback.finish(message.body.get(3), Integer.parseInt(message.body.get(4)));
                        }
                        pingRequests.get(message.body.get(3)).clear();
                    }
                    break;
                }

                case RedisBungeeCommands.VanishStatus: {
                    String uuid = message.body.get(0);
                    String status = message.body.get(1);
                    if (status.equals("vanish")) {
                        Core.vanishManager.vanish(UUID.fromString(uuid), null, true);
                    } else if (status.equals("unvanish")) {
                        Core.vanishManager.unVanish(UUID.fromString(uuid), null, true);
                    } else {
                        Core.getInstance().getLogger().warning("Unknown vanish status " + status + " for uuid " + uuid);
                    }
                    break;
                }

                default: {
                    Core.getInstance().getLogger().warning("Received an unknown message: " + message.command);
                    break;
                }
            }
        }
    }

    public void requestPing(String name, IPingCallback callback) {
        pingRequests.computeIfAbsent(name, k -> new ArrayList<>());
        pingRequests.get(name).add(callback);
        if (pingRequests.get(name).size() <= 1) {
            integration.sendMessage(RedisBungeeMessage.construct(RedisBungeeCommands.Ping, new String[]{name}, integration));
        }
    }

    public interface IPingCallback {
        void finish(String name, int ping);
    }
}
