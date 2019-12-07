package dev.demon.checks.movement.prediction;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import org.bukkit.Bukkit;

public class PredictionE extends Check {
    public PredictionE(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                double y = Math.abs(user.to.getY() - user.from.getY());
                if (user.slabTicks > 0 || user.stairTicks > 0 || user.climableTicks > 0 || user.blockAboveTicks > 0 || user.fenceTicks > 0 || user.liquidTicks > 0 || user.isLagging()) {
                    return;
                }
                if (y > 0.0 && user.onGround && user.player.isOnGround()) {
                    if (user.predictionEVerbose++ > 2) {
                        flag(user);
                        user.predictionEVerbose = 0;
                    }
                }else user.predictionEVerbose -= user.predictionEVerbose > 0 ? 1 : 0;
            }
        }
    }
}