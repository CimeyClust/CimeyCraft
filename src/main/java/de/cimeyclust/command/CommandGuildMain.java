package de.cimeyclust.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.cimeyclust.CimeyCraft;

public class CommandGuildMain extends Command
{
    private CimeyCraft plugin;

    public CommandGuildMain(String name, String description, String usageMessage, CimeyCraft plugin) {
        super(name, description, usageMessage);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player) {
            if (args.length >= 1) {
                try {
                    String currentArgument = ((String) args[0]);

                    if (currentArgument.equals("create")) {
                        if (args.length == 3) {
                            if(this.plugin.getPlayerAPI().getPlayerGuildState(((Player) sender).getPlayer()).equals("Einsiedler")) {
                                if (this.plugin.getPlayerAPI().getPlayerCoins(((Player) sender).getPlayer().getName()) >= 1200) {
                                    if(this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk()).equals(((Player) sender).getPlayer().getName()))
                                    {
                                        try{
                                            String name = ((String) args[1]);
                                            String status = ((String) args[2]);
                                            if(status.equals("privat") || status.equals("publik"))
                                            {
                                                this.plugin.getPlayerAPI().pay(1200, ((Player) sender).getPlayer());
                                                this.plugin.getGildenAPI().createGuild(name, ((Player) sender).getPlayer(), status);
                                                this.plugin.getGildenAPI().joinGuild(name, ((Player) sender).getPlayer());
                                                sender.sendMessage("§aDu hast erfolgreich die Gilde mit dem Namen "+name+" erstellt!");
                                            }
                                            else
                                            {
                                                sender.sendMessage("§cGebe an, ob die Gilde privat oder publik sein soll!");
                                            }
                                        } catch(Exception e){
                                            sender.sendMessage("§cDu darfst keine Zahlen als Namen angeben!\n"+this.getUsage());
                                        }
                                    }
                                    else
                                    {
                                        sender.sendMessage("§cDas Plot muss dir gehören, damit du es der Gilde als Main-Plot vererben kannst!");
                                    }
                                } else {
                                    sender.sendMessage("§cDu benötigst 1200cc, um eine Gilde zu gründen, aber du hast zu wenig CimeyCoins!");
                                }
                            }
                            else {
                                sender.sendMessage("§cDu musst Einsiedler sein, um eine Gilde zu gründen!");
                            }
                        } else {
                            sender.sendMessage(this.getUsage());
                        }
                    }
                    else if (currentArgument.equals("join")) {
                        if (args.length == 2) {
                            if(this.plugin.getPlayerAPI().getPlayerGuildState(((Player) sender).getPlayer()).equals("Einsiedler")) {
                                if (this.plugin.getPlayerAPI().getPlayerCoins(((Player) sender).getPlayer().getName()) >= 1200) {
                                    if(this.plugin.getPlotAPI().getPlotOwner(((Player) sender).getChunk()).equals(((Player) sender).getPlayer().getName()))
                                    {
                                        try{
                                            String name = ((String) args[1]);
                                            String status = ((String) args[2]);
                                            if(status.equals("privat") || status.equals("publik"))
                                            {
                                                this.plugin.getPlayerAPI().pay(1200, ((Player) sender).getPlayer());
                                                this.plugin.getGildenAPI().createGuild(name, ((Player) sender).getPlayer(), status);
                                                this.plugin.getGildenAPI().joinGuild(name, ((Player) sender).getPlayer());
                                                sender.sendMessage("§aDu hast erfolgreich die Gilde mit dem Namen "+name+" erstellt!");
                                            }
                                            else
                                            {
                                                sender.sendMessage("§cGebe an, ob die Gilde privat oder publik sein soll!");
                                            }
                                        } catch(Exception e){
                                            sender.sendMessage("§cDu darfst keine Zahlen als Namen angeben!\n"+this.getUsage());
                                        }
                                    }
                                    else
                                    {
                                        sender.sendMessage("§cDas Plot muss dir gehören, damit du es der Gilde als Main-Plot vererben kannst!");
                                    }
                                } else {
                                    sender.sendMessage("§cDu benötigst 1200cc, um eine Gilde zu gründen, aber du hast zu wenig CimeyCoins!");
                                }
                            }
                            else {
                                sender.sendMessage("§cDu musst Einsiedler sein, um eine Gilde zu gründen!");
                            }
                        } else {
                            sender.sendMessage(this.getUsage());
                        }
                    }
                    else if (currentArgument.equals("invite")) {
                        if(this.plugin.getPlayerAPI().getPlayerGuildState(((Player) sender).getPlayer()).equals("Gildenmitglied")) {
                            this.plugin.getGildenAPI().createInvite(this.plugin.getPlayerAPI().getGuild(((Player) sender).getPlayer()), ((Player) sender).getPlayer());
                            sender.sendMessage("§aDerzeitige Invites: ");
                            for(String invite : this.plugin.getGildenAPI().getInvites(this.plugin.getPlayerAPI().getGuild(((Player) sender).getPlayer())))
                                {

                                }
                        }
                        else
                        {
                            sender.sendMessage("§cDu musst einer Gilde angehören!");
                        }
                    }
                    else if (currentArgument.equals("remove"))
                    {

                    }
                    else
                    {
                        sender.sendMessage(this.getUsage());
                    }
                } catch (Exception e) {
                    sender.sendMessage(this.getUsage());
                }
            } else {
                sender.sendMessage(this.getUsage());
            }
        }
        else
        {
            sender.sendMessage("§cDu musst ein spieler sein, um diesen Befehl auszuführen!");
        }

        return true;
    }
}
