package de.cimeyclust.command;

import cn.nukkit.command.Command;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;

public class CommandSetLocation extends Command
{
    private CimeyCraft plugin;

    public CommandSetLocation(String name, String description, String usageMessage, CimeyCraft plugin) {
        super(name, description, usageMessage);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player )
        {
            if (((Player) sender).getLocation().getLevel().getFolderName().equals("hub")) {
                Player player = (Player) sender;

                // - setLocation
                if (player.getLevel().getName().equals("hub")) {
                    this.plugin.getLocationAPI().addLocation(player.getName(), player.getLocation());
                    player.sendMessage("§aDu hast deinen §eTeleportations-Punkt §aerfolgreich gesetzt!");
                } else {
                    player.sendMessage("§cDu kannst deinen Teleportations-Punkt nur in der Overworld setzen; Nicht in einem Minispiel, im Nether oder im Ende!");
                }
            }
            else
            {
                sender.sendMessage("§cDu kannst disen Befehl nur in der Open-World ausführen!");
            }
        }
        else
        {
            sender.sendMessage("§cDu musst ein Spieler sein, um diesen Command auszuführen!");
        }

        return true;
    }

}
