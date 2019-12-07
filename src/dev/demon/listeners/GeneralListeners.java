package dev.demon.listeners;

import dev.demon.Demon;
import dev.demon.base.user.User;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.listeners
 */
public class GeneralListeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
      //  BitDefender.getInstance().getUserManager().addUser(new User(e.getPlayer()));
//        TinyProtocolHandler.getInstance().addChannel(e.getPlayer());
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
        //    user.player.sendMessage(ChatColor.RED+"\n\n\nDemon Anticheat is currently having checks recoded.\n" + ChatColor.RED+ "All checks are currently disabled.\n" + ChatColor.GREEN + "Please contact Demon#1337 on discord for testing.\n\n\n");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
       // User user = BitDefender.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
       // if (user != null) BitDefender.getInstance().getUserManager().removeUser(user);
  //      TinyProtocolHandler.getInstance().removeChannel(e.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType().isSolid() && e.isCancelled()) {
            User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
            if (user != null) {
                user.lastBlockBreakCancel = System.currentTimeMillis();
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            if (e.isCancelled()) {
             //   Bukkit.broadcastMessage("cancel");
                user.lastCancelPlace = System.currentTimeMillis();
            }
        }
    }
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
           if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
               user.teleportedNoMove = true;
               user.setLastTeleport(System.currentTimeMillis());
           } else {
               user.lastUnknownTeleport = System.currentTimeMillis();
           }
        }
    }

    @EventHandler
    public void onGamemodeSwitch(PlayerGameModeChangeEvent e) {
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            user.setLastGamemodeSwitch(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onFlyToggle(PlayerToggleFlightEvent e) {
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            user.setLastFlightToggle(System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        User user = Demon.getInstance().getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            /*
                Don't put too much on this event as it can cause too much lag to the server
             */
            user.setOldTo(e.getTo());
            user.setOldFrom(e.getFrom());
            user.setLastBukkitMovement(System.currentTimeMillis());
        }
    }
}
