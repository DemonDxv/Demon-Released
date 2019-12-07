package dev.demon.checks.movement.scaffold;

import dev.demon.Demon;
import dev.demon.base.Check;
import dev.demon.base.CheckType;
import dev.demon.base.user.User;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class ScaffoldC extends Check {
    public ScaffoldC(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        super(checkName, type, checkType, dev, enabled);
    }
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();
        if (user != null) {
            if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
                WrappedInBlockPlacePacket entityPacket = new WrappedInBlockPlacePacket(e.getPacket(), e.getPlayer());
                if (entityPacket.getVecY() == 0.5 && entityPacket.getVecX() == 0.5 || entityPacket.getVecY() == 0 && entityPacket.getVecX() == 0 && entityPacket.getVecZ() == 0 && entityPacket.getFace().b() <= 2) {
                    flag(user);
                }
            }
        }
    }
}