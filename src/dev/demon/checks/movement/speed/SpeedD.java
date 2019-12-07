package dev.demon.checks.movement.speed;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffectType;

public class SpeedD extends Check {
    public SpeedD(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onMove(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (TimeUtils.elapsed(user.lastLag) < 1000L) {
                return;
            }
            if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || TimeUtils.elapsed(user.lastFlightToggle) < 2000L || user.isOnSlime || user.liquidTicks > 0 || TimeUtils.elapsed(user.lastTeleport) < 2000L || user.player.isInsideVehicle() || e.getPlayer().getAllowFlight() || TimeUtils.elapsed(user.lastEntityVelocity) < 1000L) {
                return;
            }
            float threshold = (float) (user.airTicks > 0 ? user.airTicks < 0 ? 0.4163 * Math.pow(0.984, user.airTicks) : 0.4163 * Math.pow(0.984, 9) : user.groundTicks > 24 ? 0.291 : 0.375);
            if (user.slabTicks > 0 || user.stairTicks > 0) {
                threshold += 0.3;
            }
            if (user.blockAboveTicks > 0 && user.iceTicks < 1) {
                threshold += 0.4;
            }
            if (user.iceTicks > 0 && user.blockAboveTicks > 0) {
                threshold += 1.1;
            }
            if (TimeUtils.elapsed(user.lastHillJump) < 1000L) {
                threshold += 0.4;
            }
            if ((System.currentTimeMillis() - user.lastIce) < 1000L) {
                threshold += 0.4;
            }
            threshold += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;
            if (!user.player.hasPotionEffect(PotionEffectType.SPEED) && user.speedPotionTicks > 0) {
                return;
            }
            //Get the speed of the player
            double deltaX = user.getTo().getX() - user.getFrom().getX(), deltaZ = user.getTo().getZ() - user.getFrom().getZ();
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            if (horizontalDistance > threshold) {
                if (user.speedDVerbose++ > 1) {
                    flag(user);
                    user.speedDVerbose = 0;
                }
            } else user.speedDVerbose -= user.speedDVerbose > 0 ? 1 : 0;
        }
    }
}
