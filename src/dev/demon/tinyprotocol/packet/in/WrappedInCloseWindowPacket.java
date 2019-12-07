package dev.demon.tinyprotocol.packet.in;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class WrappedInCloseWindowPacket extends NMSObject {
    private static final String packet = Client.CLOSE_WINDOW;

    // Fields
    private static FieldAccessor<Integer> fieldId = fetchField(packet, int.class, 0);

    // Decoded data
    private int id;

    public WrappedInCloseWindowPacket(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        id = fetch(fieldId);
    }
}
