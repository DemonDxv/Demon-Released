package dev.demon.checks.combat.aimassist;

import com.avaje.ebean.annotation.LdapId;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.utils.MathUtil;
import org.bukkit.Bukkit;

public class AimAssistA extends Check{
    public AimAssistA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                if (user.isUsingOptifine() || user.isLagging()) {
                    user.aimassistAVerbose = 0;
                    return;
                }
                if (user.newFrom.getPitch() != user.newTo.getPitch() && user.newFrom.getYaw() != user.newTo.getYaw()) {
                    double pitch = Math.abs(user.newTo.getPitch() - user.newFrom.getPitch());
                    if (pitch > 0 && pitch < 10 && Math.abs(user.player.getLocation().getPitch()) != 90 && MathUtil.gcd((long)(pitch * user.offset), (long)(user.lastPitchDifference * user.offset)) < 131072L) {
                        if (user.aimassistAVerbose <= 20) {
                            user.aimassistAVerbose += 2;
                        }
                    }user.aimassistAVerbose -= user.aimassistAVerbose > 0 ? 2 : 0;

                    if (++user.aimassistAVerbose >= 14) {
                            flag(user);
                        }
                         user.lastPitchDifference = pitch;
                    }
                }
            }
        }
    }
