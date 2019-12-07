package dev.demon.commands.commands;

import dev.demon.Demon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.commands.commands
 */
public class BitDefenderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("storch")) {
            if (commandSender.isOp() || commandSender.hasPermission("storch.command")) {
                commandSender.sendMessage(ChatColor.WHITE + "[" + ChatColor.GREEN + "!" + ChatColor.WHITE + "] " + ChatColor.RED + "Storch " + ChatColor.WHITE + Demon.getInstance().getPluginVersion());
                Demon.getInstance().getCommandManager().getCommandList().forEach((commandExecutor, commandData) -> commandSender.sendMessage(ChatColor.GRAY + "> " + ChatColor.WHITE + commandData.getUsage() + ChatColor.GRAY + " - " + ChatColor.WHITE + commandData.getDescription()));
            } else {
                commandSender.sendMessage(ChatColor.RED + "No Permission.");
            }
            return true;
        }
        return false;
    }
}
