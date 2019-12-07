package dev.demon.checks.movement.prediction;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.potion.PotionEffectType;

public class PredictionD extends Check {
    public PredictionD(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
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
                    double playerY = user.newTo.getY() - user.newFrom.getY();
                    if (user.player.getAllowFlight() || TimeUtils.elapsed(user.lastIce) < 1000L || TimeUtils.elapsed(user.lastCancelPlace) < 1200L || user.blockAboveTicks > 0 || user.iceTicks > 0 || user.slabTicks > 0 || user.stairTicks > 0) {
                        return;
                    }
                    double max = 0.335;
                    max += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.1;
                    if (playerY < 0 && MathUtil.getHorizontalDistance(user.newTo, user.newFrom) > max && TimeUtils.elapsed(user.lastFlightToggle) > 2000L && !user.onGround && user.climableTicks < 1 && user.liquidTicks < 1 && TimeUtils.elapsed(user.lastEntityVelocity) > 1000L && TimeUtils.elapsed(user.lastTeleport) > 1000L) {
                        flag(user);
                    }
                }
            }
        }
    }
}
