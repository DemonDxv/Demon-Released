package dev.demon.tinyprotocol.packet;

import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PacketField<T> {
    private WrappedField field;
    private T value;
}
