package dev.demon.listeners;

import dev.demon.Demon;
import dev.demon.base.user.User;
import dev.demon.events.BitDefenderListener;
import dev.demon.events.Listen;
import dev.demon.events.impl.PacketEvent;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.tinyprotocol.api.TinyProtocolHandler;
import dev.demon.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import dev.demon.tinyprotocol.packet.in.WrappedInFlyingPacket;
import dev.demon.tinyprotocol.packet.in.WrappedInTransactionPacket;
import dev.demon.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import dev.demon.tinyprotocol.packet.out.WrappedOutTransaction;
import dev.demon.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import dev.demon.utils.BlockAssesement;
import dev.demon.utils.BlockUtil;
import dev.demon.utils.MathUtil;
import dev.demon.utils.blockbox.BoundingBox;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.listeners
 */
public class PacketListener implements BitDefenderListener {
    @Listen
    public void onPacket(PacketEvent e) {
        User user = e.getUser();

        if (e.getType().equalsIgnoreCase(Packet.Server.KEEP_ALIVE)) {
            user.setLastServerKeepAlive(System.currentTimeMillis());
            WrappedOutTransaction transaction = new WrappedOutTransaction(0, (short) 420, false);
            TinyProtocolHandler.sendPacket(e.getPlayer(), transaction.getObject());
        }

        if (e.getType().equalsIgnoreCase(Packet.Server.TRANSACTION)) {
            WrappedOutTransaction wrappedInTransactionPacket = new WrappedOutTransaction(e.getPacket(), e.getPlayer());
            if (wrappedInTransactionPacket.getAction() == 420) {
                user.lastServerTransaction = System.currentTimeMillis();
            }
            if (wrappedInTransactionPacket.getAction() == 421) {
                user.lastServerTransaction2 = System.currentTimeMillis();
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Server.ENTITY_VELOCITY)) {
            WrappedOutVelocityPacket packet = new WrappedOutVelocityPacket(e.getPacket(), e.getPlayer());
            if (packet.getId() == e.getPlayer().getEntityId()) {
                user.setLastEntityVelocity(System.currentTimeMillis());
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.KEEP_ALIVE)) {
            user.setPing((int) (System.currentTimeMillis() - user.getLastServerKeepAlive()));
        }
        if (e.getType().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
            WrappedInUseEntityPacket entityPacket = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
            if (entityPacket.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                user.lastAttackPacket = System.currentTimeMillis();
                user.lastEntityAttacked = entityPacket.getEntity();
                if (entityPacket.getEntity() instanceof Player) {
                    User targetUser = Demon.getInstance().getUserManager().getUser(entityPacket.getEntity().getUniqueId());
                    if (targetUser != null) {
                        targetUser.lastAttackedByPlayer = System.currentTimeMillis();
                    }
                }
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.TRANSACTION)) {
            WrappedInTransactionPacket wrappedInTransactionPacket = new WrappedInTransactionPacket(e.getPacket(), e.getPlayer());
            if (wrappedInTransactionPacket.getAction() == 421) {
                long diff = Math.abs(System.currentTimeMillis() - user.lastServerTransaction2);
                long diff2 = (diff - user.lastTransaction2);
                if (diff2 > 70) {
                    user.setLaggin2(true);
                    user.setLastLag2(System.currentTimeMillis());
                } else {
                    user.setLaggin2(false);
                }
                user.lastTransaction2 = diff;
            }
            if (wrappedInTransactionPacket.getAction() == 420) {

                user.lastTransactionPing = user.transactionPing;
                user.transactionPing = (System.currentTimeMillis() - user.lastServerTransaction);

                user.lastClientTransaction = System.currentTimeMillis();
                user.transactionLast = Math.abs(user.transactionPing - user.lastTransactionPing);
                user.lastLastFucker = Math.abs(user.transactionLast - Math.abs(user.transactionPing - user.lastTransactionPing));
                user.calculatedRealPing = (int) Math.abs(user.transactionPing - user.lastTransactionPing);
                user.lastTransactionReciv = System.currentTimeMillis();
                user.lastTransUpdate = System.currentTimeMillis();
                user.lastCalulated = user.calculatedPing;
                user.calculatedPing = System.currentTimeMillis() - user.lastServerKeepAlive;
            }
            if (Math.abs(user.transactionPing - user.lastTransactionPing) > 70) {
                user.setLagging(true);
                user.setLastLag(System.currentTimeMillis());
            } else {
                user.setLagging(false);
            }
       //     user.getPlayer().sendMessage(""+user.isLagging() + " " + (System.currentTimeMillis() - user.lastLag) + " " + Math.abs(user.transactionPing - user.lastTransactionPing));
        }
        if (e.getType().equalsIgnoreCase(Packet.Client.ARM_ANIMATION)) {
            if (Demon.getInstance().getVersion().equals(Demon.Version.V1_8)) {
                user.breakingBlock = user.getPlayer().getTargetBlock((Set<Material>) null, 5).getType() != Material.AIR;
            }
            if (Demon.getInstance().getVersion().equals(Demon.Version.V1_7)) {
                user.breakingBlock = user.getPlayer().getTargetBlock((HashSet<Byte>) null, 5).getType() != Material.AIR;
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.POSITION_LOOK) || e.getType().equalsIgnoreCase(Packet.Client.POSITION) || e.getType().equalsIgnoreCase(Packet.Client.LOOK) || e.getType().equalsIgnoreCase(Packet.Client.FLYING)) {
            if (user.lastEntityAttacked != null) {
                user.reachAPastLocations.addLocation(user.lastEntityAttacked.getLocation());
            } else {
                user.reachAPastLocations.clearLocations();
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.POSITION) || e.getType().equalsIgnoreCase(Packet.Client.POSITION_LOOK) || e.getType().equalsIgnoreCase(Packet.Client.LOOK)) {
            if (user.getOldTo() != null && user.getOldFrom() != null && user.newTo != null && user.newFrom != null) {
                user.deltaX = user.getNewTo().getX() - user.getNewFrom().getX();
                user.deltaZ = user.getNewTo().getZ() - user.getNewFrom().getZ();
                user.movementSpeed = Math.sqrt(user.deltaX * user.deltaX + user.deltaZ * user.deltaZ);

                if (user.lastGroundLocation != null) {
                    if (user.getOldTo().getBlockY() > user.getOldFrom().getBlockY()) {
                        user.isWatingForHillJump = true;
                    } else if (user.isWatingForHillJump) {
                        if (user.getOldTo().clone().add(0, -1, 0).getBlock().getType() != Material.AIR && !BlockUtil.isStair(user.getOldTo().clone().add(0, -1, 0).getBlock())) {
                            user.isWatingForHillJump = false;
                            if (Math.abs(user.getPlayer().getLocation().clone().getBlockY() - user.lastBlockYHillJump) > 0) {
                                user.lastHillJump = System.currentTimeMillis();
                            }
                            user.lastBlockYHillJump = e.getPlayer().getLocation().clone().getBlockY();
                        }
                    }

                    if (user.getOldTo().getBlockY() > user.getOldFrom().getBlockY()) {
                        user.isWatingForHillJump2 = true;
                    } else if (user.isWatingForHillJump2) {
                        if (user.getOldTo().clone().add(0, -1, 0).getBlock().getType() != Material.AIR && !BlockUtil.isStair(user.getOldTo().clone().add(0, -1, 0).getBlock())) {
                            user.isWatingForHillJump2 = false;
                            if (Math.abs(user.getPlayer().getLocation().clone().getBlockY() - user.lastBlockYHillJump2) > 0) {
                                user.lastHillFall = System.currentTimeMillis();
                            }
                            user.lastBlockYHillJump2 = e.getPlayer().getLocation().clone().getBlockY();
                        }
                    }

                    user.lastHillJumpY = user.getOldTo().getBlockY();
                    user.lastHillJumpY2 = user.getOldFrom().getBlockY();
                }
                if (user.player.isOnGround()) {
                    user.lastGroundLocation2 = user.getPlayer().getLocation();
                }
                if (user.isOnGround()) {
                    user.lastGroundLocation = user.getPlayer().getLocation();
                }
            }
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_PLACE)) {
            user.lastBlockPlace = System.currentTimeMillis();
            if (user.player.getItemInHand().getType().toString().contains("SWORD")) {
                user.isBlocking = true;
            }
        }
        if (e.getType().equalsIgnoreCase(Packet.Client.BLOCK_DIG)) {
            if (user.player.getItemInHand().getType().toString().contains("SWORD")) {
                user.isBlocking = false;
            }
        }
        if (e.getType().equalsIgnoreCase(Packet.Client.HELD_ITEM_SLOT)) {
            user.isBlocking = false;
        }
        if (e.getType().equalsIgnoreCase(Packet.Client.WINDOW_CLICK)) {
            user.inventoryOpen = true;
            user.isBlocking = false;
        }

