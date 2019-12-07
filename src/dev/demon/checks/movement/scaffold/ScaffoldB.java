package dev.demon.checks.movement.scaffold;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;

public class ScaffoldB extends Check {
    public ScaffoldB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
            User user = e.getUser();
            if (user != null) {
                if (e.getPlayer().getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid() && !e.getPlayer().getLocation().clone().subtract(0, 2, 0).getBlock().getType().isSolid()) {
                    if (e.getPlayer().isSprinting()) {
                        if (user.scaffoldBVerbose.flag(5, 750L)) {
                            flag(user);
                        }
                    }
                }
            }
        }
    }
}
