package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
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
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        if(this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()) != "null")
        {
            scoreBoardManagerAPI.updateBoard("  §aPlotowner: §9"+this.plugin.getPlotAPI().getPlotOwner(event.getPlayer().getLocation().getChunk()), 5);
            scoreBoardManagerAPI.updateBoard("  §aX: §9"+ event.getPlayer().getLocation().getChunk().getX(), 6);
            scoreBoardManagerAPI.updateBoard("  §aY: §9"+event.getPlayer().getLocation().getChunk().getZ(), 7);
            scoreBoardManagerAPI.updateBoard("  §aStatus: §9"+this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()), 8);
            if(this.plugin.getPlotAPI().getPlotStatus(event.getPlayer().getLocation().getChunk()).equals("selling"))
            {
                scoreBoardManagerAPI.updateBoard("  §aPreis: §9"+this.plugin.getPlotAPI().getPlotAmount(event.getPlayer().getLocation().getChunk()), 9);
            }
        }
        else {
            scoreBoardManagerAPI.updateBoard("  §aStatus: §9Frei", 5);
            scoreBoardManagerAPI.updateBoard("  §aX: §9"+event.getPlayer().getLocation().getChunk().getX(), 6);
            scoreBoardManagerAPI.updateBoard("  §aY: §9"+event.getPlayer().getLocation().getChunk().getZ(), 7);
            scoreBoardManagerAPI.updateBoard("", 8);
            scoreBoardManagerAPI.updateBoard("", 9);
        }
    }


}
