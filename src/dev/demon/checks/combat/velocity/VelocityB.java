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

public class VelocityB extends Check {
    public VelocityB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
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
                        user.lastHorizontal = Math.sqrt(Math.pow(entityPacket.getX(), 2.0) + Math.pow(entityPacket.getZ(), 2.0));
                        user.tickSendH = 0;
                        user.hasSentH = true;
                    }
                }
            } else if (isPacketMovement(e.getType())) {
                if (!user.hasSentH)
                    return;
                user.tickSendH++;
                int maxTicks = (int) (user.transactionPing / 50) + 5;
                double dy = user.newTo.getY() - user.newFrom.getY();
                double dx = user.newTo.getX() - user.newFrom.getX();
                double dz = user.newTo.getZ() - user.newFrom.getZ();

                double horizontalDistance = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));
                double ratio = horizontalDistance / user.lastHorizontal;
              //  Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityBVerbose);

                if (TimeUtils.elapsed(user.lastLag) < 1000 || MathUtil.nextToWall(user.player)) {
                    user.velocityBVerbose = 0;
                    return;
                }
            //    Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityBVerbose);
                if (user.tickSendH <= maxTicks && ratio < 0.6 && ratio >= 0.1 && dy < 0.42f) {
                    if (++user.velocityBVerbose >= maxTicks) {
             //           Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityBVerbose);
                        flag(user);
                        user.velocityBVerbose = 0;
                    }
                } else {
                    user.velocityBVerbose = 0;
                    user.hasSentH = false;
                }
            }
        }
    }
}