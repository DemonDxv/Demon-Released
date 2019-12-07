package dev.demon.checks.movement.speed;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

public class SpeedE extends Check {
    public SpeedE(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                if (user.stairTicks > 0 || user.slabTicks > 0 || user.climableTicks > 0 || user.fenceTicks > 0 || user.wallTicks > 0 || TimeUtils.elapsed(user.lastTeleport) < 2000L || TimeUtils.elapsed(user.lastJoin) < 2000L) {
                    return;
                }
                double max = 0.614;
                max+= MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;
                if (user.onGround && user.movementSpeed >= max) {
                    flag(user);
                }
            }
        }
    }
}
