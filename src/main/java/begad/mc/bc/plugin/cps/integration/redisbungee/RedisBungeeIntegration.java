package begad.mc.bc.plugin.cps.integration.redisbungee;

import begad.mc.bc.plugin.cps.Core;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class RedisBungeeIntegration {
    public final String channel = "cps";
    public final String splitter = "@";
    private boolean detected;
    private RedisBungeeAPI api;
    private RedisBungeeListener listener;

    public void init() {
        if (ProxyServer.getInstance().getPluginManager().getPlugin("RedisBungee") != null && (api = RedisBungee.getApi()) != null) {
            Core.getInstance().getLogger().info("RedisBungee detected");
            detected = true;
            listener = new RedisBungeeListener(this);
            api.registerPubSubChannels(channel);
            ProxyServer.getInstance().getPluginManager().registerListener(Core.getInstance(), listener);
        }
    }

    public boolean isDetected() {
        return detected;
    }

    public int getPlayerCount() {
        if (isDetected()) {
            return api.getPlayerCount();
        }
        return ProxyServer.getInstance().getOnlineCount();
    }

    public ArrayList<String> getPossiblePlayerNames(String name) {
        if (isDetected()) {
            return api.getHumanPlayersOnline().stream().filter(s -> s.startsWith(name)).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<String> matches = new ArrayList<>();
        Collection<ProxiedPlayer> possibleMatches = ProxyServer.getInstance().getPlayers().stream().filter(proxiedPlayer -> proxiedPlayer.getDisplayName().startsWith(name)).collect(Collectors.toCollection(ArrayList::new));
        for (ProxiedPlayer proxiedPlayer : possibleMatches) {
            matches.add(proxiedPlayer.getDisplayName());
        }
        return matches;
    }

    public ArrayList<String> getPlayerNames() {
        if (isDetected()) {
            Collection<String> possibleMatches = api.getHumanPlayersOnline();
            return new ArrayList<>(possibleMatches);
        }
        ArrayList<String> matches = new ArrayList<>();
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            matches.add(proxiedPlayer.getDisplayName());
        }
        return matches;
    }

    public ArrayList<UUID> getPossiblePlayerUUIDs(String name) {
        if (isDetected()) {
            return api.getPlayersOnline().stream().filter(uuid -> uuid.toString().startsWith(name)).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<UUID> uuids = new ArrayList<>();
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers().stream().filter(proxiedPlayer -> proxiedPlayer.getUniqueId().toString().startsWith(name)).collect(Collectors.toCollection(ArrayList::new))) {
            uuids.add(proxiedPlayer.getUniqueId());
        }
        return uuids;
    }

    public ArrayList<UUID> getPlayerUUIDs() {
        if (isDetected()) {
            return new ArrayList<>(api.getPlayersOnline());
        }
        ArrayList<UUID> uuids = new ArrayList<>();
        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            uuids.add(proxiedPlayer.getUniqueId());
        }
        return uuids;
    }

    public RedisBungeeAPI getApi() {
        return api;
    }

    public void sendMessage(RedisBungeeMessage message) {
        api.sendChannelMessage(channel, RedisBungeeMessage.encode(message, this));
    }

    public RedisBungeeListener getListener() {
        return this.listener;
    }

    public void stop() {
        if (isDetected()) {
            api.unregisterPubSubChannels(channel);
            ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
            listener = null;
        }
    }
}
