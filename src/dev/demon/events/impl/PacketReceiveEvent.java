package dev.demon.events.impl;

import dev.demon.events.Cancellable;
import dev.demon.events.BitDefenderEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class PacketReceiveEvent extends BitDefenderEvent implements Cancellable {
    private Player player;
    @Setter
    private Object packet;
    @Setter
    private boolean cancelled;
    private String type;
    private long timeStamp;

    public PacketReceiveEvent(Player player, Object packet, String type) {
        this.player = player;
        this.packet = packet;
        this.type = type;

        timeStamp = System.currentTimeMillis();
    }
}
