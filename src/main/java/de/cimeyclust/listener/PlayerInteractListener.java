package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import de.cimeyclust.CimeyCraft;

public class PlayerInteractListener implements Listener
{
    private CimeyCraft plugin;

    public PlayerInteractListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void OnItemDropCompass(PlayerDropItemEvent event)
    {
        Item item = event.getItem();
        Player player = event.getPlayer();
        Inventory inventory = event.getPlayer().getInventory();

        if(item.getId() == Item.COMPASS && item.getCustomName().equals("§aNavigator"))
        {
            player.sendMessage("§cDu kannst deinen Navigator nicht wegschmeißen!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnItemInteractWithNavigator(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Item item  = event.getItem();

        if(item.getId() == Item.COMPASS && item.getCustomName().equals("§aNavigator"))
        {
            FormWindowSimple window = new FormWindowSimple("Navigator", "Hier kannst du alles verwalten!");
            // Shop Button
            ElementButton shopButton = new ElementButton("Shop");
            shopButton.addImage(new ElementButtonImageData("path", "textures/items/gold_ingot.png"));
            // Teleport Button
            ElementButton teleportButton = new ElementButton("Teleport");
            teleportButton.addImage(new ElementButtonImageData("path", "textures/items/compass_item.png"));
            // Gilden Button
            ElementButton gildenButton = new ElementButton("Gilde");
            gildenButton.addImage(new ElementButtonImageData("path", "textures/blocks/beacon.png"));
            window.addButton(shopButton);
            window.addButton(teleportButton);
            window.addButton(gildenButton);
            player.showFormWindow(window);
        }
    }

    @EventHandler
    public void PlayerInteractWithBlock(PlayerInteractEvent event)
    {
        Player player = (Player) event.getPlayer();

        if(event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
        {
            if((!this.plugin.getPlotAPI().getPlotOwner(event.getBlock().getChunk()).equals(player.getName()) || !this.plugin.getPlotAPI().getPlotStatus(event.getBlock().getChunk()).equals("owned")) && !player.isOp())
            {
                event.setCancelled(true);
            }
        }
        if(event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if ((!this.plugin.getPlotAPI().getPlotOwner(event.getBlock().getChunk()).equals(player.getName()) || !this.plugin.getPlotAPI().getPlotStatus(event.getBlock().getChunk()).equals("owned")) && !player.isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerBreakWithBlock(BlockBreakEvent event)
    {
        Player player = (Player) event.getPlayer();

        if((!this.plugin.getPlotAPI().getPlotOwner(event.getBlock().getChunk()).equals(player.getName()) || !this.plugin.getPlotAPI().getPlotStatus(event.getBlock().getChunk()).equals("owned")) && !player.isOp())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnNavigatorMove(InventoryClickEvent event)
    {
        Player player = event.getPlayer();
        Item item = event.getSourceItem();

        if(item.getId() == Item.COMPASS && item.getCustomName().equals("§aNavigator"))
        {
            event.setCancelled(true);
            player.sendMessage("§cDu kannst deinen Navigator nicht verschieben!");
        }
    }
}
