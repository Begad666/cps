package begad.mc.bc.plugin.cps.utils;

import begad.mc.bc.plugin.cps.Core;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.ArrayList;

public class Checker {
    public static CheckType Type;

    public Checker() {
    }

    public static boolean checkPlayer(ProxiedPlayer player) {
        if (player == null) {
            throw new NullPointerException("player Cannot be null");
        }
        switch (Type) {
            case PERM: {
                return player.hasPermission("cps.bypass");
            }
            case CONFIG_ALLOWED_PLAYERS_UUID: {
                ArrayList<String> allowedPlayers = (ArrayList<String>) Core.getConfig().get().getStringList("allowed-players");
                return allowedPlayers.contains(player.getUniqueId().toString());
            }
            case CONFIG_ALLOWED_PLAYERS_USERNAMES: {
                ArrayList<String> allowedPlayers = (ArrayList<String>) Core.getConfig().get().getStringList("allowed-players");
                return allowedPlayers.contains(player.getName());
            }
        }

        return false;
    }

    public static boolean checkConnection(PendingConnection pendingConnection) {
        if (pendingConnection == null) {
            throw new NullPointerException("pendingConnection Cannot be null");
        }
        switch (Type) {
            case PERM: {
                throw new IllegalComponentStateException("Cannot check a permission on a connection");
            }
            case CONFIG_ALLOWED_PLAYERS_UUID: {
                ArrayList<String> allowedPlayers = (ArrayList<String>) Core.getConfig().get().getStringList("allowed-players");
                return allowedPlayers.contains(pendingConnection.getUniqueId().toString());
            }
            case CONFIG_ALLOWED_PLAYERS_USERNAMES: {
                ArrayList<String> allowedPlayers = (ArrayList<String>) Core.getConfig().get().getStringList("allowed-players");
                return allowedPlayers.contains(pendingConnection.getName());
            }
        }

        return false;
    }
}
