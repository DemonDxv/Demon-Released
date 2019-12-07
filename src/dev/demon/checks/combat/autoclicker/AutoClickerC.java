package dev.demon.checks.combat.autoclicker;

import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AutoClickerC extends Check {
    public AutoClickerC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }

    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (e.getType().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
                if (user.isBreakingBlock()) {
                    return;
                }
                if (user.ticksD < 10) {
                    user.recentCounts.add(user.ticksD);
                    if (user.recentCounts.size() == 50) {
                        double average = 0.0;
                        for (int i : user.recentCounts) {
                            average += i;
                        }
                        average /= user.recentCounts.size();
                           double stdDev = 0.0;
                           for (int j : user.recentCounts) {
                                stdDev += Math.pow(j - average, 2.0);
                           }
                             stdDev /= user.recentCounts.size();
                             stdDev = Math.sqrt(stdDev);
                        if (stdDev < 0.45) {
                            flag(user);
                        }
                        user.recentCounts.clear();
                    }
                }

                user.ticksD = 0;
            }else if (isPacketMovement(e.getType())) {
                user.ticksD++;
            }
        }
    }
}