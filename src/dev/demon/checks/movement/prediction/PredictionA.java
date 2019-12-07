package dev.demon.checks.movement.prediction;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.TimeUtils;

public class PredictionA extends Check {
    public PredictionA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onMove(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null && !user.teleportedNoMove) {
                if (TimeUtils.elapsed(user.lastLag) < 1000L || TimeUtils.elapsed(user.lastCancelPlace) < 1200L) {
                    return;
                }
                if (user.airTicks > 9 && user.player.isOnGround() && !user.onGround && user.groundTicks < 1 && user.liquidTicks < 1 && user.climableTicks < 1 && user.mountedTicks < 1 && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L && TimeUtils.elapsed(user.lastTeleport) > 2000L) {
                    flag(user);
                }
            }
        }
    }
}
