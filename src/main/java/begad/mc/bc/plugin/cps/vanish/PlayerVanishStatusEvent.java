package begad.mc.bc.plugin.cps.vanish;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class PlayerVanishStatusEvent extends Event implements Cancellable {
    public final UUID uuid;
    public final UUID otherUUID;
    public final boolean nextStatus;
    public final boolean currentStatus;
    private boolean cancelled;

    public PlayerVanishStatusEvent(UUID player, UUID otherUUID, boolean nextStatus) {
        this.uuid = player;
        this.otherUUID = otherUUID;
        this.nextStatus = nextStatus;
        this.currentStatus = !nextStatus;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
