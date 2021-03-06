package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBook;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import de.cimeyclust.CimeyCraft;
import de.cimeyclust.util.ScoreBoardManagerAPI;

import java.util.Map;

public class PlayerActionListener implements Listener
{
    private CimeyCraft plugin;

    public PlayerActionListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnSwitchPlot(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        if (player.getLevel().getFolderName().equals("hub")) {
            if (!this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()).equals("")) {
                scoreBoardManagerAPI.updateBoard("  §aPlotowner: §9" + this.plugin.getPlotAPI().getPlotOwner(event.getPlayer().getLocation().getChunk()), 5);
                scoreBoardManagerAPI.updateBoard("  §aX: §9" + event.getPlayer().getLocation().getChunk().getX(), 6);
                scoreBoardManagerAPI.updateBoard("  §aY: §9" + event.getPlayer().getLocation().getChunk().getZ(), 7);
                scoreBoardManagerAPI.updateBoard("  §aStatus: §9" + this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()), 8);
                if(!this.plugin.getPlotAPI().getGuild(player.getChunk()).equals(""))
                {
                    scoreBoardManagerAPI.updateBoard("  §aGilde: §9" + this.plugin.getPlotAPI().getGuild(event.getPlayer().getLocation().getChunk()), 9);
                }
                else
                {
                    scoreBoardManagerAPI.updateBoard(" ", 9);
                }
            } else {
                scoreBoardManagerAPI.updateBoard("  §aStatus: §9Frei", 5);
                scoreBoardManagerAPI.updateBoard("  §aX: §9" + event.getPlayer().getLocation().getChunk().getX(), 6);
                scoreBoardManagerAPI.updateBoard("  §aY: §9" + event.getPlayer().getLocation().getChunk().getZ(), 7);
                scoreBoardManagerAPI.updateBoard(" ", 8);
                scoreBoardManagerAPI.updateBoard(" ", 9);
            }
        }
    }

    @EventHandler
    public void onLevelUp(EntityLevelChangeEvent event)
    {
        if(event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();

            player.setMaxHealth(10+player.getExperienceLevel());
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
    public void OnPlayerChooseGuildChest(PlayerInteractEvent event)
    {
        if(event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            if (this.plugin.getPlayerAPI().getGuildChest(event.getPlayer())) {
                Block block = event.getBlock();
                if (block instanceof BlockChest) {
                    BlockEntityChest chest = (BlockEntityChest) block.getLevel().getBlockEntity(block.getLocation());
                    if (this.plugin.getPlotAPI().getPlotOwner(chest.getChunk()).equals(event.getPlayer().getName())) {
                        if(this.plugin.getPlayerAPI().getPlayerCoins(event.getPlayer().getName()) >= 1200) {
                            this.plugin.getPlayerAPI().pay(1200, event.getPlayer());
                            this.plugin.getGildenAPI().createGuild(this.plugin.getPlayerAPI().getCurrentName(event.getPlayer()), event.getPlayer(), this.plugin.getPlayerAPI().getCurrentStatus(event.getPlayer()), chest);
                            this.plugin.getGildenAPI().joinGuild(this.plugin.getPlayerAPI().getCurrentName(event.getPlayer()), event.getPlayer());
                            event.getPlayer().sendMessage("§aDu hast erfolgreich die Gilde mit dem Namen " + this.plugin.getPlayerAPI().getCurrentName(event.getPlayer()) + " erstellt!");
                            this.plugin.getPlayerAPI().addDefault(event.getPlayer());
                            event.setCancelled(true);
                        }
                        else
                        {
                            event.getPlayer().sendMessage("§cDu brauchst 1200cc, um " +
                                    "eine Gilde zu erstellen, du besitzt aber nur " + this.plugin.getPlayerAPI().getPlayerCoins(event.getPlayer().getName()) +
                                    "cc!");
                            event.setCancelled();
                        }
                    } else {
                        event.getPlayer().sendMessage("§cDu musst eine Kiste auf deinem Plot auswählen!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent event)
    {
        if(event.getEntity().getKiller() != null && event.getEntity() instanceof Player) {
            Item item = new Item(Item.PAPER);
            item.setCustomName("§c" + event.getEntity().getPlayer().getName() + " getötet von " + event.getEntity().getKiller().getName());
            event.getEntity().dropItem(item);
            if (!this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()).equals(0)) {
                this.plugin.getPlayerAPI().incomeByPlayer(this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()), event.getEntity().getKiller().getServer().getPlayer(event.getEntity().getKiller().getName()));
                if (this.plugin.getPlayerAPI().getBountsItemsOnDeath(event.getEntity().getPlayer().getName())) {
                    Player player = (Player) this.plugin.getServer().getOfflinePlayer(this.plugin.getPlayerAPI().getBountyItemReceiver(event.getEntity().getPlayer().getName()));
                    Inventory inventory = player.getInventory();
                    Inventory enderInventory = player.getEnderChestInventory();
                    Map<Integer, Item> itemContents = player.getEnderChestInventory().getContents();
                    for(Item newItem : itemContents.values())
                    {
                        if(inventory.canAddItem(newItem)) {
                            inventory.addItem(newItem);
                        }
                        else if(enderInventory.canAddItem(newItem))
                        {
                            enderInventory.addItem(newItem);
                        }
                    }
                }

                this.plugin.getPlayerAPI().removeBounty(event.getEntity().getPlayer().getName());
                if (this.plugin.getPlayerAPI().getPlayerGuildState(event.getEntity().getPlayer()).equals("Gildenmitglied")) {
                    // event.getPlayer().setDisplayName("[§9"+this.plugin.getPlayerAPI().getGuild(event.getPlayer())+"§f] §a"+event.getPlayer().getName());
                    if (!this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()).equals(0)) {
                        event.getEntity().getPlayer().setNameTag("[§9" + this.plugin.getPlayerAPI().getGuild(event.getEntity().getPlayer()) + "§f] §a" + event.getEntity().getPlayer().getName() + " §c" + this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()) + "cc");
                        event.getEntity().getPlayer().setNameTagAlwaysVisible(true);
                    } else {
                        event.getEntity().getPlayer().setNameTag("[§9" + this.plugin.getPlayerAPI().getGuild(event.getEntity().getPlayer()) + "§f] §a" + event.getEntity().getPlayer().getName());
                        event.getEntity().getPlayer().setNameTagAlwaysVisible(true);
                    }
                } else {
                    // event.getPlayer().setDisplayName("[§9Einsiedler§f] §a"+event.getPlayer().getName());
                    if (!this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()).equals(0)) {
                        event.getEntity().getPlayer().setNameTag("[§9Einsiedler§f] §a" + event.getEntity().getPlayer().getName() + " §c" + this.plugin.getPlayerAPI().getBounty(event.getEntity().getPlayer().getName()) + "cc");
                        event.getEntity().getPlayer().setNameTagAlwaysVisible(true);
                    } else {
                        event.getEntity().getPlayer().setNameTag("[§9Einsiedler§f] §a" + event.getEntity().getPlayer().getName());
                        event.getEntity().getPlayer().setNameTagAlwaysVisible(true);
                    }
                }
                this.plugin.getPlayerAPI().removeBounty(event.getEntity().getPlayer().getName());
            }
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
        if(this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler"))
        {
            event.setFormat("[§aEinsiedler§f] [§e"+player.getName()+"§f] >> " + message);
        }

        if(this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Gildenmitglied"))
        {
            event.setFormat("[§a"+this.plugin.getPlayerAPI().getGuild(player)+"§f] [§e"+player.getName()+"§f] >> " + message);
        }
    }
}
