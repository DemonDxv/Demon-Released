/*
 * Copyright (c) 2018 NGXDEV.COM. Licensed under MIT.
 */

package dev.demon.tinyprotocol.packet.types;

import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.reflection.Reflection;
import dev.demon.utils.blockbox.ReflectionUtil;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class WrappedGameProfile extends NMSObject {
    private static final String type = Type.GAMEPROFILE;

    // Fields
    private static FieldAccessor<UUID> fieldId = fetchField(type, UUID.class, 0);
    private static FieldAccessor<String> fieldName = fetchField(type, String.class, 0);
    private static FieldAccessor<?> fieldPropertyMap = fetchField(type, Reflection.getClass(Type.PROPERTYMAP), 0);

    // Decoded data
    public UUID id;
    public String name;
    public Object propertyMap;

    public WrappedGameProfile(Object type) {
        super(type);
    }

    public WrappedGameProfile(Player player) {
        Object entityPlayer = ReflectionUtil.getEntityPlayer(player);
        FieldAccessor<Object> gameProfileAcessor = fetchField("EntityHuman", Reflection.NMS_PREFIX + type, 0);
        setObject(fetch(gameProfileAcessor));
        id = fieldId.get(getObject());
        name = fieldName.get(getObject());
        propertyMap = fieldPropertyMap.get(getObject());
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        id = fieldId.get(getObject());
        name = fieldName.get(getObject());
        propertyMap = fieldPropertyMap.get(getObject());
    }
}
