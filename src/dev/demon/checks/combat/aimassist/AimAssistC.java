package dev.demon.checks.combat.aimassist;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;

public class AimAssistC extends Check {
    public AimAssistC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                double yaw = Math.abs(user.newTo.getYaw() - user.newFrom.getYaw());
                if (yaw % 1.0 == 0 && yaw > 0 && yaw < 90) {
                    if (user.aimassistCVerbose++ > 3) {
                        flag(user);
                        user.aimassistCVerbose = 0;
                    }
                }else user.aimassistCVerbose -= user.aimassistCVerbose > 0 ? 1 : 0;
            }
        }
    }
}
