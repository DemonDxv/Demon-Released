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

public class VelocityC extends Check {
    public VelocityC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
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
                        user.lastHorizontal3 = Math.sqrt(Math.pow(entityPacket.getX(), 2.0) + Math.pow(entityPacket.getZ(), 2.0));
                        user.tickSendH3 = 0;
                        user.hasSentH3 = true;
                    }
                }
            } else if (isPacketMovement(e.getType())) {
                if (!user.hasSentH3)
                    return;
                user.tickSendH3++;
                int maxTicks = (int) (user.transactionPing / 50) + 5;
                double dy = user.newTo.getY() - user.newFrom.getY();
                double dx = user.newTo.getX() - user.newFrom.getX();
                double dz = user.newTo.getZ() - user.newFrom.getZ();

                double horizontalDistance = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));
                double ratio = horizontalDistance / user.lastHorizontal3;
                //  Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityBVerbose);

                if (TimeUtils.elapsed(user.lastLag) < 1000 || MathUtil.nextToWall(user.player)) {
                    user.velocityDVerbose = 0;
                    return;
                }
             //   Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityDVerbose);
                if (user.tickSendH3 <= maxTicks && ratio < 0.06 && dy < 0.42f) {
                    if (++user.velocityDVerbose >= maxTicks) {
                        //           Bukkit.broadcastMessage(""+ratio + " < " +0.999F + " "+user.velocityBVerbose);
                        flag(user);
                        user.velocityDVerbose = 0;
                    }
                } else {
                    user.velocityDVerbose = 0;
                    user.hasSentH3 = false;
                }
            }
        }
    }
}