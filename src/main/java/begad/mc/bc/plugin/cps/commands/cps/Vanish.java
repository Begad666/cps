package begad.mc.bc.plugin.cps.commands.cps;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Checker;
import begad.mc.bc.plugin.cps.utils.Permissions;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Vanish extends Command {
    public Vanish() {
        super("vanish");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> arrayList) {
        if ((sender instanceof ProxiedPlayer)) {
            if (sender.hasPermission(Permissions.Vanish.COMMAND) || Checker.checkPlayer((ProxiedPlayer) sender)) {
                if (arrayList.size() <= 0) {
                    if (sender.hasPermission(Permissions.Vanish.SELF)) {
                        boolean done;
                        ProxiedPlayer player = (ProxiedPlayer) sender;
                        if (Core.vanishManager.isVanished(player)) {
                            done = Core.vanishManager.unVanish(player.getUniqueId(), null, false);
                        } else {
                            done = Core.vanishManager.vanish(player.getUniqueId(), null, false);
                        }
                        if (done) {
                            if (Core.vanishManager.isVanished(player)) {
                                Utils.sendMessage(sender, "", "You have vanished into the shadows", "", "vanish.vanished");
                            } else {
                                Utils.sendMessage(sender, "", "You have unvanished into the sunlight", "", "vanish.unvanished");
                            }
                        } else {
                            Utils.sendMessage(sender, "", "A plugin denied the status change", "", "vanish.cancelled");
                        }
                    } else {
                        Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                    }
                } else {
                    if (arrayList.get(0).equals("show")) {
                        if (sender.hasPermission(Permissions.Vanish.SEE)) {
                            StringBuilder msg = new StringBuilder(Utils.getMessage("", "Showing all vanished players: ", ": ", "commands.cps.vanish.show", false));
                            Map<UUID, Boolean> map = Core.vanishManager.getPlayers().entrySet().stream().filter(Map.Entry::getValue).collect(Collectors.toMap(Map.Entry::getKey, uuidBooleanEntry -> true));
                            if (map.size() > 0) {
                                for (Map.Entry<UUID, Boolean> entry : map.entrySet()) {
                                    msg.append("\n").append(Utils.getName(entry.getKey()));
                                }
                            } else {
                                msg.append("There are no vanished players");
                            }
                            Utils.sendMessage(sender, msg.toString());
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    } else {
                        if (sender.hasPermission(Permissions.Vanish.OTHERS)) {
                            try {
                                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(arrayList.get(0));
                                boolean done;
                                if (Core.vanishManager.isVanished(player)) {
                                    done = Core.vanishManager.unVanish(player.getUniqueId(), ((ProxiedPlayer) sender).getUniqueId(), false);
                                } else {
                                    done = Core.vanishManager.vanish(player.getUniqueId(), ((ProxiedPlayer) sender).getUniqueId(), false);
                                }
                                if (done) {
                                    if (Core.vanishManager.isVanished(player)) {
                                        Utils.sendMessage(sender, player.getDisplayName(), player.getDisplayName() + " has vanished into the shadows by you", "", "vanish.vanished-other");
                                        if (Core.getConfig().get().getBoolean("settings.vanish-others-message")) {
                                            Utils.sendMessage(player, "", "You have vanished into the shadows by " + ((ProxiedPlayer) sender).getDisplayName(), ((ProxiedPlayer) sender).getDisplayName(), "vanish.vanished-other-me");
                                        }
                                    } else {
                                        Utils.sendMessage(sender, player.getDisplayName(), player.getDisplayName() + " has unvanished into the sunlight by you", "", "vanish.unvanished-other");
                                        if (Core.getConfig().get().getBoolean("settings.vanish-others-message")) {
                                            Utils.sendMessage(player, "", "You have unvanished into the sunlight by " + ((ProxiedPlayer) sender).getDisplayName(), ((ProxiedPlayer) sender).getDisplayName(), "vanish.unvanished-other-me");
                                        }
                                    }
                                } else {
                                    Utils.sendMessage(sender, "", "A plugin denied the status change", "", "vanish.cancelled");
                                }
                            } catch (Exception e) {
                                if (Core.redisBungeeIntegration.getPlayerNames().contains(arrayList.get(0))) {
                                    UUID uuid = Core.redisBungeeIntegration.getApi().getUuidFromName(arrayList.get(0), false);
                                    boolean done;
                                    if (Core.vanishManager.isVanished(uuid)) {
                                        done = Core.vanishManager.unVanish(uuid, ((ProxiedPlayer) sender).getUniqueId(), false);
                                    } else {
                                        done = Core.vanishManager.vanish(uuid, ((ProxiedPlayer) sender).getUniqueId(), false);
                                    }
                                    if (done) {
                                        if (Core.vanishManager.isVanished(uuid)) {
                                            Utils.sendMessage(sender, arrayList.get(0), arrayList.get(0) + " has vanished into the shadows by you", "", "vanish.vanished-other");
                                        } else {
                                            Utils.sendMessage(sender, arrayList.get(0), arrayList.get(0) + " has unvanished into the sunlight by you", "", "vanish.unvanished-other");
                                        }
                                    } else {
                                        Utils.sendMessage(sender, "", "A plugin denied the status change", "", "vanish.cancelled");
                                    }
                                } else {
                                    Utils.sendMessage(sender, "", "That player is not online", "", "vanish.not-online-error");
                                }
                            }
                        } else {
                            Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
                        }
                    }
                }
            } else {
                Utils.sendMessage(sender, "", "You can't use that command", "", "commands.cps.not-allowed");
            }
        } else {
            if (arrayList.size() <= 0) {
                Utils.sendMessage(sender, "", "You must be a player", "", "vanish.not-player-error");
            } else {
                try {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(arrayList.get(0));
                    boolean done;
                    if (Core.vanishManager.isVanished(player)) {
                        done = Core.vanishManager.unVanish(player.getUniqueId(), Utils.CONSOLE_UUID, false);
                    } else {
                        done = Core.vanishManager.vanish(player.getUniqueId(), Utils.CONSOLE_UUID, false);
                    }
                    if (done) {
                        if (Core.vanishManager.isVanished(player)) {
                            Utils.sendMessage(sender, player.getDisplayName(), player.getDisplayName() + " has vanished into the shadows by you", "", "vanish.vanished-other");
                            if (Core.getConfig().get().getBoolean("settings.vanish-others-message")) {
                                Utils.sendMessage(player, "", "You have vanished into the shadows by " + Utils.CONSOLE.get(), Utils.CONSOLE.get(), "vanish.vanished-other-me");
                            }
                        } else {
                            Utils.sendMessage(sender, player.getDisplayName(), player.getDisplayName() + " has unvanished into the sunlight by you", "", "vanish.unvanished-other");
                            if (Core.getConfig().get().getBoolean("settings.vanish-others-message")) {
                                Utils.sendMessage(player, "", "You have unvanished into the sunlight by " + Utils.CONSOLE.get(), Utils.CONSOLE.get(), "vanish.unvanished-other-me");
                            }
                        }
                    } else {
                        Utils.sendMessage(sender, "", "A plugin denied the status change", "", "vanish.cancelled");
                    }
                } catch (Exception e) {
                    if (Core.redisBungeeIntegration.getPlayerNames().contains(arrayList.get(0))) {
                        UUID uuid = Core.redisBungeeIntegration.getApi().getUuidFromName(arrayList.get(0), false);
                        boolean done;
                        if (Core.vanishManager.isVanished(uuid)) {
                            done = Core.vanishManager.unVanish(uuid, Utils.CONSOLE_UUID, false);
                        } else {
                            done = Core.vanishManager.vanish(uuid, Utils.CONSOLE_UUID, false);
                        }
                        if (done) {
                            if (Core.vanishManager.isVanished(uuid)) {
                                Utils.sendMessage(sender, arrayList.get(0), arrayList.get(0) + " has vanished into the shadows by you", "", "vanish.vanished-other");
                            } else {
                                Utils.sendMessage(sender, arrayList.get(0), arrayList.get(0) + " has unvanished into the sunlight by you", "", "vanish.unvanished-other");
                            }
                        } else {
                            Utils.sendMessage(sender, "", "A plugin denied the status change", "", "vanish.cancelled");
                        }
                    } else {
                        Utils.sendMessage(sender, "", "That player is not online", "", "vanish.not-online-error");
                    }
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return new ArrayList<>(Collections.singletonList("show"));
    }
}
