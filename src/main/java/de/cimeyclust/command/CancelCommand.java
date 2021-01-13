package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;

public class CancelCommand extends Command
{
    private CimeyCraft plugin;

    public CancelCommand(String name, String description, String usageMessage, CimeyCraft plugin) {
        super(name, description, usageMessage);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        String action = null;
        if(sender instanceof Player)
        {
            Player player = ((Player) sender).getPlayer();
            if(this.plugin.getPlayerAPI().getGuildChest(player))
            {
                this.plugin.getPlayerAPI().addDefault(player);
                action = "Gilden-Erstellung";
            }
            if(action != null) {
                player.sendMessage("§a" + action + " wurde abgebrochen!");
            }
            else
            {
                player.sendMessage("Keine vorhandene Aktion, die abgebrochen werden könnte!");
            }
        }
        else
        {
            sender.sendMessage("§cDu kannst diesen Befehl nur als Spieler ausführen!");
        }

        return true;
    }
}
