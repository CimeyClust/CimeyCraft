package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void OnPlayerQuitEvent(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        event.setQuitMessage("§cDer Spieler §e" + player.getName() + " §chat den Server verlassen!");
    }
}
