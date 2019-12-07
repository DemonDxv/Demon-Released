package dev.demon.checks.misc.impossible;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;

public class ImpossibleA extends Check {
    public ImpossibleA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (isPacketMovement(e.getType())) {
                if (Math.abs(user.getPlayer().getLocation().getPitch()) > 90) {
                    flag(user);
                }
            }
        }
    }
}
