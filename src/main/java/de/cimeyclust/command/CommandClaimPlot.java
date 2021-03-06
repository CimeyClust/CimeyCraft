package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;
import de.cimeyclust.util.ScoreBoardManagerAPI;

public class CommandClaimPlot extends Command {
    private CimeyCraft plugin;

    public CommandClaimPlot(String name, String description, String usageMessage, String[] aliases, CimeyCraft plugin) {
        super(name, description, usageMessage, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player)
        {
            if (((Player) sender).getLocation().getLevel().getFolderName().equals("hub")) {
                String plotowner = this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk());
                String plotstatus = this.plugin.getPlotAPI().getPlotStatus(((Player) sender).getChunk());

                if (plotstatus.equals("")) {
                    Integer money = this.plugin.getPlayerAPI().getPlayerCoins(sender.getName());

                    if (money >= 500) {
                        this.plugin.getPlotAPI().claimPlot(((Player) sender).getChunk(), ((Player) sender).getPlayer());
                        this.plugin.getPlayerAPI().pay(500, ((Player) sender).getPlayer());

                        sender.sendMessage("§aDu hast das Plot erfolgreich §egeclaimt §aund 500cc ausgegeben!");
                    } else {
                        sender.sendMessage("§cDu hast nur noch " + money + "cc, aber das Plot kostet 500cc!");
                    }
                } else if (!plotowner.equals("") && plotstatus.equals("selling")) {
                    if (plotowner.equals(sender.getName())) {
                        this.plugin.getPlotAPI().buyPlot(((Player) sender).getChunk(), ((Player) sender).getPlayer());

                        sender.sendMessage("§aDu hast den Verkauf rückgängig gemacht!\nDas Plot gehört nun wieder dir!");
                    } else if (!plotowner.equals(sender.getName())) {
                        if (this.plugin.getPlayerAPI().getPlayerCoins(sender.getName()) >= this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk())) {
                            Player target = Server.getInstance().getPlayer(this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk()));
                            if (target == null) {
                                this.plugin.getPlayerAPI().incomeByName(this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk()), this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk()));
                                sender.sendMessage("§aDer Spieler ist nicht online. Der Betrag wurde ihm offline überwiesen.");
                            }

                            this.plugin.getPlayerAPI().incomeByPlayer(this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk()), target);
                            this.plugin.getPlotAPI().buyPlot(((Player) sender).getChunk(), ((Player) sender).getPlayer());
                            this.plugin.getPlayerAPI().pay(this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk()), ((Player) sender).getPlayer());

                            sender.sendMessage("§aDu hast das Plot erfolgreich §egekauft §aund " + this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk()) + "cc ausgegeben!");
                        } else {
                            sender.sendMessage("§cDu hast nur noch " + this.plugin.getPlayerAPI().getPlayerCoins(sender.getName()) + "cc, aber das Plot kostet " + this.plugin.getPlotAPI().getPlotAmount(((Player) sender).getChunk()) + "cc!");
                        }
                    }
                } else {
                    sender.sendMessage("§cDas Plot ist schon belegt und steht nicht zum Verkauf!");
                }
            }
            else
            {
                sender.sendMessage("§cDu kannst disen Befehl nur in der Open-World ausführen!");
            }
        }
        else
        {
            sender.sendMessage("§cDu kannst diesen Command nur als Spieler ausführen!");
        }

        return true;
    }
}
