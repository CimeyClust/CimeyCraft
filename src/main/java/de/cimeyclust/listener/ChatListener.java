package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerMessageEvent;
import de.cimeyclust.CimeyCraft;

public class ChatListener implements Listener
{
    private CimeyCraft plugin;

    public ChatListener(CimeyCraft plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnSentMessage(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage();


    }
}
