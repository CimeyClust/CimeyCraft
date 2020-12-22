package de.cimeyclust.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class CommandGiveMoney extends Command {

    public CommandGiveMoney(String name, String description, String usageMessage) {
        super(name, description, usageMessage);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }
}
