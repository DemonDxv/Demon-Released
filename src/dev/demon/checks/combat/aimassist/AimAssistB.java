package dev.demon.checks.combat.aimassist;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import sun.awt.image.codec.JPEGImageDecoderImpl;

public class AimAssistB extends Check {
    public AimAssistB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                double pitch = Math.abs(user.newTo.getPitch() - user.newFrom.getPitch());
                if (pitch % 1.0 == 0 && pitch > 0 && pitch < 90) {
                    if (user.aimassistBVerbose++ > 3) {
                        flag(user);
                        user.aimassistBVerbose = 0;
                    }
                }else user.aimassistBVerbose -= user.aimassistBVerbose > 0 ? 1 : 0;
            }
        }
    }
}
