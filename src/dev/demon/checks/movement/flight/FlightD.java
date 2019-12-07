package dev.demon.checks.movement.flight;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.potion.PotionEffectType;

public class FlightD extends Check {
    public FlightD(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                float diff = (float) (user.to.getY() - user.from.getY());
                boolean ground = user.player.isOnGround();
                if (TimeUtils.elapsed(user.lastEntityVelocity) < 1000L || user.isLagging() || user.liquidTicks > 0 || user.climableTicks > 0 || user.webTicks > 0 || user.player.getAllowFlight() || user.blockAboveTicks > 0) {
                    return;
                }
                float playerY = 0.42f;
                if (user.slabTicks > 0 || user.stairTicks > 0 || user.wallTicks > 0) {
                    playerY = (float) 0.5;
                }
                playerY += MathUtil.getPotionEffectLevel(user.player, PotionEffectType.JUMP) * 0.1f;
                if (user.lastClientGround && !ground) {
                    if (diff <= 0 || diff == 0.7532f || diff == 0.248136f || diff <= 0.404445 && diff > 0.404444) {
                        return;
                    }
                    if (diff > playerY || diff < playerY && user.slabTicks < 1 && user.stairTicks < 1 && user.wallTicks < 1) {
                        if (user.gravityVerbose++ > 1) {
                            flag(user);
                            user.gravityVerbose = 0;
                        }
                    }else user.gravityVerbose -= user.gravityVerbose > 0 ? 1 : 0;
                }
                user.lastClientGround = ground;
            }
        }
    }
}
