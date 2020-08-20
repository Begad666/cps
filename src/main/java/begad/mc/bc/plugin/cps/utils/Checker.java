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
        if (Type == CheckType.PERM) {
            if (player != null) {
                return player.hasPermission("cps.bypass");
            } else {
                throw new NullPointerException("player Cannot be null");
            }
        } else {
            ArrayList<String> allowedPlayers = (ArrayList) Core.getConfig().get().getStringList("allowed-players");
            if (player != null) {
                return Core.OnlineMode ? allowedPlayers.contains(player.getUniqueId().toString()) : allowedPlayers.contains(player.getName());
            } else {
                throw new NullPointerException("player Cannot be null");
            }
        }
    }

    public static boolean checkConnection(PendingConnection pendingConnection) {
        if (Type == CheckType.PERM) {
            throw new IllegalComponentStateException("Cannot check a permission on a connection");
        } else {
            ArrayList<String> allowedplayers = (ArrayList) Core.getConfig().get().getStringList("allowed-players");
            if (pendingConnection != null) {
                return Core.OnlineMode ? allowedplayers.contains(pendingConnection.getUniqueId().toString()) : allowedplayers.contains(pendingConnection.getName());
            } else {
                throw new NullPointerException("pendingConnection Cannot be null");
            }
        }
    }
}
