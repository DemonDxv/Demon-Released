package dev.demon.checks.combat.autoclicker;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;

public class AutoClickerA extends Check {
    public AutoClickerA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            int ticks = user.ticksA;
            double avgSpeed = user.avgSpeedA;
            if (e.getType().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
                if (TimeUtils.elapsed(user.lastLag) < 1000L || user.breakingBlock) {
                    user.autoClickerAVerbose = 0;
                    return;
                }
                int ticksree = ticks;
                ticks = 0;
                if (ticksree > 5) {
                    ticks = 0;
                    user.ticksA = ticks;
                    return;
                }

                double speed = ticksree * 50;
                avgSpeed = ((avgSpeed * 14) + speed) / 15;

            //    Bukkit.broadcastMessage(""+avgSpeed);
                if (avgSpeed < 55) {
                    if (user.autoClickerAVerbose++ > 20) {
                        flag(user);
                    }
                } else user.autoClickerAVerbose -= user.autoClickerAVerbose > 0 ? 1 : 0;
            }else if (isPacketMovement(e.getType())) {
                ticks++;
            }
            user.ticksA = ticks;
            user.avgSpeedA = avgSpeed;
        }
    }
}
