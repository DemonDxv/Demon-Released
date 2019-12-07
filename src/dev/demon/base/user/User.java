package dev.demon.base.user;

import dev.demon.utils.BlockAssesement;
import dev.demon.utils.MCSmoothing;
import dev.demon.utils.MathUtil;
import dev.demon.utils.Verbose;
import dev.demon.utils.blockbox.BoundingBox;
import dev.demon.utils.location.CustomLocation;
import dev.demon.utils.location.PastLocation;
import lombok.Getter;
import lombok.Setter;
import dev.demon.Demon;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.base.user
 */
@Getter
@Setter
@SuppressWarnings("ALL")
public class User {
    public Player player;
    private UUID uuid;

    public Location to, from, oldTo, oldFrom, lastGroundLocation, lastOnGroundLocation, lastGroundLocation2;
    private BoundingBox boundingBox = new BoundingBox(0, 0, 0, 0, 0, 0);
    private BlockAssesement blockAssesement;
    public boolean returncount, lastGround3, lastClientGround2, lastGroundGround, lastGround2, wasOnGround, inventoryOpen, isBlocking, isGcd, movedPitch, hasSentH4, hasSentH3, hasSentH2, hasSentH, hasSent, breakingBlock, packetClientGround, teleportedNoMove, clientGround, lastClientGround, lastonground, onGroundMixed, isWatingForHillJump2, laggin2, isOnSlime, slime, lagging, isWatingForHillJump, onGround, lastGround;
    public int aimassistEVerbose, aimassistAVerbose, ticksE, clientGroundTicks, clientAirTicks, predictionGVerbose, impossibleDVerbose, impossibleBVerbose, jumpPotionTicks, predictionEVerbose, predictionFVerbose, motionDTicks, motionDTicks2, autoClickerDVerbose, aimassistDVerbose, aimassistCVerbose, flightGVerbose, blockingTicks, inventoryTicks, aimLVerbose, aimKVerbose, aimJVerbose, killauraFVerbose, killauraBVerbose, killauraAVerbose, tickSendH4, velocityEVerbose, velocityDVerbose, tickSendH3, tickSendH2, velocityCVerbose, tickSendH, velocityBVerbose, velocityVerbose, tickSend, timerAVerbose, timerCVerbose, badPacksBVerbose, badPacketsAVerbose, swings, aimEVerbose, aimDVerbose, reachBVerbose, flightBVerbose, gravityVerbose, fenceTicks, wallTicks, webTicks, speedAVerbose, groundTicksMixed, airTicksMixed, speedPotionTicks, speedDVerbose, climableTicks, reachAVerbose, aimAOptifineVerbose, aimAVerbose, violation, groundTicks, airTicks, ping, stairTicks, slabTicks, blockAboveTicks, liquidTicks, iceTicks, halfBlockTicks, speedBVerbose, mountedTicks;
    public long lastPostLook, lastBlockPlace, blockDigSpam, lastBlockPlacePacket, lastCancelPlace, killauraKPosLook, lastPos, lastFakeKeep2, lastFakeTrans1, lastFakeTrans, lastFakeKeep1, lastFakeKeep, lastPostDig, lastTime, lastTime2, lastBadPacketsBBlockDig, lastHasPos, timerALastTime, lastUnknownTeleport, lastBadPacketsAFlying, lastFullBlockMove, lastAimEPositionLook, lastAimEPosition, lastBlockBreakCancel, lastAttackedByPlayer, lastHillFall, lastAlertRec, lastFlag, lastLag2, lastTransaction2, lastServerTransaction2, lastFlightToggle, lastGamemodeSwitch, lastJoin, lastLag, transactionPing, calculatedPing, lastCalulated, lastTransUpdate, lastTransactionReciv, calculatedRealPing, lastLastFucker, transactionLast, lastClientTransaction, lastServerTransaction, lastTransactionPing, lastTransDiff, lastAttackPacket, lastIce, lastHillJump, lastEntityVelocity, lastTeleport, lastBukkitMovement, lastServerKeepAlive;
    public double aimassistDVal, aimassistEVal, lastPitchDifference, lastDist2, pitchValue, lastTurn, lastGcd, avgYawSpeed, avgYawDevation, avgPitchSpeed, avgPitchDevation, lastY, lastYDiff, lastAimLPitch, lastAimKPitch, lastAimJPitch, lastPitchF, lastYawA, killauraBVal, lastAvgClickSpeed, lastPitchA, lastHorizontal4, lastHorizontal3, lastHorizontal2, lastHorizontal, lastVertical, balance, balance2, timerABal, aimassistBVerbose, lastAimAssistBVal, lastAimEDiff, lastAimDValue, predictionDLastY, flightCVerbose, flyCLastVal, flightAVerbose, lastPredictedSpeed, lastongroundspeed, lastSpeed, lastBlockYHillJump2, lastSpeedPotionMP, flightDDistance, lastAimCPitchDiff, lastAimAPitch, lastHillJumpY2, lastHillJumpY, lastBlockYHillJump, movementSpeed, deltaX, deltaZ, lastDist;
    public float lastFallDist, lastPitchC, lastYawB, lastPitchB, previous, lastYawChangeA, lastPitchChangeA;
    public double offset = Math.pow(2, 24);
    public Verbose scaffoldCVerbose = new Verbose(), scaffoldBVerbose = new Verbose(), scaffoldAVerbose = new Verbose(), killauraGVerbose = new Verbose(), gravityAVerbose = new Verbose(), fakelagCVerbose = new Verbose(), fakelagVerbose = new Verbose(), noslowAVerbose= new Verbose(), killauraLVerbose = new Verbose(), killauraKVerbose = new Verbose(), killauraJVerbose = new Verbose(), killauraIVerbose = new Verbose(), killauraHVerbose = new Verbose(), killauraCVerbose = new Verbose(), killauraEVerbose = new Verbose(), killauraDVerbose = new Verbose(), aimassistBVerbose2 = new Verbose(), criticalsAVerbose = new Verbose(), aimDVerbose2 = new Verbose(), flyDVerbose = new Verbose(), flyCVerbose = new Verbose(), flyBVerbose = new Verbose(), aimCVerbose = new Verbose(), aimBVerbose = new Verbose();
    public Entity lastEntityAttacked;
    public PastLocation reachAPastLocations = new PastLocation(), reachBPastLocations = new PastLocation();
    public CustomLocation lastFlightDLocation, newTo, newFrom;
    public LinkedList<Integer> recentCounts = new LinkedList<>(), recentCountsE = new LinkedList();

