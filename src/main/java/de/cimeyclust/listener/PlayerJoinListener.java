package de.cimeyclust.listener;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import de.cimeyclust.CimeyCraft;
import de.cimeyclust.util.ScoreBoardManagerAPI;

public class PlayerJoinListener implements Listener
{
    private CimeyCraft plugin;

    public PlayerJoinListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerJoin( PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.setHealth(20);
        player.getFoodData().reset();
        player.setOnFire(0);
        if(!player.hasPlayedBefore())
        {
            Item navigator = Item.get(Item.COMPASS);
            navigator.setCustomName("§aNavigator");
            navigator.addEnchantment();
            player.getInventory().setItem(8, navigator);
        }

        event.setJoinMessage("§aDer Spieler §e" + player.getName() + " §ahat den Server betreten!");
    }

    @EventHandler
    public void PlayerInitializedListener(PlayerLocallyInitializedEvent event)
    {
        if(!event.getPlayer().hasPlayedBefore())
        {
            this.plugin.getPlayerAPI().addDefault(event.getPlayer());
            FormWindow window = new FormWindowSimple("§eWillkommen", "Hallo und willkommen auf unserem CimeyCraft-MCPE-RolePlay-Server!\n" +
                    "Kurzanleitung:\n1. Kaufe dir ein Plot\n2. Handle mit anderen Spielern\n3. Schütze dich vor anderen Spielern\n4. Schließe dich einer Gilde an\n" +
                    "Für weitere Hilfen und Informationen besuche unseren Discord-Server §ehttps://discord.gg/D2hcWACa93§f, frage andere Spieler oder benutze den §aNavigator.");
            event.getPlayer().showFormWindow(window);
        }
        Level level = event.getPlayer().getServer().getLevelByName("hub");
        Location loc = new Location(662, 67, 584, 0, 0, level);
        event.getPlayer().teleport(loc);
        event.getPlayer().sendTitle("§aWillkommen", "§aauf CimeyCraft");

        ScoreBoardManagerAPI scoreBoardManagerAPI = new ScoreBoardManagerAPI("§3CimeyCraft");
        scoreBoardManagerAPI.addEntry("§3Deine Daten:", 0);
        scoreBoardManagerAPI.addEntry("  §aCimey-Coins: §9"+this.plugin.getPlayerAPI().getPlayerCoins(event.getPlayer().getName()), 1);
        scoreBoardManagerAPI.addEntry("  §aStatus: §9"+this.plugin.getPlayerAPI().getPlayerGuildState(event.getPlayer())+" ", 2);
        if(!this.plugin.getPlayerAPI().getPlayerGuildState(event.getPlayer()).equals("Einsiedler"))
        {
            scoreBoardManagerAPI.addEntry("  §aGilde: §9"+this.plugin.getPlayerAPI().getGuild(event.getPlayer())+" ", 3);
        }
        scoreBoardManagerAPI.addEntry("§3Plot:", 4);
        if(!this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()).equals(""))
        {
            scoreBoardManagerAPI.addEntry("  §aPlotowner: §9"+this.plugin.getPlotAPI().getPlotOwner(event.getPlayer().getLocation().getChunk()), 5);
            scoreBoardManagerAPI.addEntry("  §aX: §9"+ event.getPlayer().getLocation().getChunk().getX(), 6);
            scoreBoardManagerAPI.addEntry("  §aY: §9"+event.getPlayer().getLocation().getChunk().getZ(), 7);
            scoreBoardManagerAPI.addEntry("  §aStatus: §9"+this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()), 8);
            if(this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()).equals("selling"))
            {
                scoreBoardManagerAPI.addEntry("  §aPreis: §9"+this.plugin.getPlotAPI().getPlotAmount(event.getPlayer().getLocation().getChunk()), 9);
            }
            else
            {
                scoreBoardManagerAPI.addEntry(" ", 9);
            }
            if(!this.plugin.getPlotAPI().getGuild(event.getPlayer().getChunk()).equals(""))
            {
                scoreBoardManagerAPI.addEntry("  §aGilde: §9" + this.plugin.getPlotAPI().getGuild(event.getPlayer().getLocation().getChunk()), 10);
            }
            else
            {
                scoreBoardManagerAPI.addEntry(" ", 10);
            }
        }
        else
        {
            scoreBoardManagerAPI.addEntry("  §aStatus: §9Frei", 5);
            scoreBoardManagerAPI.addEntry("  §aX: §9"+event.getPlayer().getLocation().getChunk().getX(), 6);
            scoreBoardManagerAPI.addEntry("  §aY: §9"+event.getPlayer().getLocation().getChunk().getZ(), 7);
            scoreBoardManagerAPI.addEntry(" ", 8);
            scoreBoardManagerAPI.addEntry(" ", 9);
            scoreBoardManagerAPI.addEntry(" ", 10);
        }
        this.plugin.getScoreBoardManagerAPIMap().put(event.getPlayer().getUniqueId(), scoreBoardManagerAPI);
        scoreBoardManagerAPI.setScoreBoard(event.getPlayer());
    }
}
