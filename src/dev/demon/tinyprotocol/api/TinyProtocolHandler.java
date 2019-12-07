package dev.demon.tinyprotocol.api;

import dev.demon.Demon;
import dev.demon.base.user.User;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.packets.ChannelInjector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class TinyProtocolHandler {
    @Getter
    private static ChannelInjector instance;

    public boolean paused = false;

    private static Map<Player, ProtocolVersion> versionCache = new WeakHashMap<>();

    public TinyProtocolHandler() {
        // 1.8+ and 1.7 NMS have different class paths for their libraries used. This is why we have to separate the two.
        // These feed the packets asynchronously, before Minecraft processes it, into our own methods to process and be used as an API.

        instance = new ChannelInjector();

        Bukkit.getPluginManager().registerEvents(instance, Demon.getInstance());
    }

    // Purely for making the code cleaner
    public static void sendPacket(Player player, Object packet) {
        instance.getChannel().sendPacket(player, packet);
    }

    public static ProtocolVersion getProtocolVersion(Player player) {
        return instance.getChannel().getProtocolVersion(player);
    }

    private boolean didPosition = false;

    public Object onPacketOutAsync(Player sender, Object packet) {
        if(!paused) {
            String name = packet.getClass().getName();
            int index = name.lastIndexOf(".");
            String packetName = name.substring(index + 1);
            User user = Demon.getInstance().getUserManager().getUser(sender.getUniqueId());
            PacketEvent event = new PacketEvent(sender, packet, packetName, PacketEvent.Direction.SERVER, user);

            if (user != null && user.getTo() != null && user.getFrom() != null) Demon.getInstance().getEventManager().callEvent(event);
            return !event.isCancelled() ? event.getPacket() : null;
        } else return packet;
    }

    public Object onPacketInAsync(Player sender, Object packet) {
        if (!paused) {
            String name = packet.getClass().getName();
            int index = name.lastIndexOf(".");
            String packetName = name.substring(index + 1).replace("PacketPlayInUseItem", "PacketPlayInBlockPlace")
                    .replace(Packet.Client.LEGACY_LOOK, Packet.Client.LOOK)
                    .replace(Packet.Client.LEGACY_POSITION, Packet.Client.POSITION)
                    .replace(Packet.Client.LEGACY_POSITION_LOOK, Packet.Client.POSITION_LOOK);


            User user = Demon.getInstance().getUserManager().getUser(sender.getUniqueId());
            PacketEvent event = new PacketEvent(sender, packet, packetName, PacketEvent.Direction.CLIENT, user);

           if (user != null && user.getTo() != null && user.getFrom() != null) Demon.getInstance().getEventManager().callEvent(event);
            return !event.isCancelled() ? event.getPacket() : null;
        }
        return packet;
    }
}

