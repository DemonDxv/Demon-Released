package dev.demon.checks.combat.killaura;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.TimeUtils;

public class KillauraC extends Check {
    public KillauraC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            double avgPitchSpeed = user.avgPitchSpeed, avgPitchDevation = user.avgPitchDevation;
            if (isPacketMovement(e.getType())) {
                if (TimeUtils.elapsed(user.lastAttackPacket) < 100L) {

                    double pitch = user.newTo.getPitch() - user.newFrom.getPitch();
                    double yaw = user.newTo.getYaw() - user.newFrom.getYaw();
                    if (yaw > 3 && yaw < 90) {
                        avgPitchSpeed = ((avgPitchSpeed * 14) + pitch) / 15;
                        double devation = Math.abs(pitch - avgPitchSpeed);
                        avgPitchDevation = ((avgPitchDevation * 9) + devation) / 10;
                        if (avgPitchDevation < 0.1) {
                            if (user.killauraHVerbose.flag(10, 700L)) {
                                flag(user);
                            }
                        }
                    }
                }
                user.avgPitchSpeed = avgPitchSpeed;
                user.avgPitchDevation = avgPitchDevation;
            }
        }
    }
}
