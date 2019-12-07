package dev.demon.checks.combat.aimassist;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;

public class AimAssistE extends Check {
    public AimAssistE(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onMove(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                double yawDelta = user.newTo.getYaw() - user.newFrom.getYaw();
                if (yawDelta > 2 && yawDelta == user.aimassistDVal) {
                    if (user.aimassistEVerbose++ > 1) {
                        flag(user);
                    }
                } else user.aimassistEVerbose -= user.aimassistEVerbose > 0 ? 1 : 0;
                user.aimassistEVal = yawDelta;
            }
        }
    }
}
