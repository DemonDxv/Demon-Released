package dev.demon.checks.combat.killaura;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;
import org.bukkit.potion.PotionEffectType;

public class KillauraB extends Check {
    public KillauraB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                if (TimeUtils.elapsed(user.lastAttackPacket) < 100L) {
                    double baseSpeed = 0.26f + (MathUtil.getPotionEffectLevel(user.player, PotionEffectType.SPEED) * 0.062f) + ((user.player.getWalkSpeed() - 0.2f) * 1.6f);;
                    if (user.onGround && user.player.isSprinting() && (user.movementSpeed > baseSpeed)) {
                        if (user.killauraGVerbose.flag(10, 810L)) {
                            flag(user);
                        }
                    }
                }
            }
        }
    }
}