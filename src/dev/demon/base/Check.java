package dev.demon.base;

import dev.demon.api.PlayerViolationDevEvent;
import dev.demon.api.PlayerViolationEvent;
import dev.demon.base.user.User;
import dev.demon.events.BitDefenderListener;
import dev.demon.tinyprotocol.api.Packet;
import dev.demon.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import dev.demon.Demon;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.base
 */
@Getter
@Setter
public class Check implements BitDefenderListener {
    private boolean enabled, dev;
    private int banVL = 50, addToVL = 1;
    private String checkName, type;
    private CheckType checkType;


    public Check(String checkName, String type, CheckType checkType, boolean dev, boolean enabled) {
        this.checkName = checkName;
        this.type = type;
        this.checkType = checkType;
        this.enabled = enabled;
        this.dev = dev;
    }

    public void flag(User user) {
        String violation = String.valueOf(user.getViolation());
        String alert = Demon.alertsMessage.replace("%player%", user.player.getName()).replace("%check%", checkName).replace("%checktype%", type).replace("%vl%", violation).replace("&", "§");
        String expAlert = Demon.alertsDev.replace("%player%", user.player.getName()).replace("%check%", checkName).replace("%checktype%", type).replace("%vl%", violation).replace("&", "§");
        //  Bukkit.broadcastMessage(""+violation + " "+user.violation)
        if (TimeUtils.elapsed(user.lastTeleport) < 2000L || TimeUtils.elapsed(user.lastLag) < 1000L || TimeUtils.elapsed(user.lastJoin) < 2000L || TimeUtils.elapsed(user.lastFlightToggle) < 2000L || TimeUtils.elapsed(user.lastUnknownTeleport) < 2000L) {
            return;
        }
        if (!Demon.getInstance().getBanQueue().contains(user.getUuid())) {
            if (Demon.alertsEnabled) {
                if (dev) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerViolationDevEvent(user.player, checkName, type, user.violation));
                    Demon.getInstance().getUserManager().getUsers().stream().filter(users -> users.getPlayer().hasPermission("demon.alerts")).forEach(users -> users.getPlayer().sendMessage(expAlert));
                } else {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerViolationEvent(user.player, checkName, type, user.violation));
                    Demon.getInstance().getUserManager().getUsers().stream().filter(users -> users.getPlayer().hasPermission("demon.alerts")).forEach(users -> users.getPlayer().sendMessage(alert));
                    user.setViolation(user.getViolation() + addToVL);
                }
            }
   /*         if (user.getViolation() >= banVL && !dev) {
                Demon.getInstance().getBanQueue().add(user.getUuid());
                user.setViolation(0);
                sendStaffMessage(ChatColor.RED + user.getPlayer().getName() + ChatColor.GRAY + " Will be auto-banned in " + ChatColor.RED + "15 " + ChatColor.GRAY + "seconds!", user.getPlayer().getName());
                new BukkitRunnable() {
                    int i = 0;
                    String name = user.getPlayer().getName();
                    UUID uuid = user.getUuid();

                    @Override
                    public void run() {
                        if (!Demon.getInstance().getBanQueue().contains(uuid)) {
                            if (user.getPlayer().isOnline()) {
                                user.setViolation(0);
                            }
                            this.cancel();
                            return;
                        }
                        if (i >= 15) {
                            i = 0;
                            Bukkit.broadcastMessage(" ");
                            Bukkit.broadcastMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Demon" + ChatColor.WHITE + "] " + ChatColor.WHITE + "has removed " + ChatColor.GREEN + name + " " + ChatColor.WHITE + "from the network!");
                            Bukkit.broadcastMessage(" ");
                            Demon.getInstance().getBanQueue().remove(uuid);
                            this.cancel();
                        }
                        i++;
                    }
                }.runTaskTimer(Demon.getInstance(), 20L, 20L);
            }
        }
    }*/
        }
    }


    public void sendStaffMessage(String s, String username) {
        String message = ChatColor.WHITE + "[" + ChatColor.GOLD + "!" + ChatColor.WHITE + "] " + ChatColor.GRAY + s;
        TextComponent textComponent = new TextComponent(message);
        textComponent.addExtra("\n"+ChatColor.RED + "(Click here to " + ChatColor.RED + "cancel)");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Click this will cancel the ban for " + ChatColor.GRAY + username).create()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cancelban " + username));
        Demon.getInstance().getUserManager().getUsers().parallelStream().filter(users -> users.getPlayer().isOp() || users.getPlayer().hasPermission("bitdefender.alerts")).forEach(users -> users.getPlayer().spigot().sendMessage(textComponent));
    }

    public boolean isPacketMovement(String packet) {
        return (packet.equalsIgnoreCase(Packet.Client.POSITION) || packet.equalsIgnoreCase(Packet.Client.FLYING) || packet.equalsIgnoreCase(Packet.Client.POSITION_LOOK) || packet.equalsIgnoreCase(Packet.Client.LOOK));
    }
}
