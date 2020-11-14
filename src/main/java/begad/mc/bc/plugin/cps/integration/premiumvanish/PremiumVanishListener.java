package begad.mc.bc.plugin.cps.integration.premiumvanish;

import begad.mc.bc.plugin.cps.Core;
import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PremiumVanishListener implements Listener {
    private final PremiumVanishIntegration integration;

    public PremiumVanishListener(PremiumVanishIntegration integration) {
        this.integration = integration;
    }

    @EventHandler
    public void onVanish(BungeePlayerHideEvent event) {
        Core.vanishManager.vanish(event.getPlayer().getUniqueId(), null, true);
    }

    @EventHandler
    public void onUnVanish(BungeePlayerShowEvent event) {
        Core.vanishManager.unVanish(event.getPlayer().getUniqueId(), null, true);
    }
}