    //Optifine things
    public long lastOptifine, lastOptifineREE, lastRetardOpitfineSpam, lastAimAssistACE;
    public int optifineSmoothing2, lastClientSmoothingValue, optifineSmoothing, LastSmoothingCounter, smoothingCounter, optifineSmoothSens, optifinePitchSmooth, optifineSameCount, optifineConstantVL2, optifineConstantVL, optifineSmoothingFix, killauraAYawReset, killauraAPitchReset, aimAssistsACount, optifineSmoothingTicks;
    public MCSmoothing newPitchSmoothing = new MCSmoothing(), newYawSmoothing = new MCSmoothing(), yaw = new MCSmoothing(), pitch = new MCSmoothing(), smooth = new MCSmoothing();
    public double lastSmoothingRot2, lastSmoothingRot, lastPitchDelta, lastSmoothPitch1, lastOptifinePitchSmoothidfklol;
    public float lastYawDelta, lastSmoothYaw;
    public boolean cineCamera;

    /**
     * AutoClicker shit
     *
     */

    public int ticksB, autoClickerBVerbose, autoClickerCVerbose, ticksC, autoClickerAVerbose, ticksA, ticksD;
    public double avgSpeedB, avgDevationB, avgSpeedC, avgDevationC, avgSpeedA, avgDevationA, avgSpeedD, avgSpeedDLast, avgDevationD;

    public User(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        to = player.getLocation();
        from = player.getLocation();
        oldFrom = from;
        oldTo = to;

        lastJoin = System.currentTimeMillis();
    }


