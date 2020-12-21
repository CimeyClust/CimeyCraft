package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.Chunk;

import java.text.Normalizer;

public class PlayerInteractListener implements Listener
{
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
    public void OnPlayerDeathGiveCompassToHim(PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();

        Item navigator = Item.get(Item.COMPASS);
        navigator.setCustomName("§aNavigator");
        navigator.addEnchantment();
        player.getInventory().setItem(8, navigator);
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
