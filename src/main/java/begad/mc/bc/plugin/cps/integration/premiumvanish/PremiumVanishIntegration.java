package begad.mc.bc.plugin.cps.integration.premiumvanish;

import begad.mc.bc.plugin.cps.Core;
import net.md_5.bungee.api.ProxyServer;

public class PremiumVanishIntegration {
    private boolean detected;
    private PremiumVanishListener listener;

    public void init() {
        if (ProxyServer.getInstance().getPluginManager().getPlugin("PremiumVanish") != null) {
            Core.getInstance().getLogger().info("PremiumVanish detected");
            detected = true;
            listener = new PremiumVanishListener(this);
            ProxyServer.getInstance().getPluginManager().registerListener(Core.getInstance(), listener);
        }
    }

    public boolean isDetected() {
        return detected;
    }

    public PremiumVanishListener getListener() {
        return this.listener;
    }

    public void stop() {
        if (isDetected()) {
            ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
            listener = null;
        }
    }
}
