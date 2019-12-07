package dev.demon.checks.combat.velocity;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import dev.demon.utils.MathUtil;
import dev.demon.utils.TimeUtils;

public class VelocityA extends Check {
    public VelocityA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onMove(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (e.getType().equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
                WrappedOutVelocityPacket entityPacket = new WrappedOutVelocityPacket(e.getPacket(), e.getPlayer());
                if (entityPacket.getId() == entityPacket.getPlayer().getEntityId()) {
                    if (entityPacket.getY() > 0.2) {
                        user.lastVertical = entityPacket.getY();
                        user.tickSend = 0;
                        user.hasSent = true;
                    }
                }
            } else if (isPacketMovement(e.getType())) {
                if (!user.hasSent)
                    return;
                user.tickSend++;
                int maxTicks = (int) (user.transactionPing / 50) + 5;
                double y = user.newTo.getY() - user.newFrom.getY();
                if (TimeUtils.elapsed(user.lastLag) < 1000 || MathUtil.nextToWall(user.player) || user.blockAboveTicks > 0) {
                    user.velocityVerbose = 0;
                    return;
                }
                if (user.tickSend <= maxTicks && y <= user.lastVertical * 0.999) {
                    if (++user.velocityVerbose >= maxTicks) {
                        flag(user);
                        user.velocityVerbose = 0;
                    }
                } else {
                    user.velocityVerbose = 0;
                    user.hasSent = false;
                }
            }
        }
    }
}