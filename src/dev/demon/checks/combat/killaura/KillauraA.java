package dev.demon.checks.combat.killaura;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.TimeUtils;

public class KillauraA extends Check {
    public KillauraA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (user.isLagging()) {
                user.noslowAVerbose.setVerbose(0);
                return;
            }
            if (e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
                WrappedInUseEntityPacket entityPacket = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
                if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                    if (TimeUtils.elapsed(user.lastPostDig) < 30L) {
                        if (user.noslowAVerbose.flag(2, 1000L)) {
                            flag(user);
                        }
                    }
                }
            } else if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {
                user.lastPostDig = System.currentTimeMillis();
            }
        }
    }
}