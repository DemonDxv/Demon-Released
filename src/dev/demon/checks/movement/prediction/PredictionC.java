package dev.demon.checks.movement.prediction;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.location.CustomLocation;

public class PredictionC extends Check {
    public PredictionC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null && !user.teleportedNoMove) {
                if (TimeUtils.elapsed(user.lastLag) < 1000L) {
                    return;
                }
                if (user.newTo != null && user.newFrom != null) {
                    CustomLocation to = user.newTo, from = user.newFrom;
                    double speed = MathUtil.getHorizontalDistance(to, from);
                    if (speed == user.lastPredictedSpeed && speed > 0.2 && !MathUtil.nextToWall(user.player) && !user.onGround && user.liquidTicks < 1 && user.climableTicks < 1 && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L) {
                        flag(user);
                    }
                    user.lastPredictedSpeed = speed;
                }
            }
        }
    }
}
