package dev.demon.checks.combat.autoclicker;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;

public class AutoClickerB extends Check {
    public AutoClickerB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            int ticks = user.ticksB;
            double avgSpeed = user.avgSpeedB, avgDevation = user.avgDevationB;
            if (e.getType().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
                if (TimeUtils.elapsed(user.lastLag) < 1000L || user.breakingBlock) {
                    user.autoClickerBVerbose = 0;
                    return;
                }
                int ticksree = ticks;
                ticks = 0;
                if (ticksree > 5) { ticks = 0; user.ticksB = ticks; return; }

                double speed = ticksree * 50;
                avgSpeed = ((avgSpeed * 14) + speed) / 15;
                double devation = Math.abs(speed - avgSpeed);
                avgDevation = ((avgDevation * 9) + devation) / 10;


                if (avgSpeed > 120) {
                    return;
                }
                if (avgDevation < 3.0) {
                    if (user.autoClickerBVerbose++ > 10) {
                        flag(user);
                        user.autoClickerBVerbose = 0;
                    }
                } else user.autoClickerBVerbose -= user.autoClickerBVerbose > 0 ? 1 : 0;
            }else if (isPacketMovement(e.getType())) {
                ticks++;
            }
            user.ticksB = ticks;
            user.avgSpeedB = avgSpeed;
            user.avgDevationB = avgDevation;
        }
    }
}
