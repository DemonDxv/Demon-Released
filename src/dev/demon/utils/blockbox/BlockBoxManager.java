package dev.demon.utils.blockbox;

import dev.demon.tinyprotocol.api.ProtocolVersion;
import dev.demon.utils.blockbox.boxes.BlockBox1_7_R4;
import dev.demon.utils.blockbox.boxes.BlockBox1_8_R1;
import dev.demon.utils.blockbox.boxes.BlockBox1_8_R2;
import dev.demon.utils.blockbox.boxes.BlockBox1_8_R3;
import lombok.Getter;

@Getter
public class BlockBoxManager {
    private BlockBox blockBox;

    public BlockBoxManager() {
        //i want to obfuscate my shit!!!
        String version = ProtocolVersion.getGameVersion().getServerVersion().replaceAll("v", "");
        if (version.equalsIgnoreCase("1_7_R4")) {
            blockBox = new BlockBox1_7_R4();
        } else if (version.equalsIgnoreCase("1_8_R1")) {
            blockBox = new BlockBox1_8_R1();
        } else if (version.equalsIgnoreCase("1_8_R2")) {
            blockBox = new BlockBox1_8_R2();
        } else if (version.equalsIgnoreCase("1_8_R3")) {
            blockBox = new BlockBox1_8_R3();
        }
        //blockBox = (BlockBox) Reflection.getClass("cc.flycode.OverFlow.util.blockbox.boxes.BlockBox" + ProtocolVersion.getGameVersion().getServerVersion().replaceAll("v", "")).newInstance();
    }
}
