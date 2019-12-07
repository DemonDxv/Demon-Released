package dev.demon.checks.movement.flight;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;
import org.bukkit.GameMode;

public class FlightC extends Check {
    public FlightC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null && !user.teleportedNoMove) {
                if (user.newFrom != null && user.newTo != null) {
                    if (TimeUtils.elapsed(user.lastLag) < 1000L || TimeUtils.elapsed(user.lastTeleport) < 2000L || TimeUtils.elapsed(user.lastCancelPlace) < 1200L) {
                        return;
                    }
                    if (user.onGround) {
                        user.lastOnGroundLocation = user.player.getLocation();
                    } else if (user.lastOnGroundLocation != null && user.climableTicks < 1 && user.player.getWorld() != null && user.player.getLocation().getWorld().equals(user.lastOnGroundLocation.getWorld()) && user.lastOnGroundLocation.getWorld().getName().equalsIgnoreCase(user.getPlayer().getLocation().getWorld().getName()) && TimeUtils.elapsed(user.lastFlightToggle) > 2000L && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L && user.lastOnGroundLocation != null && user.player.getLocation() != null && user.webTicks < 1 && user.airTicks > 10 && user.liquidTicks < 1 && !user.player.getAllowFlight() && !user.player.getGameMode().equals(GameMode.CREATIVE) && (user.getTo().getY() > user.getFrom().getY() || user.getTo().getY() == user.getFrom().getY() || Math.abs(user.getTo().getY() - user.getFrom().getY()) < 0.020) && user.player.getLocation().distance(user.lastOnGroundLocation) > 15) {
                        if (user.flightBVerbose++ > 1) {
                            flag(user);
                        }
                    } else user.flightBVerbose -= user.flightBVerbose > 0 ? 1 : 0;
                }
            }
        }
    }
}