package dev.demon.tinyprotocol.packet.in;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedInKeepAlivePacket extends NMSObject {
    private static final String packet = Client.KEEP_ALIVE;

    private static FieldAccessor<Integer> fieldLegacy;
    private static FieldAccessor<Long> field;

    private long time;

    public WrappedInKeepAlivePacket(long time) {
        if (ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_12)) setPacket(packet, (int) time);
        else setPacket(packet, time);
    }

    public WrappedInKeepAlivePacket(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        if (ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_12)) {
            fieldLegacy = fetchField(packet, int.class, 0);
            time = fetch(fieldLegacy);
        }
        else {
            field = fetchField(packet, long.class, 0);
            time = fetch(field);
        }
    }
}