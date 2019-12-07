package dev.demon.events.impl;

import lombok.Getter;
import lombok.Setter;
import dev.demon.base.user.User;
import dev.demon.events.BitDefenderEvent;
import dev.demon.events.Cancellable;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class PacketEvent extends BitDefenderEvent implements Cancellable {
    private Player player;
    @Setter
    private Object packet;
    @Setter
    private boolean cancelled;
    private String type;
    private Direction direction;
    private User user;
    private long timeStamp;
    private Location to, from;
    private CustomLocation newTo, newFrom;

    public PacketEvent(Player player, Object packet, String type, Direction direction, User user) {
        this.player = player;
        this.packet = packet;
        this.type = type;
        this.direction = direction;
        this.user = user;
        if (user.getOldTo() == null) to = player.getLocation();
        if (user.getOldFrom() == null) from = player.getLocation();
        if (user.newTo == null) newTo = new CustomLocation(0, 0, 0, 0, 0);
        if (user.newFrom == null) newFrom = new CustomLocation(0, 0, 0, 0, 0);
        to = user.getOldTo();
        from = user.getOldFrom();
        newTo = user.getNewTo();
        newFrom = user.getNewFrom();

        timeStamp = System.currentTimeMillis();
    }

    public enum Direction {
        CLIENT,
        SERVER
    }
}
