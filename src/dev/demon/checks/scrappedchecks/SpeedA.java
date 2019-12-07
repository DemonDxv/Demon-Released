package dev.demon.checks.scrappedchecks;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends Check {
    public SpeedA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (isPacketMovement(e.getType())) {
                double move = user.movementSpeed;
                double playerY = Math.abs(user.to.getY() - user.from.getY());

                double airlimit = 0.36D;
                double groundlimit = 0.2873D;
                double groundLimitJump = 0.614D;

                groundlimit += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;
                groundLimitJump += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;
                airlimit += MathUtil.getPotionEffectLevel(e.getPlayer(), PotionEffectType.SPEED) * 0.2;

                if (user.stairTicks > 0 || user.slabTicks > 0) {
                    groundlimit += 0.42D;
                    groundLimitJump += 0.09D;
                }
                if (TimeUtils.elapsed(user.lastIce) < 1000L && user.blockAboveTicks > 0) {
                    groundlimit += 1;
                    groundLimitJump += 1;
                    airlimit += 1;
                }
                if (user.blockAboveTicks > 0) {
                    groundlimit += 0.43;
                    groundLimitJump += 0.1;
                    airlimit += 0.4;
                }
                if (TimeUtils.elapsed(user.lastIce) < 1000L) {
                    airlimit += (0.98F * 0.91F);
                    groundlimit += (0.98F * 0.91F);
                    groundLimitJump += (0.98F * 0.91F);
                }
                if (TimeUtils.elapsed(user.lastJoin) < 10000L || TimeUtils.elapsed(user.lastTeleport) < 2000L || TimeUtils.elapsed(user.lastUnknownTeleport) < 2000L || user.isLagging()) {
                    return;
                }


                if (user.airTicks > 1 && move > airlimit || user.groundTicks > 10 && move > groundlimit && playerY <= 0 || move >= groundLimitJump) {
                //    Bukkit.broadcastMessage(""+move + " "+airlimit + " " + groundlimit + " "+groundLimitJump);
                    flag(user);
                }
            }
        }
    }
}
