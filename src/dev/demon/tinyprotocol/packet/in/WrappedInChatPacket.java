package dev.demon.tinyprotocol.packet.in;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

public class WrappedInChatPacket extends NMSObject {
    private static String packet = Client.CHAT;

    private static FieldAccessor<String> messageAccessor = fetchField(packet, String.class, 0);

    public WrappedInChatPacket(Object object, Player player) {
        super(object, player);
    }

    @Getter
    private String message;

    @Override
    public void process(Player player, ProtocolVersion version) {
        this.message = fetch(messageAccessor);
    }
}
