package dev.demon.checks.scrappedchecks;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.location.CustomLocation;

public class SpeedB extends Check {
    public SpeedB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user.getNewFrom() != null && user.newTo != null) {
                CustomLocation from = user.getNewFrom(), to = user.newTo;
                double distX = to.getX() - from.getX();
                double distZ = to.getZ() - from.getZ();
                double dist = (distX * distX) + (distZ * distZ);

                if (TimeUtils.secondsFromLong(user.lastTeleport) < 6L || TimeUtils.elapsed(user.lastFlightToggle) < 2000L || TimeUtils.secondsFromLong(user.lastLag2) < 5L || (System.currentTimeMillis() - user.lastLag) < 1000L || user.mountedTicks > 0 || user.getPlayer().getAllowFlight() || user.liquidTicks > 0 || (System.currentTimeMillis() - user.getLastEntityVelocity()) < 1000L || (System.currentTimeMillis() - user.getLastTeleport()) < 1000L) {
                    user.speedAVerbose = 0;
                    return;
                }

                double lastDist = user.lastDist;
                user.lastDist = dist;
                boolean onGround = user.onGround, lastOnGround = user.lastGround;
                user.lastGround = onGround;
                double friction = 0.91F;
                double shiftDist = lastDist * friction;
                double equalness = dist - shiftDist;
                double fix = equalness * 138;

                if (fix >= 1.0 && fix < 30 && !onGround && !lastOnGround && user.getNewTo().getY() != user.getNewFrom().getY()) {
                    flag(user);
                }
            }
        }
    }
}
