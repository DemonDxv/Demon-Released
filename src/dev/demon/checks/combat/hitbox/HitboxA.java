package dev.demon.checks.combat.hitbox;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;

public class HitboxA extends Check {
    public HitboxA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e){
        User user = e.getUser();
        if (user != null) {
           if (isPacketMovement(e.getType())) {
               if (TimeUtils.elapsed(user.lastAttackPacket) < 100L) {

               }
            }
        }
    }
}
