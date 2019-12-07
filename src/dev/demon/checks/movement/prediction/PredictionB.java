package dev.demon.checks.movement.prediction;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.location.CustomLocation;

public class PredictionB extends Check {
    public PredictionB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onMove(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null && !user.teleportedNoMove) {
                if (TimeUtils.elapsed(user.lastLag) < 1000L || TimeUtils.elapsed(user.lastCancelPlace) < 1200L || TimeUtils.elapsed(user.lastEntityVelocity) < 2000L || TimeUtils.elapsed(user.lastTeleport) < 5000L || TimeUtils.elapsed(user.lastUnknownTeleport) < 5000L || TimeUtils.elapsed(user.lastJoin) < 5000L) {
                    return;
                }
                CustomLocation to = user.newTo, from = user.newFrom;
                double moveSpeed = MathUtil.getHorizontalDistance(to.clone().toVector(), from.clone().toVector());
                if (user.player.isInsideVehicle() || user.mountedTicks > 0 || user.player.getAllowFlight() || TimeUtils.elapsed(user.lastFlightToggle) < 2000L) {
                    return;
                }
                if (Math.abs(moveSpeed - user.lastSpeed) > 1.5) {
                    if (0 - Math.abs(moveSpeed - user.lastSpeed) == Math.abs(moveSpeed) - user.lastSpeed && user.climableTicks < 1 && user.liquidTicks < 1 && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L && TimeUtils.elapsed(user.lastTeleport) > 1000L || Math.abs(moveSpeed - user.lastSpeed) == user.lastSpeed && user.climableTicks < 1 && user.liquidTicks < 1 && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L && TimeUtils.elapsed(user.lastTeleport) > 1000L) {
                           flag(user);
                    }
                }
                user.lastSpeed = moveSpeed;
            }
        }
    }
}
