package dev.demon.checks.misc.fakelag;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;

public class FakeLagB extends Check {
    public FakeLagB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (TimeUtils.elapsed(user.lastJoin) < 10000L || TimeUtils.elapsed(user.lastTeleport) < 2000L || TimeUtils.elapsed(user.lastUnknownTeleport) < 2000L) {
                return;
            }
            if (e.getType().equalsIgnoreCase(Packet.Client.KEEP_ALIVE)) {
                user.lastFakeKeep1 = System.currentTimeMillis();
            }else if (e.getType().equalsIgnoreCase(Packet.Client.TRANSACTION)) {
                user.lastFakeTrans = System.currentTimeMillis();
            }else if (isPacketMovement(e.getType())) {
                if (TimeUtils.elapsed(user.lastFakeKeep1) < 3000L && TimeUtils.elapsed(user.lastFakeTrans) > 5000L) {
                    flag(user);
                }
            }
        }
    }
}
