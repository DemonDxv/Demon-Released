package dev.demon.utils.reflection;

import dev.demon.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedClass;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedMethod;
import dev.demon.tinyprotocol.reflection.MethodInvoker;
import dev.demon.tinyprotocol.reflection.Reflection;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftReflection {
    public static Class<?> craftHumanEntity = Reflection.getCraftBukkitClass("entity.CraftHumanEntity");
    public static Class<?> craftEntity = Reflection.getCraftBukkitClass("entity.CraftEntity");
    public static Class<?> craftItemStack = Reflection.getCraftBukkitClass("inventory.CraftItemStack");
    public static Class<?> craftBlock = Reflection.getCraftBukkitClass("block.CraftBlock");
    public static Class<?> craftWorld = Reflection.getCraftBukkitClass("CraftWorld");
    public static WrappedClass craftplayer = Reflections.getCBClass("entity.CraftPlayer");

    //Vanilla Instances
    public static MethodInvoker itemStackInstance = Reflection.getMethod(craftItemStack,
            "asNMSCopy", ItemStack.class);
    public static MethodInvoker humanEntityInstance = Reflection.getMethod(craftHumanEntity, "getHandle");
    public static MethodInvoker entityInstance = Reflection.getMethod(craftEntity, "getHandle");
    public static MethodInvoker blockInstance = Reflection.getMethod(craftBlock, "getNMSBlock");
    public static MethodInvoker worldInstance = Reflection.getMethod(craftWorld, "getHandle");
    public static WrappedMethod bukkitEntity = MinecraftReflection.entity.getMethod("getBukkitEntity");
    public static WrappedMethod entityPlayerHandle = craftplayer
            .getMethodByType(MinecraftReflection.entityPlayer.getParent(), 0);

    public static Object getVanillaItemStack(ItemStack stack) {
        return itemStackInstance.invoke(null, stack);
    }

    public static Object getEntityHuman(HumanEntity entity) {
        return humanEntityInstance.invoke(entity);
    }

    public static Object getEntity(Entity entity) {
        return entityInstance.invoke(entity);
    }

    public static Object getVanillaBlock(Block block) {
        return blockInstance.invoke(block);
    }

    public static Object getVanillaWorld(World world) {
        return worldInstance.invoke(world);
    }

    public static Entity getBukkitEntity(Object vanillaEntity) {
        return bukkitEntity.invoke(vanillaEntity);
    }

    public static <T> T getVanillaPlayer(Player player) {
        return entityPlayerHandle.invoke(player);
    }
}