        if (e.getType().equalsIgnoreCase(Packet.Client.CLOSE_WINDOW) || e.getType().equalsIgnoreCase(Packet.Server.CLOSE_WINDOW)) {
            user.inventoryOpen = false;
            user.isBlocking = false;
        }



        if (e.getType().equalsIgnoreCase(Packet.Client.POSITION) || e.getType().equalsIgnoreCase(Packet.Client.FLYING) || e.getType().equalsIgnoreCase(Packet.Client.POSITION_LOOK) || e.getType().equalsIgnoreCase(Packet.Client.LOOK)) {
            WrappedInFlyingPacket wrappedInFlyingPacket = new WrappedInFlyingPacket(e.getPacket(), e.getPlayer());
            if (user.newFrom == null || user.newTo == null) {
                user.newFrom = new CustomLocation(0, 0, 0, 0, 0);
                user.newTo = new CustomLocation(0, 0, 0, 0, 0);
            }
            if (user.newFrom != null && user.newTo != null) {
                user.newFrom = user.newTo.clone();
                if (wrappedInFlyingPacket.isPos()) {
                    user.newTo.setX(wrappedInFlyingPacket.getX());
                    user.newTo.setY(wrappedInFlyingPacket.getY());
                    user.newTo.setZ(wrappedInFlyingPacket.getZ());
                    user.newTo.setPitch(wrappedInFlyingPacket.getPitch());
                    user.newTo.setYaw(wrappedInFlyingPacket.getYaw());
                    user.newTo.setTimeStamp(System.currentTimeMillis());
                    user.lastHasPos = System.currentTimeMillis();
                }
            }
          //  Bukkit.broadcastMessage(""+user.player.getLocation().getY());





            if (user.oldTo != user.oldFrom && user.newTo != user.newFrom) {
                double x = Math.floor(user.oldFrom.getX());
                double z = Math.floor(user.oldFrom.getZ());
                if (Math.floor(user.oldTo.getX()) != x || Math.floor(user.oldTo.getZ()) != z) {
                    if (user.teleportedNoMove && (System.currentTimeMillis() - user.lastTeleport) > 1000L && (System.currentTimeMillis() - user.lastHasPos) < 1000L && (System.currentTimeMillis() - user.lastBukkitMovement) < 1000L) {
                        user.teleportedNoMove = false;
                    }
                    user.lastFullBlockMove = System.currentTimeMillis();
                }
            }



            user.setFrom(user.getTo());
            user.setBoundingBox(new BoundingBox(user.getOldTo().toVector(), user.getOldFrom().toVector().add(new Vector(0, 1.85, 0))).grow(0.3f, 0, 0.3f));
            BlockAssesement blockAssesement = new BlockAssesement(user.getBoundingBox(), user);
            Demon.getInstance().getBlockBoxManager().getBlockBox().getCollidingBoxes(user.getPlayer().getWorld(), user.getBoundingBox().grow(0.5f, 0.35f, 0.5f).subtract(0, 0.5f, 0, 0, 0, 0)).parallelStream().forEach(bb -> blockAssesement.update(bb, user.getPlayer().getWorld()));
            user.setBlockAssesement(blockAssesement);
            user.setOnGround(blockAssesement.isOnGround());
            //Mixed mode used for detecting on-ground better
            user.setOnGroundMixed(blockAssesement.isOnGround() || MathUtil.isOnGround(user.getPlayer().getLocation()));
            //    user.setOnGround(MathUtil.isOnGround(user.getPlayer().getLocation()));
            user.update(User.UpdateType.TICKS);

            user.setTo(e.getPlayer().getLocation());
        }
    }
}
