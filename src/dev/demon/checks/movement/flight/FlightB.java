package dev.demon.checks.movement.flight;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffectType;

public class FlightB extends Check {
    public FlightB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (isPacketMovement(e.getType())) {
                double ydiff = user.to.getY() - user.from.getY();
                if (user.liquidTicks > 0 || TimeUtils.elapsed(user.lastTeleport) < 2000L || user.isLagging() || TimeUtils.elapsed(user.lastCancelPlace) < 1200L || user.player.getAllowFlight() || user.player.getGameMode().equals(GameMode.CREATIVE)|| user.slabTicks > 0 || user.stairTicks > 0 || String.valueOf(ydiff).contains("-") || TimeUtils.elapsed(user.lastEntityVelocity) < 2000L || user.webTicks > 0 || user.player.isInsideVehicle() || user.mountedTicks > 0 || user.blockAboveTicks > 0 || user.fenceTicks > 0 || user.wallTicks > 0 || user.climableTicks > 0 || user.player.hasPotionEffect(PotionEffectType.JUMP)) {
                    return;
                }
                boolean ground = user.onGround;
                user.wasOnGround = ground;
                boolean WasOnground = user.wasOnGround;
                double lastYDiff = user.lastYDiff;
                double prediction = (lastYDiff - 0.08D) * 0.9800000190734863;
                if (Math.abs(prediction) <= 0.005) {
                    prediction = 0;
                }
                if (ydiff < -0.07839 && ydiff > -0.07841 && prediction == 0) {
                    prediction = ydiff;
                }
                double Delta = Math.abs(prediction - ydiff);
                if (Delta > 1.0E-12 && (!user.onGround && !WasOnground) && user.airTicks > 10 && user.jumpPotionTicks < 1) {
                    if (user.gravityAVerbose.flag(1, 1000L)) {
                        flag(user);
                    }
                }
            }
        }
    }
}
