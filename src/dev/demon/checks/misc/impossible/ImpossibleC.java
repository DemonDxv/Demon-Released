package dev.demon.checks.misc.impossible;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.TimeUtils;

public class ImpossibleC extends Check {
    public ImpossibleC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE) && e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
                WrappedInUseEntityPacket entityPacket = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
                if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                    if (user.player.getItemInHand().getType().toString().contains("SWORD")) {
                        flag(user);
                    }
                }
            }
        }
    }
}
