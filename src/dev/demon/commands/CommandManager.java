package dev.demon.commands;

import dev.demon.commands.commands.BitDefenderCommand;
import dev.demon.commands.commands.CancelBanCommand;
import dev.demon.commands.util.CommandData;
import lombok.Getter;
import dev.demon.Demon;
import org.bukkit.command.CommandExecutor;

import java.util.HashMap;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.commands
 */
@Getter
public class CommandManager {
    private HashMap<CommandExecutor, CommandData> commandList = new HashMap<>();

    public CommandManager() {
        addCommand(new BitDefenderCommand(), "Demon", "/Demon", "Main Command", true);
        addCommand(new CancelBanCommand(), "CancelBan", "/cancelban <player>", "Cancels a players ban", true);

        commandList.forEach(((commandExecutor, commandData) -> Demon.getInstance().getCommand(commandData.getCommand()).setExecutor(commandExecutor)));
    }

    private void addCommand(CommandExecutor commandExecutor, String command, String usage, String description, boolean enabled) {
        if (enabled) {
            CommandData commandData = new CommandData(command, description, usage);
            commandList.put(commandExecutor, commandData);
        }
    }
}
