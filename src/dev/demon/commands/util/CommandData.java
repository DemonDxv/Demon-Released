package dev.demon.commands.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Created on 30/10/2019 Package me.jumba.bitdefender.commands.util
 */
@Getter
@Setter
public class CommandData {
    private String description, usage, command;

    public CommandData(String command, String description, String usage) {
        this.command = command;
        this.description = description;
        this.usage = usage;
    }
}
