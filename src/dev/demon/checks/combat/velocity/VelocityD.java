package dev.demon.checks.combat.velocity;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

public class VelocityD extends Check {
    public VelocityD(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (e.getType().equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
            WrappedOutVelocityPacket packet = new WrappedOutVelocityPacket(e.getPacket(), e.getPlayer());
            if (packet.getId() == e.getPlayer().getEntityId()) {
                User user = e.getUser();
                if (user != null) {
                    float diff = (float) (user.to.getY() - user.from.getY());
                    boolean ground = user.player.isOnGround();
                    if (user.liquidTicks > 0 || user.isLagging() || user.climableTicks > 0 || user.webTicks > 0 || user.player.getAllowFlight() || user.player.hasPotionEffect(PotionEffectType.JUMP) || user.blockAboveTicks > 0) {
                        return;
                    }
                    float playerY = (float) 0.36075;
                    if (user.slabTicks > 0 || user.stairTicks > 0 || user.wallTicks > 0) {
                        playerY = (float) 0.5;
                    }
                    if (user.lastClientGround2 && !ground) {
                        if (diff == 0.42f || diff == 0.3332f || diff == 0.248136f) {
                            return;
                        }
                        if (diff > playerY || diff < playerY && user.slabTicks < 1 && user.stairTicks < 1 && user.wallTicks < 1) {
                            flag(user);
                        }
                    }
                    user.lastClientGround2 = ground;
                }
            }
        }
    }
}
