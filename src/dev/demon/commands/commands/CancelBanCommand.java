package dev.demon.commands.commands;

import dev.demon.Demon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.commands.commands
 */
public class CancelBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("cancelban")) {
            if (commandSender.isOp() || commandSender.hasPermission("bitdefender.cancelban")) {
                if (strings.length > 0) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(strings[0]);
                    if (player != null) {
                        if (Demon.getInstance().getBanQueue().contains(player.getUniqueId())) {
                            Demon.getInstance().getBanQueue().remove(player.getUniqueId());
                            commandSender.sendMessage(ChatColor.GREEN + "Canceled ban for " + ChatColor.RED + player.getName() + ChatColor.GREEN + "!");
                        } else {
                            commandSender.sendMessage(ChatColor.RED + player.getName() + " " + ChatColor.GRAY + "is not queued for a ban!");
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "Player could not be found in cache!");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "No player specified.");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "No Permission.");
            }
            return true;
        }
        return false;
    }
}
