package dev.demon.checks.misc.timer;

import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.utils.TimeUtils;
import org.bukkit.GameMode;

public class TimerA extends Check {
    public TimerA(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        if (isPacketMovement(e.getType())) {
            User user = e.getUser();
            if (user != null) {
                if (TimeUtils.secondsFromLong(user.lastJoin) < 5L || user.isLagging() || user.player.getGameMode().equals(GameMode.CREATIVE) || System.currentTimeMillis() - user.lastTeleport < 1000L || (System.currentTimeMillis() - user.lastFullBlockMove) > 1000L) {
                    user.lastTime2 = System.currentTimeMillis();
                    user.timerCVerbose = 0;
                    return;
                }
                long time = System.currentTimeMillis();
                long lastTime = user.lastTime2 != 0 ? user.lastTime2 : time - 50;
                user.lastTime2 = time;

                long rate = time - lastTime;

                user.balance2 = 50.0f / rate;
                if (user.balance2 > 50) {
                    return;
                }
                if (user.balance2 >= 1.05) {
                    if (user.timerCVerbose++ > 10) {
                        flag(user);
                        user.timerCVerbose = 0;
                    }
                }else user.timerCVerbose -= user.timerCVerbose > 0 ? 1 : 0;
            }
        }
    }
}