package dev.demon.tinyprotocol.packet.out;

import dev.demon.tinyprotocol.packet.types.WrappedEnumDifficulty;
import dev.demon.tinyprotocol.packet.types.WrappedEnumGameMode;
import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.api.NMSObject;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import dev.demon.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedClass;
import lombok.Getter;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

@Getter
public class WrappedOutRespawnPacket extends NMSObject {

    public WrappedOutRespawnPacket(Object object, Player player) {
        super(object, player);
    }

    private static String packet = Server.RESPAWN;

    private static FieldAccessor<Enum> difficultyAcessor;
    private static FieldAccessor<Enum> gamemodeAccessor;
    private static WrappedClass worldTypeClass;

    //Before 1.13
    private static FieldAccessor<Integer> dimensionAccesor;

    //1.13 and newer version of World ID
    private static FieldAccessor<Object> dimensionManagerAcceessor;
    private static WrappedClass dimensionManagerClass;

    private int dimension;
    private WrappedEnumGameMode gamemode;
    private WrappedEnumDifficulty difficulty;
    private WorldType worldType;

    @Override
    public void process(Player player, ProtocolVersion version) {
        if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            Object dimensionManager = fetch(dimensionManagerAcceessor);
            dimension = dimensionManagerClass.getFirstFieldByType(int.class).get(dimensionManager);
        } else {
            dimension = fetch(dimensionAccesor);
        }
        gamemode = WrappedEnumGameMode.fromObject(fetch(gamemodeAccessor));
        difficulty = WrappedEnumDifficulty.fromObject(fetch(difficultyAcessor));
        worldType = WorldType.getByName(worldTypeClass.getFirstFieldByType(String.class).get(getObject()));
    }

    static {
        if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            dimensionManagerAcceessor = fetchField(packet, Object.class, 0);
            dimensionManagerClass = Reflections.getNMSClass("DimensionManager");
        } else dimensionAccesor = fetchField(packet, int.class, 0);

        difficultyAcessor = fetchField(packet, Enum.class, 0);
        gamemodeAccessor = fetchField(packet, Enum.class, 1);
        worldTypeClass = Reflections.getNMSClass("WorldType");
    }
}
