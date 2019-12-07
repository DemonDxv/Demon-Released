package dev.demon.checks.movement.flight;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;
import org.bukkit.GameMode;

public class FlightA extends Check {
    public FlightA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (isPacketMovement(e.getType())) {
                if (TimeUtils.elapsed(user.lastEntityVelocity) < 1000L || user.isLagging() || user.liquidTicks > 0 || TimeUtils.elapsed(user.lastCancelPlace) < 1200L || user.climableTicks > 0 || user.player.getAllowFlight() || user.player.getGameMode().equals(GameMode.CREATIVE) || TimeUtils.elapsed(user.lastTeleport) < 2000L) {
                    return;
                }
                double distY = user.newTo.getY() - user.newFrom.getY();
                double lastY = user.lastY;
                user.lastY = distY;
                double predictedDist = (lastY - 0.08D) * 0.9800000190734863D;
                boolean onGround = user.onGround;
                boolean lastGround = user.lastGround2;
                user.lastGround2 = onGround;
                boolean lastLastGround = user.lastGroundGround;
                user.lastGroundGround = lastGround;

                if (!onGround && !lastGround && !lastLastGround && Math.abs(predictedDist) >= 0.005D) {
                    if (Math.abs(distY - predictedDist) > 0.001) {
                        if (user.flightGVerbose++ > 2) {
                            flag(user);
                        }
                    }else user.flightGVerbose -= user.flightGVerbose > 0 ? 1 : 0;
                }
            }
        }
    }
}