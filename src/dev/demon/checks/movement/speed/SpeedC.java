package dev.demon.checks.movement.speed;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffectType;

public class SpeedC extends Check {
    public SpeedC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user.getNewFrom() != null && user.newTo != null) {
                CustomLocation from = user.newFrom, to = user.newTo;
                if (TimeUtils.elapsed(user.lastLag) < 1000L) {
                    return;
                }
                if (user.player.getGameMode().equals(GameMode.CREATIVE) || TimeUtils.elapsed(user.lastFlightToggle) < 2000L || user.halfBlockTicks > 0 || TimeUtils.elapsed(user.lastEntityVelocity) < 1000L || TimeUtils.elapsed(user.lastTeleport) < 2000L || user.player.isInsideVehicle() || user.player.getAllowFlight() || TimeUtils.elapsed(user.lastHillJump) < 1000L) {
                    return;
                }
                double speed = MathUtil.getHorizontalDistance(to, from);
                double maxSpeed = 0.2873D;
                if (user.iceTicks > 0) {
                    maxSpeed += (0.98F * 0.91F);
                }
                if (user.slabTicks > 0 || user.stairTicks > 0) {
                    maxSpeed += 0.33966671926;
                }
                if (user.blockAboveTicks > 0) {
                    return;
                }
                maxSpeed += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;
                if (!user.player.hasPotionEffect(PotionEffectType.SPEED) && user.speedPotionTicks > 0) {
                    return;
                }

                double diff = to.getY() - from.getY();
                if (speed > maxSpeed && user.groundTicks > 20 && user.airTicks < 1 && diff == 0) {
                    flag(user);
                }
            }
        }
    }
}