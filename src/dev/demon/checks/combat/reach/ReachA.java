package dev.demon.checks.combat.reach;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.MathUtil;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.checks.combat.reach
 */
public class ReachA extends Check {
    public ReachA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (e.getTo() == null || e.getFrom() == null) return;
        if ((System.currentTimeMillis() - user.lastJoin) < 1000L) return;
        if (e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
            WrappedInUseEntityPacket useEntityPacket = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
            if (useEntityPacket.getEntity() instanceof Player && useEntityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                if (MathUtil.nextToWall((Player) user.getLastEntityAttacked()) || user.getPlayer().getGameMode().equals(GameMode.CREATIVE) || user.isLagging()) {
                    user.reachAVerbose = 0;
                    return;
                }
                Vector origin = user.getPlayer().getLocation().clone().add(0, 1.53, 0).toVector();
                // Location origin = user.player.getLocation();
                List<Vector> pastLocation = user.reachAPastLocations.getEstimatedLocation(user.transactionPing, 150 + Math.abs(user.lastTransactionPing - user.transactionPing)).stream().map(CustomLocation::toVector).collect(Collectors.toList());
                float distance = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(origin.clone().setY(0)) - 0.3).min().orElse(0);
                // float distance = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(origin.toVector().clone().setY(0)) - 0.3f).min().orElse(0);
                double maxReach = 3.1;
                if (distance >= maxReach) {
                    if (user.reachAVerbose++ > 3) {
                        flag(user);
                        user.reachAVerbose = 0;
                    }
                } else user.reachAVerbose -= user.reachAVerbose > 0 ? 1 : 0;
            }
        }
    }
}
