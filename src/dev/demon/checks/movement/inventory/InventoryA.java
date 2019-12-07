package dev.demon.checks.movement.inventory;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.TimeUtils;

public class InventoryA extends Check {
    public InventoryA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (isPacketMovement(e.getType())) {
                if (user.inventoryTicks > 15 && user.inventoryOpen && TimeUtils.elapsed(user.lastFullBlockMove) < 100L) {
                    flag(user);
                }
            }
        }
    }
}