    public void update(UpdateType updateType) {
        if (updateType == UpdateType.TICKS) {
            if (player.isOnGround()) {
                clientAirTicks = 0;
                if (clientGroundTicks < 50) clientGroundTicks++;
            }else {
                clientGroundTicks = 0;
                if (clientAirTicks < 50) clientAirTicks++;
            }
            if (onGround) {
                airTicks = 0;
                if (groundTicks < 50) groundTicks++;
            } else {
                groundTicks = 0;
                if (airTicks < 50) airTicks++;
            }
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                if (jumpPotionTicks < 50) jumpPotionTicks++;
            }else {
                if (jumpPotionTicks > 0) jumpPotionTicks--;
            }
            if (inventoryOpen) {
                if (inventoryTicks < 50) inventoryTicks++;
            } else {
                inventoryTicks = 0;
            }
            if (isBlocking) {
                if (blockingTicks < 50) blockingTicks++;
            } else {
                blockingTicks = 0;
            }

            if (onGroundMixed) {
                airTicksMixed = 0;
                if (groundTicksMixed < 20) groundTicksMixed++;
            } else {
                groundTicksMixed = 0;
                if (airTicksMixed < 20) airTicksMixed++;
            }


            if (blockAssesement != null) {
                if (blockAssesement.isStair()) {
                    if (stairTicks < 20) stairTicks++;
                } else {
                    if (stairTicks > 0) stairTicks--;
                }

                if (blockAssesement.isFence()) {
                    if (fenceTicks < 20) fenceTicks++;
                } else {
                    if (fenceTicks > 0) fenceTicks--;
                }
                if (blockAssesement.isWeb()) {
                    if (webTicks < 20) webTicks++;
                } else {
                    if (webTicks > 0) webTicks--;
                }
                if (blockAssesement.isWall()) {
                    if (wallTicks < 20) wallTicks++;
                } else {
                    if (wallTicks > 0) wallTicks--;
                }

                if (blockAssesement.isSlab()) {
                    if (slabTicks < 20) slabTicks++;
                } else {
                    if (slabTicks > 0) slabTicks--;
                }

                if (blockAssesement.isBlockAbove()) {
                    if (blockAboveTicks < 20) blockAboveTicks++;
                } else {
                    if (blockAboveTicks > 0) blockAboveTicks--;
                }

                if (blockAssesement.isLiquid()) {
                    if (liquidTicks < 20) liquidTicks++;
                } else {
                    if (liquidTicks > 0) liquidTicks--;
                }

                if (blockAssesement.isOnIce()) {
                    lastIce = System.currentTimeMillis();
                    if (iceTicks < 50) iceTicks++;
                } else {
                    if (iceTicks > 0) iceTicks--;
                }

                if (blockAssesement.isHalfblock()) {
                    if (halfBlockTicks < 20) halfBlockTicks++;
                } else {
                    if (halfBlockTicks > 0) halfBlockTicks--;
                }

                if (blockAssesement.isClimbale()) {
                    if (climableTicks < 20) climableTicks++;
                } else {
                    if (climableTicks > 0) climableTicks--;
                }

            }

            if (player.getVehicle() != null) {
                if (mountedTicks < 20) mountedTicks++;
            } else {
                if (mountedTicks > 0) mountedTicks--;
            }

            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                if (speedPotionTicks < 20) speedPotionTicks++;
                lastSpeedPotionMP = MathUtil.getPotionEffectLevel(player, PotionEffectType.SPEED);
            } else {
                if (speedPotionTicks > 0) speedPotionTicks--;
            }

            if (Demon.getInstance().getVersion() == Demon.Version.V1_8) {
                if (player.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.SLIME_BLOCK) {
                    isOnSlime = true;
                }
                if (isOnSlime && player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.SLIME_BLOCK && player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.AIR) {
                    isOnSlime = false;
                }
            }

        }
    }

    public enum UpdateType {
        TICKS
    }

    public boolean isUsingOptifine() {
        return optifineSmoothing > 0 || optifineSmoothingFix > 2 || optifineConstantVL2 > 5;
    }

    public boolean generalCancel() {
        return this.player.isFlying() || this.player.getAllowFlight() || this.player.getGameMode().equals(GameMode.CREATIVE);
    }

    public boolean checkNull() {
        return to != null && from != null && newTo != null && newFrom != null && oldFrom != null && oldTo != null;
    }
}