package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCompass;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import de.cimeyclust.CimeyCraft;
import de.cimeyclust.util.ScoreBoardManagerAPI;

import java.util.Arrays;
import java.util.List;

public class PlayerActionListener implements Listener
{
    private CimeyCraft plugin;

    public PlayerActionListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnSwitchPlot(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLevel().getFolderName().equals("hub")) {
            if (!this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()).equals("")) {
                ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
                scoreBoardManagerAPI.updateBoard("  §aPlotowner: §9" + this.plugin.getPlotAPI().getPlotOwner(event.getPlayer().getLocation().getChunk()), 5);
                scoreBoardManagerAPI.updateBoard("  §aX: §9" + event.getPlayer().getLocation().getChunk().getX(), 6);
                scoreBoardManagerAPI.updateBoard("  §aY: §9" + event.getPlayer().getLocation().getChunk().getZ(), 7);
                scoreBoardManagerAPI.updateBoard("  §aStatus: §9" + this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()), 8);
            } else {
                ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
                scoreBoardManagerAPI.updateBoard("  §aStatus: §9Frei", 5);
                scoreBoardManagerAPI.updateBoard("  §aX: §9" + event.getPlayer().getLocation().getChunk().getX(), 6);
                scoreBoardManagerAPI.updateBoard("  §aY: §9" + event.getPlayer().getLocation().getChunk().getZ(), 7);
                scoreBoardManagerAPI.updateBoard(" ", 8);
                scoreBoardManagerAPI.updateBoard(" ", 9);
            }
        }
    }

    @EventHandler
    public void OnPlayerRespawnGiveCompassToHim(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        Item navigator = Item.get(Item.COMPASS);
        navigator.setCustomName("§aNavigator");
        navigator.addEnchantment();
        player.getInventory().setItem(8, navigator);
        Level level = event.getPlayer().getServer().getLevelByName("hub");
        Location loc = new Location(662, 67, 584, 0, 0, level);
        event.getPlayer().teleport(loc);
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event)
    {
        if(event.getEntity().getKiller() != null)
        {
            Item item = new Item(Item.PAPER);
            item.setCustomName("§c"+event.getEntity().getPlayer().getName()+" getötet von "+event.getEntity().getKiller());
            event.getEntity().dropItem(item);
        }
    }

    @EventHandler
    public void OnItemPickUp(InventoryPickupItemEvent event)
    {
        if(event.getItem().getItem().getCustomName().equals("§aNavigator") && event.getItem().getItem().getId() == Item.COMPASS)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnPlayerChat(PlayerChatEvent event)
    {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if(this.plugin.getPlayerAPI().getPlayerGuildState(player.getName()).equals("Einsiedler"))
        {
            event.setMessage("[§aEinsiedler§f] [§e"+player.getName()+"§f] >> "+message);
        }

        if(this.plugin.getPlayerAPI().getPlayerGuildState(player.getName()).equals("Gildenmitglied"))
        {
            event.setMessage("[§aEinsiedler§f] [§e"+player.getName()+"§f] >> "+message);
        }
    }
}
