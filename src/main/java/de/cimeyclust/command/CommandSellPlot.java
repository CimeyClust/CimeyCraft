package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;

public class CommandSellPlot extends Command
{
    private CimeyCraft plugin;

    public CommandSellPlot(String name, String description, String usageMessage, CimeyCraft plugin) {
        super(name, description, usageMessage);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player)
        {
            String plotowner = this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk());
            String plotstatus = this.plugin.getPlotAPI().getPlotStatus(((Player) sender).getChunk());
            if(plotowner != null && plotstatus.equals("owned")) {
                if (((Player) sender).getName().equals(plotowner)) {
                    if (args.length == 1) {
                        try{
                            this.plugin.getPlotAPI().sell(((Player) sender).getChunk(), Integer.parseInt(args[0]));
                        }
                        catch (Exception e)
                        {
                            sender.sendMessage("§cDu hast keine Zahl angegeben!\n"+this.getUsage());
                        }
                    } else {
                        sender.sendMessage(this.getUsage());
                    }
                } else {
                    sender.sendMessage("§cDu kannst nicht das Plot eines anderen Spielers verkaufen! (Was für eine Unverschämtheit!)");
                }
            }
            else
            {
                sender.sendMessage("§cDieses Plot gehört dir nicht! Du kannst es nicht verkaufen!");
            }
        }
        else
        {
            sender.sendMessage("§cDu musst Spieler sein, um disen Command auszuführen!");
        }

        return true;
    }
}
