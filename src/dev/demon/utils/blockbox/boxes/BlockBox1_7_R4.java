package dev.demon.utils.blockbox.boxes;

import dev.demon.utils.BlockUtil;
import dev.demon.utils.MathUtil;
import dev.demon.utils.blockbox.BlockBox;
import dev.demon.utils.blockbox.BoundingBox;
import dev.demon.utils.blockbox.ReflectionUtil;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockBox1_7_R4 implements BlockBox {

    @Override
    public List<BoundingBox> getCollidingBoxes(World world, BoundingBox box) {
        List<AxisAlignedBB> aabbs = new ArrayList<>();
        List<BoundingBox> boxes = new ArrayList<>();

        int minX = MathUtil.floor(box.minX);
        int maxX = MathUtil.floor(box.maxX + 1);
        int minY = MathUtil.floor(box.minY);
        int maxY = MathUtil.floor(box.maxY + 1);
        int minZ = MathUtil.floor(box.minZ);
        int maxZ = MathUtil.floor(box.maxZ + 1);


        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    org.bukkit.block.Block block = BlockUtil.getBlock(new Location(world, x, y, z));
                    if (block != null && !block.getType().equals(Material.AIR)) {
                        if (BlockUtil.collisionBoundingBoxes.containsKey(block.getType())) {
                            aabbs.add((AxisAlignedBB) BlockUtil.collisionBoundingBoxes.get(block.getType()).add(block.getLocation().toVector()).toAxisAlignedBB());
                        } else {

                            final int aX = x, aY = y, aZ = z;

                            net.minecraft.server.v1_7_R4.World nmsWorld = ((CraftWorld) world).getHandle();
                            Block nmsBlock = ((CraftWorld) world).getHandle().getType(aX, aY, aZ);
                            nmsBlock.a(nmsWorld, aX, aY, aZ, (AxisAlignedBB) box.toAxisAlignedBB(), aabbs, null);

                            List<AxisAlignedBB> preBoxes = new ArrayList<>();

                            nmsBlock.updateShape(nmsWorld, aX, aY, aZ);
                            nmsBlock.a(nmsWorld, aX, aY, aZ, (AxisAlignedBB) box.toAxisAlignedBB(), preBoxes, null);

                            if (preBoxes.size() > 0) {
                                aabbs.addAll(preBoxes);
                            } else {
                                boxes.add(new BoundingBox((float) nmsBlock.x(), (float) nmsBlock.z(), (float) nmsBlock.B(), (float) nmsBlock.y(), (float) nmsBlock.A(), (float) nmsBlock.C()).add(block.getLocation().toVector()));
                            }
                        }
                        /*
                        else {
                            BoundingBox blockBox = new BoundingBox((float) nmsBlock.B(), (float) nmsBlock.D(), (float) nmsBlock.F(), (float) nmsBlock.C(), (float) nmsBlock.E(), (float) nmsBlock.G());
                        }*/

                    }
                }
            }
        }

        aabbs.stream().filter(box::collides).forEach(aabb -> boxes.add(ReflectionUtil.toBoundingBox(aabb)));
        return boxes;
    }

    @Override
    public List<BoundingBox> getSpecificBox(Location loc) {
        return Collections.synchronizedList(getCollidingBoxes(loc.getWorld(), new BoundingBox(loc.toVector(), loc.toVector())));
    }

    @Override
    public boolean isChunkLoaded(Location loc) {

        net.minecraft.server.v1_7_R4.World world = ((CraftWorld) loc.getWorld()).getHandle();

        return !world.isStatic && world.isLoaded(loc.getBlockX(), 0, loc.getBlockZ()) && world.getChunkAtWorldCoords(loc.getBlockX(), loc.getBlockZ()).d;
    }

    @Override
    public boolean isRiptiding(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean isUsingItem(Player player) {
        EntityHuman entity = ((org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity) player).getHandle();
        return entity.bF() != null && entity.bF().getItem().d(entity.bF()) != EnumAnimation.NONE;
    }

    @Override
    public float getMovementFactor(Player player) {
        return (float) ((CraftPlayer) player).getHandle().getAttributeInstance(GenericAttributes.d).getValue();
    }

    @Override
    public int getTrackerId(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = (EntityTrackerEntry) ((WorldServer) entityPlayer.getWorld()).tracker.trackedEntities.get(entityPlayer.getId());
        return entry.tracker.getId();
    }

    @Override
    public float getAiSpeed(Player player) {
        return ((CraftPlayer) player).getHandle().bl();
    }
}
