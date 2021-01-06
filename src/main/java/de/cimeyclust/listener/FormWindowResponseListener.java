package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Location;
import de.cimeyclust.CimeyCraft;

import java.util.*;

public class FormWindowResponseListener implements Listener
{
    private CimeyCraft plugin;

    public FormWindowResponseListener(CimeyCraft plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onFormWindowResponse(PlayerFormRespondedEvent event)
    {
        Player player = event.getPlayer();
        if(event.wasClosed())
        {
            return;
        }

        if(event.getWindow() instanceof FormWindowSimple)
        {
            if(event.getResponse() instanceof FormResponseSimple)
            {
                FormWindowSimple formWindowSimple = (FormWindowSimple) event.getWindow();
                FormResponseSimple formResponseSimple = (FormResponseSimple) event.getResponse();

                String text = formResponseSimple.getClickedButton().getText();

                if(text.equals("Teleport"))
                {
                    Location teleportLocation = this.plugin.getLocationAPI().getLocation(player.getName());
                    if(teleportLocation != null)
                    {
                        player.teleport(teleportLocation);
                    }
                    else {
                        player.sendMessage("§cDu hast noch keinen Teleportations-Punkt für dich gesetzt! §cSetze ihn mit /setlocation!");
                    }
                }
                else if(text.equals("Shop"))
                {

                }
                else if(text.equals("Gilde"))
                {
                    FormWindowSimple window = new FormWindowSimple("Gilden-Management", "Hier kannst du alles, was mit Gilden zu tun hat, verwalten!");
                    if(this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler"))
                    {
                        // Gilde beitreten Button
                        ElementButton joinGuild = new ElementButton("<-Gilde beitreten->");
                        window.addButton(joinGuild);
                        player.showFormWindow(window);
                    }
                    else
                    {

                    }
                }
                else if (text.equals("<-Gilde beitreten->"))
                {
                    FormWindowSimple window = new FormWindowSimple("Trete einer Gilde bei", "Hier kannst du Gilden beitreten oder ihnen Anfragen schicken!");
                    for(String guildName : this.plugin.getGildenAPI().getGuilds())
                    {
                        window.addButton(new ElementButton(guildName+" - "+this.plugin.getGildenAPI().getGuildState(guildName)));
                    }
                    player.showFormWindow(window);
                }
                else if(this.plugin.getGildenAPI().getGuilds().contains(text.replace(" - privat", "")) || this.plugin.getGildenAPI().getGuilds().contains(text.replace(" - publik", "")))
                {
                    if(this.plugin.getGildenAPI().getGuildState(text.replace(" - publik", "")).equals("publik")) {
                        this.plugin.getGildenAPI().joinGuild(text.replace(" - publik", ""), player);
                        player.sendMessage("§aDu bist der Gilde " + text.replace(" - publik", "") + " beigetreten!");
                    }
                    else {
                        player.sendMessage("§aDu hast eine Anfrage zum Beitritt an diese Gilde gesendet!");
                    }
                }
            }
        }
    }
}
