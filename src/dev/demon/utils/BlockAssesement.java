package dev.demon.utils;

import dev.demon.utils.blockbox.BoundingBox;
import lombok.Getter;
import lombok.Setter;
import dev.demon.Demon;
import dev.demon.base.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Created on 21/10/2019 Package me.jumba.util.block
 */
@Getter
@Setter
public class BlockAssesement {
    private User user;
    private BoundingBox boundingBox;
    private boolean onGround, onIce, blockAbove, stair, slab, pistion, climbale, groundSlime, web, chests, halfblock, liquid, wall, carpet, stairSlabs, slime, fence;

    public BlockAssesement(BoundingBox box, User user) {
        this.user = user;
        this.boundingBox = box;
    }

    public void update(BoundingBox bb, World world) {
        Location location = bb.getMinimum().toLocation(world);
        Block block = BlockUtil.getBlock(location);
        if (block != null) {
            if (BlockUtil.isSolid(block)) {
                if (bb.getMinimum().getY() <= (boundingBox.getMinimum().getY()) && (bb.collidesVertically(boundingBox.subtract(0, 0.1f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.2f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.3f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.4f, 0, 0, 0, 0)) || bb.collidesVertically(boundingBox.subtract(0, 0.5f, 0, 0, 0, 0))) || (bb.collidesVertically(boundingBox.subtract(0, 0.12f, 0, 0, 1f, 0)))) {
                    onGround = true;
                }
                if (BlockUtil.isIce(block) && boundingBox.subtract(0, 0.5f, 0, 0, 0, 0).collidesVertically(bb)) {
                    onIce = true;
                }
                if ((bb.getMaximum().getY()) >= boundingBox.getMaximum().getY() && bb.collidesVertically(boundingBox.add(0, 0, 0, 0, 0.35f, 0))) {
                    blockAbove = true;
                }
                if (BlockUtil.isStair(block)) {
                    stair = true;
                }
                if (BlockUtil.isFence(block)) {
                    fence = true;
                }
                if (BlockUtil.isSlab(block)) {
                    slab = true;
                }
                if (BlockUtil.isPiston(block)) {
                    pistion = true;
                }
                if (BlockUtil.isClimbableBlock(block)) {
                    climbale = true;
                }

                if (BlockUtil.isChest(block)) {
                    chests = true;
                }
                if (BlockUtil.isHalfBlock(block)) {
                    halfblock = true;
                }

                if (BlockUtil.isLiquid(block)) liquid = true;

                if (block.getType() == Material.COBBLE_WALL || block.getType().getId() == 85 || block.getType().getId() == 139 || block.getType().getId() == 113 || block.getTypeId() == 188 || block.getTypeId() == 189 || block.getTypeId() == 190 || block.getTypeId() == 191 || block.getTypeId() == 192)
                    wall = true;

                if (block.getType() == Material.CARPET) carpet = true;

                if (BlockUtil.isStair(block) || BlockUtil.isSlab(block)) stairSlabs = true;

                if (Demon.getInstance().getVersion() == Demon.Version.V1_8 && block.getType() == Material.SLIME_BLOCK) {
                    slime = true;
                }

            } else {
                if (BlockUtil.isLiquid(block)) liquid = true;
                if (block.getType() == Material.WEB && boundingBox.collidesVertically(bb)) {
                    web = true;
                }
            }
        }
    }
}
