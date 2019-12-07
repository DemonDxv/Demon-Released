package dev.demon.checks.misc.impossible;

import com.mysql.jdbc.TimeUtil;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import dev.demon.tinyprotocol.packet.in.WrappedInFlyingPacket;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.TimeUtils;
import org.bukkit.Bukkit;

public class ImpossibleB extends Check {
    public ImpossibleB(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
                if (user.player.getItemInHand().getType().toString().contains("SWORD")) {
                    user.lastBlockPlacePacket = System.currentTimeMillis();
                }
            }else if (e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
                WrappedInUseEntityPacket entityPacket = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
                if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                    if (TimeUtils.elapsed(user.lastBlockPlacePacket) < 70) {
                        if (user.impossibleBVerbose++ > 1) {
                            flag(user);
                            user.impossibleBVerbose = 0;
                        }
                    }else user.impossibleBVerbose -= user.impossibleBVerbose > 0 ? 1 : 0;
                }
            }
        }
    }
}
