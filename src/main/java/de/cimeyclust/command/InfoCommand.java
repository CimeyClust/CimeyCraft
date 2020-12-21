package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class InfoCommand extends Command
{
    public InfoCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        sender.sendMessage("Dieses Plugin ist für das RP in MCPE für CimeyCraft.");

        return true;
    }
}
