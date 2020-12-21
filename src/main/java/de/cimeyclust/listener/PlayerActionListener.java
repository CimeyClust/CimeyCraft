package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.format.FullChunk;
import de.cimeyclust.CimeyCraft;
import de.cimeyclust.util.ScoreBoardManagerAPI;

public class PlayerActionListener implements Listener
{
    private CimeyCraft plugin;

    public PlayerActionListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnSwitchPlot(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        FullChunk newChunk = player.getLocation().getChunk();

        if(player.getLocation().getChunk() != newChunk)
        {
            ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
            scoreBoardManagerAPI.updateBoard("  §aCimey-Coins: §9"+this.plugin.getPlayerAPI().getPlayerCoins(player.getName()), 1);
        }
    }
}
