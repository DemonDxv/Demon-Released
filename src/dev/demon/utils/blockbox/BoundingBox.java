package dev.demon.utils.blockbox;

import dev.demon.Demon;
import dev.demon.utils.BlockUtil;
import dev.demon.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox {

    public float minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Vector min, Vector max) {
        this.minX = (float) Math.min(min.getX(), max.getX());
        this.minY = (float) Math.min(min.getY(), max.getY());
        this.minZ = (float) Math.min(min.getZ(), max.getZ());
        this.maxX = (float) Math.max(min.getX(), max.getX());
        this.maxY = (float) Math.max(min.getY(), max.getY());
        this.maxZ = (float) Math.max(min.getZ(), max.getZ());
    }

    public BoundingBox add(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(Vector vector) {
        float x = (float) vector.getX(), y = (float) vector.getY(), z = (float) vector.getZ();

        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox grow(float x, float y, float z) {
        float newMinX = minX - x;
        float newMaxX = maxX + x;
        float newMinY = minY - y;
        float newMaxY = maxY + y;
        float newMinZ = minZ - z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox shrink(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX - x;
        float newMinY = minY + y;
        float newMaxY = maxY - y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ - z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public BoundingBox subtract(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }

    public boolean intersectsWithBox(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public List<BoundingBox> getCollidingBlockBoxes(Player player) {
        List<BoundingBox> toReturn = new ArrayList<>();
        int minX = MathUtil.floor(this.minX);
        int maxX = MathUtil.floor(this.maxX + 1);
        int minY = MathUtil.floor(this.minY);
        int maxY = MathUtil.floor(this.maxY + 1);
        int minZ = MathUtil.floor(this.minZ);
        int maxZ = MathUtil.floor(this.maxZ + 1);


        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                for (int y = minY - 1; y < maxY; y++) {
                    Location loc = new Location(player.getWorld(), x, y, z);
                    if (Demon.getInstance().getBlockBoxManager().getBlockBox().isChunkLoaded(loc)) {
                        Block block = BlockUtil.getBlock(loc);
                        if (!block.getType().equals(Material.AIR)) {
                            toReturn.addAll(Demon.getInstance().getBoxes().getBoundingBox(block));
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    public Vector getMinimum() {
        return new Vector(minX, minY, minZ);
    }

    public Vector getMaximum() {
        return new Vector(maxX, maxY, maxZ);
    }

    public List<Block> getCollidingBlocks(Player player) {
        List<Block> toReturn = new ArrayList<>();

        getCollidingBlockBoxes(player).forEach(bb -> bb.getMinimum().toLocation(player.getWorld()).getBlock());
        return toReturn;
    }

    public List<Block> getAllBlocks(Player player) {
        Location min = new Location(player.getWorld(), MathUtil.floor(minX), MathUtil.floor(minY), MathUtil.floor(minZ));
        Location max = new Location(player.getWorld(), MathUtil.floor(maxX), MathUtil.floor(maxY), MathUtil.floor(maxZ));
        List<Block> all = new ArrayList<>();
        for (float x = (float) min.getX(); x < max.getX(); x++) {
            for (float y = (float) min.getY(); y < max.getY(); y++) {
                for (float z = (float) min.getZ(); z < max.getZ(); z++) {
                    Block block = BlockUtil.getBlock(new Location(player.getWorld(), x, y, z));
                    if (!block.getType().equals(Material.AIR)) {
                        all.add(block);
                    }
                }
            }
        }
        return all;
    }

    public boolean inBlock(Player player) {
        return getCollidingBlocks(player).size() > 0;
    }

    public boolean intersectsWithBox(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public boolean collides(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }

    public boolean collides(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesHorizontally(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }

    public boolean collidesHorizontally(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesVertically(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public boolean collidesVertically(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionUtil.getFieldValue(ReflectionUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public Object toAxisAlignedBB() {
        return ReflectionUtil.newAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public String toString() {
        return "[" + minX + ", " + minY + ", " + minZ + ", " + maxX + ", " + maxY + ", " + maxZ + "]";
    }
}