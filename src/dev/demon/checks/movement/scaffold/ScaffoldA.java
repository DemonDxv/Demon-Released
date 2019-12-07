package dev.demon.checks.movement.scaffold;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.TimeUtils;
import org.bukkit.Bukkit;

public class ScaffoldA extends Check {
    public ScaffoldA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (user.isLagging()) {
                user.scaffoldAVerbose.setVerbose(0);
                return;
            }
            if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
                    if (TimeUtils.elapsed(user.lastPostLook) < 30) {
                        if (user.scaffoldAVerbose.flag(7, 750L)) {
                            flag(user);
                        }
                    }
            } else if (e.getType().equalsIgnoreCase(Packet.Client.POSITION_LOOK)) {
                user.lastPostLook = System.currentTimeMillis();
            }
        }
    }
}