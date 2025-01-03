/*
 * Copyright (c) 2018 NGXDEV.COM. Licensed under MIT.
 */

package dev.demon.tinyprotocol.api;

import dev.demon.tinyprotocol.reflection.Reflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
@AllArgsConstructor
public enum ProtocolVersion {
    V1_7(4, "v1_7_R3"),
    V1_7_10(5, "v1_7_R4"),
    V1_8(45, "v1_8_R1"),
    V1_8_5(46, "v1_8_R2"),
    V1_8_9(47, "v1_8_R3"),
    V1_9(107, "v1_9_R1"),
    V1_9_1(108, null),
    V1_9_2(109, "v1_9_R2"),
    V1_9_4(110, "v1_9_R2"),
    V1_10(210, "v1_10_R1"),
    V1_10_2(210, "v1_10_R1"),
    V1_11(316, "v1_11_R1"),
    V1_12(335, "v1_12_R1"),
    V1_12_1(338, null),
    V1_12_2(340, "v1_12_R1"),
    V1_13(350, "v1_13_R1"),
    V1_13_1(351, "v1_13_R2"),
    V1_13_2(352, "v1_13_R2"),
    V1_14(477, "v1_14_R1"),
    UNKNOWN(-1, "UNKNOWN");

    @Getter
    private static ProtocolVersion gameVersion = fetchGameVersion();
    private int version;
    private String serverVersion;
    @Getter
    private static boolean paperSpigot = Bukkit.getServer().getBukkitVersion().toLowerCase().contains("paper");

    private static ProtocolVersion fetchGameVersion() {
        for (ProtocolVersion version : values()) {
            if (version.getServerVersion() != null && version.getServerVersion().equals(Reflection.VERSION))
                return version;
        }
        return UNKNOWN;
    }

    public static ProtocolVersion getVersion(int versionId) {
        for (ProtocolVersion version : values()) {
            if (version.getVersion() == versionId) return version;
        }
        return UNKNOWN;
    }



    public boolean isBelow(ProtocolVersion version) {
        return this.getVersion() < version.getVersion();
    }

    public boolean isAbove(ProtocolVersion version) {
        return this.getVersion() > version.getVersion();
    }

    public boolean isOrAbove(ProtocolVersion version) {
        return this.getVersion() >= version.getVersion();
    }
}
