package dev.demon.tinyprotocol.packet.in;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.reflection.Reflection;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

public class WrappedIn13KeepAlive extends NMSObject {
    private static final String packet = Client.KEEP_ALIVE;
    @Getter
    private long ping;
    private FieldAccessor<Long> pingField = Reflection.getFieldSafe(packet, long.class, 0);

    public WrappedIn13KeepAlive(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        ping = fetch(pingField);
    }
}
