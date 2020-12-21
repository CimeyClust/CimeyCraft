package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
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
        player.teleport(player.getLevel().getSafeSpawn());
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
        this.plugin.getPlayerAPI().addDefault(event.getPlayer().getName());
        if(!event.getPlayer().hasPlayedBefore())
        {
            FormWindow window = new FormWindowSimple("§eWillkommen", "Hallo und willkommen auf unserem CimeyCraft-MCPE-RolePlay-Server!\n" +
                    "Kurzanleitung:\n1. Kaufe dir ein Plot\n2. Handle mit anderen Spielern\n3. Schütze dich vor anderen Spielern\n4. Schließe dich einer Gilde an\n" +
                    "Für weitere Hilfen und Informationen besuche unseren Discord-Server §ehttps://discord.gg/D2hcWACa93§f, frage andere Spieler oder benutze den §aNavigator.");
            event.getPlayer().showFormWindow(window);
        }
        event.getPlayer().sendTitle("§aWillkommen", "§aauf CimeyCraft");

        ScoreBoardManagerAPI scoreBoardManagerAPI = new ScoreBoardManagerAPI("§3CimeyCraft");
        scoreBoardManagerAPI.addEntry("§3Deine Daten:", 0);
        scoreBoardManagerAPI.addEntry("  §aCimey-Coins: §9"+this.plugin.getPlayerAPI().getPlayerCoins(event.getPlayer().getName()), 1);
        scoreBoardManagerAPI.addEntry("  §aStatus: §9"+this.plugin.getPlayerAPI().getPlayerGuildState(event.getPlayer().getName())+" ", 2);
        if(!this.plugin.getPlayerAPI().getPlayerGuildState(event.getPlayer().getName()).equals("Einsiedler"))
        {
            scoreBoardManagerAPI.addEntry("  §aRanking: §9"+this.plugin.getPlayerAPI().getPlayerGuildState(event.getPlayer().getName())+"  ", 3);
        }
        this.plugin.getScoreBoardManagerAPIMap().put(event.getPlayer().getUniqueId(), scoreBoardManagerAPI);
        scoreBoardManagerAPI.setScoreBoard(event.getPlayer());
    }
}
