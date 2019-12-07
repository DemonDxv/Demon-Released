package dev.demon.checks.movement.speed;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.potion.PotionEffectType;

public class SpeedB extends Check {
    public SpeedB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user.getNewFrom() != null && user.newTo != null) {
                CustomLocation from = user.newFrom, to = user.newTo;

                if (TimeUtils.secondsFromLong(user.lastTeleport) < 6L || TimeUtils.elapsed(user.lastFlightToggle) < 2000L || TimeUtils.secondsFromLong(user.lastLag2) < 5L || (System.currentTimeMillis() - user.lastLag) < 1000L || user.mountedTicks > 0 || user.getPlayer().getAllowFlight() || user.liquidTicks > 0 || (System.currentTimeMillis() - user.getLastEntityVelocity()) < 1000L || (System.currentTimeMillis() - user.getLastTeleport()) < 1000L) {
                    user.speedBVerbose = 0;
                    return;
                }

                double speed = Math.sqrt(Math.pow(to.getX() - from.getX(), 2.0) + Math.pow(to.getZ() - from.getZ(), 2.0));
                float friction = 0.91f;
                boolean onground = user.onGround;
                user.lastongroundspeed = speed;
                double shiffedspeed = user.lastongroundspeed * friction;
                double newdistance = user.lastongroundspeed - shiffedspeed;
                double updateddistance = newdistance * 112.0;
                boolean LastOnGround = user.lastonground;
                user.lastonground = onground;
                double max = 6.4;
                max += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;

                if (updateddistance >= max && onground && LastOnGround) {
                    if (user.speedBVerbose++ > 1) {
                        flag(user);
                    }
                } else user.speedBVerbose -= user.speedBVerbose > 0 ? 1 : 0;
            }
        }
    }
}
