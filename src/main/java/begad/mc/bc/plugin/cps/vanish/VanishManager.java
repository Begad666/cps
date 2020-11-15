package begad.mc.bc.plugin.cps.vanish;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VanishManager {
    private Map<UUID, Boolean> players = new HashMap<>();

    public boolean vanish(UUID uuid, UUID otherUuid, boolean external) {
        if (!external) {
            PlayerVanishStatusEvent event = new PlayerVanishStatusEvent(uuid, otherUuid, true);
            ProxyServer.getInstance().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }
        this.players.put(uuid, true);
        return true;
    }

    public boolean vanish(ProxiedPlayer player, ProxiedPlayer otherPlayer, boolean external) {
        return this.vanish(player.getUniqueId(), otherPlayer.getUniqueId(), external);
    }

    public boolean unVanish(UUID uuid, UUID otherUuid, boolean external) {
        if (!external) {
            PlayerVanishStatusEvent event = new PlayerVanishStatusEvent(uuid, otherUuid, false);
            ProxyServer.getInstance().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }
        this.players.put(uuid, false);
        return true;
    }

    public boolean unVanish(ProxiedPlayer player, ProxiedPlayer otherPlayer, boolean external) {
        return this.unVanish(player.getUniqueId(), otherPlayer.getUniqueId(), external);
    }

    public boolean isVanished(UUID uuid) {
        return this.players.containsKey(uuid) && this.players.get(uuid);
    }

    public boolean isVanished(ProxiedPlayer player) {
        return this.isVanished(player.getUniqueId());
    }

    public Map<UUID, Boolean> getPlayers() {
        return Collections.unmodifiableMap(this.players);
    }

    public boolean dump(File file) {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {
                objectOutputStream.writeObject(this.players);
                objectOutputStream.flush();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadFile(File file) {
        try (FileInputStream stream = new FileInputStream(file)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {
                this.players = (Map<UUID, Boolean>) objectInputStream.readObject();
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
