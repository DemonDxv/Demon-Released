/*
 * Copyright (c) 2018 NGXDEV.COM. Licensed under MIT.
 */

package dev.demon.tinyprotocol.packet.in;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedInVelocityPacket extends Packet {
    private static final String packet = Client.FLYING;

    // Fields
    private static FieldAccessor<Integer> fieldX = fetchField(packet, int.class, 0);
    private static FieldAccessor<Integer> fieldY = fetchField(packet, int.class, 1);
    private static FieldAccessor<Integer> fieldZ = fetchField(packet, int.class, 2);

    // Decoded data
    private double x, y, z;

    public WrappedInVelocityPacket(Object packet) {
        super(packet);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        x = fieldX.get(getPacket());
        y = fieldY.get(getPacket());
        z = fieldZ.get(getPacket());
    }
}
