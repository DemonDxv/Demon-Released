package dev.demon.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import dev.demon.utils.location.CustomLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.utils
 */
public class MathUtil {
    private static DecimalFormat decimalFormat = new DecimalFormat("##.##");

    private static ImmutableSet<Material> ground = Sets.immutableEnumSet(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
            Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH,
            Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT,
            Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
            Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL,
            Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST,
            Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE,
            Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN,
            Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER);

    public static boolean isOnGround(Location loc) {
        double diff = .3;

        return !isGround(loc.clone().add(0, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(0, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(0, -.1, -diff).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, -diff).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, -diff).getBlock().getType())
                || (getBlockHeight(loc.clone().subtract(0.0D, 0.5D, 0.0D).getBlock()) != 0.0D &&
                (!isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, 0).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, 0).getBlock().getType())
                        || !isGround(loc.clone().add(0, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(0, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())
                        || !isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())));
    }

    public static double gcd(double a, double b)
    {
        if (b == 0.0)
            return a;
        return gcd(b, a % b);
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }
    public static long gcdv(long a, long b) {
        if (b <= 0x4000) {
            return a;
        }

        return gcdv(b, a % b);
    }

    public static double getBlockHeight(Block block) {
        if (block.getTypeId() == 44) {
            return 0.5;
        }
        if (block.getTypeId() == 53) {
            return 0.5;
        }
        if (block.getTypeId() == 85) {
            return 0.2;
        }
        if (block.getTypeId() == 54 || block.getTypeId() == 130) {
            return 0.125;
        }
        return 0;
    }
    public static double normalize(double val, double min, double max) {
        if (max < min) return 0;
        return (val - min) / (max - min);
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(0, RoundingMode.UP);
        return bd.doubleValue();
    }
    public static double getHorizontalDistance(CustomLocation from, CustomLocation to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }
    public static double getHorizontalDistance(Vector to, Vector from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }
    private static boolean isGround(Material material) {
        return ground.contains(material);
    }


    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public static float getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (!pe.getType().getName().equals(pet.getName())) continue;
            return pe.getAmplifier() + 1;
        }
        return 0;
    }

    public static double decFormated(double d) {
        return Double.parseDouble(decimalFormat.format(d));
    }

    public static long gcd(long current, long previous) {
        try {
            try {
                return (previous <= 16384L) ? current : gcd(previous, current % previous);
            } catch (StackOverflowError ignored2) {
                return 100000000000L;
            }
        } catch (Exception ignored) {
            return 100000000000L;
        }
    }

    public static double getHorizontalDistanceSpeed(Location to, Location from, Player p) {
        double x = Math.abs(to.getX()) - Math.abs(from.getX());
        double z = Math.abs(to.getZ()) - Math.abs(from.getZ());
        return Math.sqrt(x * x + z * z);
    }

    public static boolean nextToWall(Player p) {
        Location loc = p.getLocation();
        Location xp = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ());
        Location xn = new Location(loc.getWorld(), loc.getX() - 0.5, loc.getY(), loc.getZ());
        Location zp = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 0.5);
        Location zn = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 0.5);
        return xp.getBlock().getType().isSolid() || xn.getBlock().getType().isSolid() || zp.getBlock().getType().isSolid() || zn.getBlock().getType().isSolid();
    }

    public static boolean looked(Location from, Location to) {
        return (from.getYaw() != 0 && to.getYaw() != 0) || (from.getPitch() != 0 && from.getPitch() != 0);
    }

    public static float getDelta(float one, float two) {
        return Math.abs(one - two);
    }

    public static double preciseRound(double value, double precision) {
        double scale = Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
