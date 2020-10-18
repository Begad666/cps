//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package begad.mc.bc.plugin.cps.features;

import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

public class DisconnectNotAllowedUsers {
    public DisconnectNotAllowedUsers() {
    }

    public static class PermBased implements Listener {
        public PermBased() {
        }

        @EventHandler
        public void afterLoginChecks(PostLoginEvent event) {
            if (!Core.getConfig().get().getBoolean("settings.allow-all-versions")) {
                int protocol = event.getPlayer().getPendingConnection().getVersion();
                ArrayList<Integer> allowedprotocols = (ArrayList<Integer>) Core.getConfig().get().getIntList("settings.allowed-protocols");
                int defpro = Core.getConfig().get().getInt("settings.default-protocol");
                if (!allowedprotocols.contains(protocol) && defpro != protocol) {
                    event.getPlayer().getPendingConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.not-supported-client-message"))));
                    return;
                }
            }
            if (Core.getConfig().get().getBoolean("settings.maintenance-enabled")) {
                if (!Checker.checkPlayer(event.getPlayer())) {
                    event.getPlayer().getPendingConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.maintenance-message"))));
                }
            } else {
                if (Core.getConfig().get().getInt("network-info.max-players") > 0 && ProxyServer.getInstance().getOnlineCount() >= Core.getConfig().get().getInt("network-info.max-players")) {
                    if (!Checker.checkPlayer(event.getPlayer())) {
                        event.getPlayer().getPendingConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.full-message"))));
                    }
                }
            }
        }
    }

    public static class AllowedPlayersBased {
        public AllowedPlayersBased() {
        }

        public static class UUIDBased implements Listener {
            public UUIDBased() {
            }

            @EventHandler
            public void loginChecks(LoginEvent event) {
                if(event.isCancelled()) {
                    return;
                }
                
                if (!Core.getConfig().get().getBoolean("settings.allow-all-versions")) {
                    int protocol = event.getConnection().getVersion();
                    ArrayList<Integer> allowedprotocols = (ArrayList<Integer>) Core.getConfig().get().getIntList("settings.allowed-protocols");
                    int defpro = Core.getConfig().get().getInt("settings.default-protocol");
                    if (!allowedprotocols.contains(protocol) && defpro != protocol) {
                        event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.not-supported-client-message"))));
                        return;
                    }
                }
                if (Core.getConfig().get().getBoolean("settings.maintenance-enabled")) {
                    if (!Checker.checkConnection(event.getConnection())) {
                        event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.maintenance-message"))));
                    }
                } else {
                    if (Core.getConfig().get().getInt("network-info.max-players") > 0 && ProxyServer.getInstance().getOnlineCount() >= Core.getConfig().get().getInt("network-info.max-players")) {
                        if (!Checker.checkConnection(event.getConnection())) {
                            event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.full-message"))));
                        }
                    }
                }
            }
        }

        public static class UsernameBased implements Listener {
            public UsernameBased() {
            }

            @EventHandler
            public void loginChecks(LoginEvent event) {
                if(event.isCancelled()) {
                    return;
                }
                
                if (!Core.getConfig().get().getBoolean("settings.allow-all-versions")) {
                    int protocol = event.getConnection().getVersion();
                    ArrayList<Integer> allowedprotocols = (ArrayList<Integer>) Core.getConfig().get().getIntList("settings.allowed-protocols");
                    int defpro = Core.getConfig().get().getInt("settings.default-protocol");
                    if (!allowedprotocols.contains(protocol) && defpro != protocol) {
                        event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.not-supported-client-message"))));
                        return;
                    }
                }
                if (Core.getConfig().get().getBoolean("settings.maintenance-enabled")) {
                    if (!Checker.checkConnection(event.getConnection())) {
                        event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.maintenance-message"))));
                    }
                } else {
                    if (Core.getConfig().get().getInt("network-info.max-players") > 0 && ProxyServer.getInstance().getOnlineCount() >= Core.getConfig().get().getInt("network-info.max-players")) {
                        if (!Checker.checkConnection(event.getConnection())) {
                            event.getConnection().disconnect(new TextComponent(Utils.replaceEveryThing(Core.getConfig().get().getString("messages.full-message"))));
                        }
                    }
                }
            }
        }
    }
}
