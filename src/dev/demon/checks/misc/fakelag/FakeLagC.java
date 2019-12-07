package dev.demon.checks.misc.fakelag;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;

public class FakeLagC extends Check {
    public FakeLagC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
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
                user.lastFakeKeep2 = System.currentTimeMillis();
            }
            if (e.getType().equalsIgnoreCase(Packet.Client.TRANSACTION)) {
                user.lastFakeTrans1 = System.currentTimeMillis();
            }
             if (e.getType().equalsIgnoreCase(Packet.Client.POSITION) || e.getType().equalsIgnoreCase(Packet.Client.POSITION_LOOK) || e.getType().equalsIgnoreCase(Packet.Client.LOOK)) {
                 user.lastPos = System.currentTimeMillis();
             }
             if (isPacketMovement(e.getType())) {
            //    Bukkit.broadcastMessage(""+TimeUtils.elapsed(user.lastFakeKeep2) + " "+TimeUtils.elapsed(user.lastFakeTrans1) + " "+TimeUtils.elapsed(user.lastPos) );
                if (TimeUtils.elapsed(user.lastFakeKeep2) > 5000L && TimeUtils.elapsed(user.lastFakeTrans1) > 5000L && TimeUtils.elapsed(user.lastPos) < 250L) {
                    if (user.fakelagCVerbose.flag(10, 1000L)) {
                        flag(user);
                    }
                }
            }
        }
    }
}

// ;)