package dev.demon.utils.reflection;

import dev.demon.tinyprotocol.api.ProtocolVersion;
import dev.demon.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedClass;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedConstructor;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedField;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedMethod;
import dev.demon.tinyprotocol.packet.types.BaseBlockPosition;
import dev.demon.tinyprotocol.packet.types.WrappedEnumAnimation;
import dev.demon.utils.blockbox.BoundingBox;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinecraftReflection {
    public static WrappedClass entity = Reflections.getNMSClass("Entity");
    public static WrappedClass axisAlignedBB = Reflections.getNMSClass("AxisAlignedBB");
    public static WrappedClass entityHuman = Reflections.getNMSClass("EntityHuman");
    public static WrappedClass block = Reflections.getNMSClass("Block");
    public static WrappedClass iBlockData = Reflections.getNMSClass("IBlockData");
    public static WrappedClass world = Reflections.getNMSClass("World");
    public static WrappedClass worldServer = Reflections.getNMSClass("WorldServer");
    public static WrappedClass playerInventory = Reflections.getNMSClass("PlayerInventory");
    public static WrappedClass itemStack = Reflections.getNMSClass("ItemStack");
    public static WrappedClass enumAnimation = Reflections.getNMSClass("EnumAnimation");
    public static WrappedClass networkManager = Reflections.getNMSClass("NetworkManager");
    public static WrappedClass playerConnection = Reflections.getNMSClass("PlayerConnection");
    public static WrappedClass entityPlayer = Reflections.getNMSClass("EntityPlayer");
    public static WrappedClass enumProtocol;
    public static WrappedClass channelClass;
    public static WrappedClass attributeKeyClass;
    public static WrappedClass attribute;

    //BoundingBoxes
    public static WrappedMethod getCubes;
    public static WrappedField aBB = axisAlignedBB.getFieldByName("a");
    public static WrappedField bBB = axisAlignedBB.getFieldByName("b");
    public static WrappedField cBB = axisAlignedBB.getFieldByName("c");
    public static WrappedField dBB = axisAlignedBB.getFieldByName("d");
    public static WrappedField eBB = axisAlignedBB.getFieldByName("e");
    public static WrappedField fBB = axisAlignedBB.getFieldByName("f");
    public static WrappedConstructor aabbConstructor;
    public static WrappedMethod idioticOldStaticConstructorAABB;
    public static WrappedField entityBoundingBox = entity.getFirstFieldByType(axisAlignedBB.getParent());

    //ItemStack methods and fields
    public static WrappedMethod enumAnimationStack;

    //1.13+ only
    public static WrappedClass voxelShape;
    public static WrappedClass worldReader;
    public static WrappedMethod getCubesFromVoxelShape;

    //Blocks
    public static WrappedMethod addCBoxes;
    public static WrappedClass blockPos;
    public static WrappedConstructor blockPosConstructor;
    public static WrappedMethod getBlockData;

    //Player Connection
    public static WrappedField networkManagerPlayerField = playerConnection
            .getFieldByType(networkManager.getParent(), 0);
    public static WrappedField playerConnectionField = entityPlayer
            .getFieldByType(playerConnection.getParent(), 0);
    public static WrappedField networkChannelField;

    //1.8 Protocol
    public static WrappedField attributeProtocol;
    public static WrappedMethod attrChannelMethod;
    public static WrappedMethod enumProtocolIdMethod;

    public static WrappedEnumAnimation getArmAnimation(HumanEntity entity) {
        if(entity.getItemInHand() != null) {
            return getItemAnimation(entity.getItemInHand());
        }
        return WrappedEnumAnimation.NONE;
    }

    public static WrappedEnumAnimation getItemAnimation(ItemStack stack) {
        Object itemStack = CraftReflection.getVanillaItemStack(stack);

        return WrappedEnumAnimation.fromNMS(enumAnimationStack.invoke(itemStack));
    }

    public static List<BoundingBox> getBlockBox(@Nullable Entity entity, Block block) {
        Object vanillaBlock = CraftReflection.getVanillaBlock(block);
        Object world = CraftReflection.getVanillaWorld(block.getWorld());

        //TODO Use increasedHeight if it doesnt get fence or wall boxes properly.
        //boolean increasedHeight = BlockUtils.isFence(block) || BlockUtils.isWall(block);
        //We do this so we can get the block inside
        BoundingBox box = new BoundingBox(
                block.getLocation().toVector(),
                block.getLocation().clone()
                        .add(1,1,1)
                        .toVector());

        List<Object> aabbs = new ArrayList<>();

        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            addCBoxes.invoke(vanillaBlock, world,
                    block.getX(), block.getY(), block.getZ(),
                    box.toAxisAlignedBB(), aabbs,
                    entity != null ? CraftReflection.getEntity(entity) : null); //Entity is always null for these
        } else if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_13)) {
            BaseBlockPosition blockPos = new BaseBlockPosition(block.getX(), block.getY(), block.getZ());
            Object blockData = getBlockData.invoke(vanillaBlock);

            addCBoxes.invoke(vanillaBlock, world, blockPos.getAsBlockPosition(), blockData,
                    box.toAxisAlignedBB(), aabbs, entity != null ? CraftReflection.getEntity(entity) : null);
            //Entity is always null for these
        }

        return aabbs.stream().map(MinecraftReflection::fromAABB).collect(Collectors.toList());
    }

    //1.7 field is boundingBox
    //1.8+ method is getBoundingBox.
    public static BoundingBox getEntityBoundingBox(Entity entity) {
        Object vanillaEntity = CraftReflection.getEntity(entity);

        return fromAABB(entityBoundingBox.get(vanillaEntity));
    }

    public static List<BoundingBox> getCollidingBoxes(@Nullable Entity entity, World world, BoundingBox box) {
        Object vWorld = CraftReflection.getVanillaWorld(world);
        List<BoundingBox> boxes = new ArrayList<>();
        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_13)) {
            List<Object> aabbs = ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_12)
                    ? getCubes.invoke(vWorld, entity != null
                    ? CraftReflection.getEntity(entity)
                    : null, box.toAxisAlignedBB())
                    : getCubes.invoke(vWorld, box.toAxisAlignedBB(), false, entity != null
                    ? CraftReflection.getEntity(entity) : null);

            boxes = aabbs.stream().map(MinecraftReflection::fromAABB).collect(Collectors.toList());
        } else {
            Object voxelShape = getCubes.invoke(vWorld, null, box.toAxisAlignedBB(), 0D, 0D, 0D);

            if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_13_2)) {
                List<Object> aabbs = getCubesFromVoxelShape.invoke(voxelShape);

                boxes = aabbs.stream().map(MinecraftReflection::fromAABB).collect(Collectors.toList());
            } else {
                List<Object> aabbs = new ArrayList<>();

                ((List<Object>) voxelShape).stream()
                        .map(ob -> {
                            List<Object> aabbList = getCubesFromVoxelShape.invoke(ob);
                            return aabbList;
                        }).forEach(aabbs::addAll);

               boxes = aabbs.stream().map(MinecraftReflection::fromAABB).collect(Collectors.toList());
            }
        }
        return boxes;
    }

    //a, b, c is minX, minY, minZ
    //d, e, f is maxX, maxY, maxZ
    public static BoundingBox fromAABB(Object aabb) {
        double a, b, c, d, e, f;

        a = aBB.get(aabb);
        b = bBB.get(aabb);
        c = cBB.get(aabb);
        d = dBB.get(aabb);
        e = eBB.get(aabb);
        f = fBB.get(aabb);

        return new BoundingBox((float) a,(float) b,(float) c,(float) d,(float) e,(float) f);
    }

    public static <T> T toAABB(BoundingBox box) {
        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            return idioticOldStaticConstructorAABB
                    .invoke(null,
                            (double)box.minX, (double)box.minY, (double)box.minZ,
                            (double)box.maxX, (double)box.maxY, (double)box.maxZ);
        } else return aabbConstructor
                .newInstance((double)box.minX, (double)box.minY, (double)box.minZ,
                (double)box.maxX, (double)box.maxY, (double)box.maxZ);
    }

    public static ProtocolVersion getVersion(Player player) {
        Object vanillaPlayer = CraftReflection.getVanillaPlayer(player);

        Object playerConn = getPlayerConnection(player);
        Object network = networkManagerPlayerField.get(playerConn);

        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            int version = networkManager.getMethod("getVersion").invoke(network);

            return ProtocolVersion.getVersion(version);
        } else {
            Object channel = networkChannelField.get(network);
            Object attribute = attributeProtocol.get(network);
            Object attr = attrChannelMethod.invoke(channel, attribute);
            Object enumProtocol = MinecraftReflection.attribute.getMethod("get").invoke(attr);
            int protocolId = enumProtocolIdMethod.invoke(enumProtocol);

            return ProtocolVersion.getVersion(protocolId);
        }
    }

    public static <T> T getPlayerConnection(Player player) {
        Object vanillaPlayer = CraftReflection.getVanillaPlayer(player);

        return playerConnectionField.get(vanillaPlayer);
    }

    static {
        if(ProtocolVersion.getGameVersion().isAbove(ProtocolVersion.V1_7_10)) {
            blockPos = Reflections.getNMSClass("BlockPosition");
            blockPosConstructor = blockPos.getConstructor(int.class, int.class, int.class);
            getBlockData = block.getMethod("getBlockData");
            aabbConstructor = axisAlignedBB
                    .getConstructor(double.class, double.class, double.class, double.class, double.class, double.class);
            channelClass = Reflections.getClass("io.netty.channel.Channel");
            attributeKeyClass = Reflections.getClass("io.netty.util.AttributeKey");
            attributeProtocol = networkManager.getFirstFieldByType(attributeKeyClass.getParent());
            enumProtocol = Reflections.getNMSClass("EnumProtocol");
            enumProtocolIdMethod = enumProtocol.getMethodByType(int.class, 0);
            attribute = Reflections.getClass("io.netty.util.Attribute");
        } else {
            channelClass = Reflections.getClass("net.minecraft.util.io.netty.channel.Channel");
            attributeKeyClass = Reflections.getClass("net.minecraft.util.io.netty.util.AttributeKey");
            idioticOldStaticConstructorAABB = axisAlignedBB.getMethodByType(axisAlignedBB.getParent(), 0);
            attribute = Reflections.getClass("net.minecraft.util.io.netty.util.Attribute");
        }

        attrChannelMethod = channelClass.getMethod("attr", attributeKeyClass.getParent());
        networkChannelField = networkManager.getFieldByType(channelClass.getParent(), 0);
        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_12)) {
            getCubes = world.getMethod("getCubes", entity.getParent(), axisAlignedBB.getParent());

            if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
                //1.7.10 does not have the BlockPosition object yet.
                addCBoxes = block.getMethod("a", world.getParent(), int.class, int.class, int.class,
                        axisAlignedBB.getParent(), List.class, entity.getParent());
            } else {
                addCBoxes = block.getMethod("a", world.getParent(), blockPos.getParent(), iBlockData.getParent(),
                        axisAlignedBB.getParent(), List.class, entity.getParent());
            }
        } else if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_13)) {
            getCubes = world.getMethod("a", entity.getParent(), axisAlignedBB.getParent(),
                    boolean.class, List.class);
            addCBoxes = block.getMethod("a", world.getParent(), blockPos.getParent(), iBlockData.getParent(),
                    axisAlignedBB.getParent(), List.class, entity.getParent());
        } else {
            worldReader = Reflections.getNMSClass("IWorldReader");
            //1.13 and 1.13.1 returns just VoxelShape while 1.13.2+ returns a Stream<VoxelShape>
            getCubes = worldReader.getMethod("a", entity.getParent(), axisAlignedBB.getParent(),
                    double.class, double.class, double.class);
            voxelShape = Reflections.getNMSClass("VoxelShape");
            getCubesFromVoxelShape = voxelShape.getMethodByType(List.class, 0);
        }
        try {
            enumAnimationStack = itemStack.getMethodByType(enumAnimation.getParent(), 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
