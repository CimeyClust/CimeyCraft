package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;

public class CommandGiveMoney extends Command {

    private CimeyCraft plugin;

    public CommandGiveMoney(String name, String description, String usageMessage, CimeyCraft plugin) {
        super(name, description, usageMessage);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender.isOp())
        {
            if(args.length == 2)
            {
                Player target = Server.getInstance().getPlayer(args[1]);

                if(target == null)
                {
                    sender.sendMessage("§cDer Spieler wurde nicht gefunden! Stelle sicher, dass er online ist.");
                }
                else
                {
                    try
                    {
                        Integer amount = Integer.parseInt(args[0]);

                        this.plugin.getPlayerAPI().incomeByPlayer(amount, target);
                        sender.sendMessage("§cErfolgreich den Betrag zum Guthaben des Spielers hinzugefügt!");

                    }
                    catch (Exception e)
                    {
                        sender.sendMessage("§cDu hast keine Zahl als Betrag angegeben!");
                    }
                }
            }
            else
            {
                sender.sendMessage("§cZu wenig Argumente angegeben!\nUsage: "+this.getUsage());
            }
        }
        else
        {
            sender.sendMessage("§cDu hast nicht die nötigen Rechte dazu!");
        }

        return true;
    }
}
