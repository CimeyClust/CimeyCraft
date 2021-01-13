package de.cimeyclust.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.*;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseData;
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

                String simpleText = formResponseSimple.getClickedButton().getText();

                if(simpleText.equals("Teleport"))
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
                else if(simpleText.equals("Shop"))
                {

                }
                else if(simpleText.equals("Gilde"))
                {
                    if(this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler"))
                    {
                        FormWindowSimple window = new FormWindowSimple("Gilden-Management", "Hier kannst du alles, was mit Gilden zu tun hat, verwalten!");
                        // Gilde beitreten Button
                        ElementButton joinGuild = new ElementButton("<-Gilde beitreten->");
                        ElementButton createGuild = new ElementButton("<-Gilde erstellen->");
                        window.addButton(joinGuild);
                        window.addButton(createGuild);
                        player.showFormWindow(window);
                    }
                    else if(this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Gildenmitglied"))
                    {
                        FormWindowCustom window = new FormWindowCustom("§a"+this.plugin.getPlayerAPI().getGuild(player));
                        // Rename Guild
                        String verwalter = null;
                        for(String newVerwalter : this.plugin.getGildenAPI().getGuildAdmins(this.plugin.getPlayerAPI().getGuild(player)))
                        {
                            if(verwalter != null)
                            {
                                verwalter = verwalter + ", " + newVerwalter;
                            }
                            else
                            {
                                verwalter = newVerwalter;
                            }
                        }
                        ElementLabel inhaber = new ElementLabel("  §aInhaber: §9"+this.plugin.getGildenAPI().getGuildOwner(this.plugin.getPlayerAPI().getGuild(player)));
                        ElementLabel verwalterElement = new ElementLabel("  §aVerwalter: §9"+verwalter);
                        ElementLabel memberNumber = new ElementLabel("  §aMitgliederzahl: §9"+String.valueOf(this.plugin.getGildenAPI().getGuildMember(this.plugin.getPlayerAPI().getGuild(player)).size()));
                        // 0
                        window.addElement(new ElementLabel("§cStats:"));
                        // 1
                        window.addElement(inhaber);
                        // 2
                        window.addElement(verwalterElement);
                        // 3
                        window.addElement(memberNumber);

                        if(this.plugin.getGildenAPI().getGuildOwner(this.plugin.getPlayerAPI().getGuild(player)).equals(player.getName())) {
                            ElementInput renameGuild = new ElementInput("Dein Gilden-Name. Gebe einen Neuen ein und klicke auf \"Senden\", um deine Gilde umzubenennen!", this.plugin.getPlayerAPI().getGuild(player), this.plugin.getPlayerAPI().getGuild(player));
                            // 4
                            window.addElement(renameGuild);

                            ElementDropdown changeStatus = new ElementDropdown("Hier kannst du den Status deiner Gilde ändern:", new ArrayList<String>() {{
                                this.add(plugin.getGildenAPI().getGuildState(plugin.getPlayerAPI().getGuild(player)));
                                if (plugin.getGildenAPI().getGuildState(plugin.getPlayerAPI().getGuild(player)).equals("privat")) {
                                    this.add("publik");
                                } else {
                                    this.add("privat");
                                }
                            }});
                            // 5
                            window.addElement(changeStatus);
                        }
                        if(this.plugin.getGildenAPI().getGuildState(this.plugin.getPlayerAPI().getGuild(player)).equals("privat") && (this.plugin.getGildenAPI().getGuildOwner(this.plugin.getPlayerAPI().getGuild(player)).equals(player.getName()) || this.plugin.getGildenAPI().getGuildAdmins(this.plugin.getPlayerAPI().getGuild(player)).contains(player.getName()))) {
                            ElementInput inviteElement = new ElementInput("Gebe den Spielernamen eines Spielers ein, den du einladen möchtest:", "Spielername");
                            // 6
                            window.addElement(inviteElement);
                        }
                        if(this.plugin.getGildenAPI().getGuildOwner(this.plugin.getPlayerAPI().getGuild(player)).equals(player.getName())) {
                            ElementToggle deleteGuild = new ElementToggle("§cGilde löschen.\nJa oder Nein", false);
                            // 7
                            window.addElement(deleteGuild);
                        }

                        player.showFormWindow(window);
                    }
                }
                else if(simpleText.equals("<-Gilde erstellen->"))
                {
                    FormWindowCustom window = new FormWindowCustom("Gilde erstellen");
                    if(this.plugin.getPlayerAPI().getPlayerCoins(player.getName()) <= 1200)
                    {
                        ElementLabel elementLabel = new ElementLabel("§cDu brauchst 1200cc, um " +
                                "eine Gilde zu erstellen, du besitzt aber nur " + this.plugin.getPlayerAPI().getPlayerCoins(player.getName()) +
                                "cc!");
                        window.addElement(elementLabel);
                    }
                    if(!this.plugin.getPlotAPI().getPlotOwner(player.getChunk()).equals(player.getName()))
                    {
                        ElementLabel elementLabel = new ElementLabel("§cDas Plot, auf dem du stehst muss dir gehören!");
                        window.addElement(elementLabel);
                    }
                    ElementInput elementInput = new ElementInput("Vergebe einen Namen für deine Gilde:", "Meine Gilde");
                    window.addElement(elementInput);
                    ElementDropdown elementDropdown = new ElementDropdown("Darf deiner Gilde jeder beitreten oder nur eingeladene Leute?:", new ArrayList<String>(){{this.add("publik"); this.add("privat");}});
                    window.addElement(elementDropdown);
                    player.showFormWindow(window);
                }

                else if (simpleText.equals("<-Gilde beitreten->"))
                {
                    FormWindowSimple window = new FormWindowSimple("Trete einer Gilde bei", "Hier kannst du Gilden beitreten oder ihnen Anfragen schicken!");
                    for(String guildName : this.plugin.getGildenAPI().getGuilds())
                    {
                        window.addButton(new ElementButton(guildName+" - "+this.plugin.getGildenAPI().getGuildState(guildName)));
                    }
                    player.showFormWindow(window);
                }
                else if(this.plugin.getGildenAPI().getGuilds().contains(simpleText.replace(" - privat", "")) || this.plugin.getGildenAPI().getGuilds().contains(simpleText.replace(" - publik", "")))
                {
                    if(this.plugin.getGildenAPI().getGuildState(simpleText.replace(" - publik", "")).equals("publik")) {
                        this.plugin.getGildenAPI().joinGuild(simpleText.replace(" - publik", ""), player);
                        player.sendMessage("§aDu bist der Gilde " + simpleText.replace(" - publik", "") + " beigetreten!");
                    }
                    else {
                        player.sendMessage("§aDu hast eine Anfrage zum Beitritt an diese Gilde gesendet!");
                    }
                }
            }
        }
        else if(event.getWindow() instanceof FormWindowCustom)
        {
            if(event.getResponse() instanceof FormResponseCustom)
            {
                FormResponseCustom formResponseCustom = (FormResponseCustom) event.getResponse();
                FormWindowCustom formWindowCustom = (FormWindowCustom) event.getWindow();

                if (formWindowCustom.getTitle().equals("Gilde erstellen")) {
                    if(this.plugin.getPlayerAPI().getPlayerCoins(player.getName()) >= 1200) {
                        if(this.plugin.getPlotAPI().getPlotOwner(player.getChunk()).equals(player.getName())) {
                            String name = formResponseCustom.getInputResponse(0);
                            FormResponseData status = formResponseCustom.getDropdownResponse(1);
                            if(!name.equals("")) {
                                this.plugin.getPlayerAPI().setGuildChest(player.getPlayer());
                                this.plugin.getPlayerAPI().setCurrent(player.getPlayer(), name, status.getElementContent());
                                player.sendMessage("§aSchlage (Standard: linke Maustaste) auf eine Truhe auf deinem Plot. Es wird zur Gilden-Truhe, auf die jeder Clanangehörige Zugriff hat!\n" +
                                        "Gebe /cancel ein, um den Vorgang abzubrechen! (Vorgang wird nach Server-Neustart und Rejoin automatisch abgebrochen!)");
                            }
                            else
                            {
                                player.sendMessage("§cDu kannst deinen Gildennamen nicht leer lassen!");
                            }
                        }
                        else
                        {
                            player.sendMessage("§cDas Plot, auf dem du stehst muss dir gehören!");
                        }
                    }
                    else
                    {
                        player.sendMessage("§cDu brauchst 1200cc, um " +
                                "eine Gilde zu erstellen, du besitzt aber nur " + this.plugin.getPlayerAPI().getPlayerCoins(player.getName()) +
                                "cc!");
                    }
                }

                else if(formWindowCustom.getTitle().equals("§a"+this.plugin.getPlayerAPI().getGuild(player)))
                {
                    if(this.plugin.getGildenAPI().getGuildOwner(this.plugin.getPlayerAPI().getGuild(player)).equals(player.getName())) {
                        String guildName = formResponseCustom.getInputResponse(4);
                        String status = formResponseCustom.getDropdownResponse(5).getElementContent();
                        String invitedName = null;
                        Boolean deleteGuild = false;
                        if(this.plugin.getGildenAPI().getGuildState(this.plugin.getPlayerAPI().getGuild(player)).equals("privat")) {
                            invitedName = formResponseCustom.getInputResponse(6);
                            deleteGuild = formResponseCustom.getToggleResponse(7);
                        }
                        else
                        {
                            deleteGuild = formResponseCustom.getToggleResponse(6);
                        }
                        if(!this.plugin.getGildenAPI().getGuildState(this.plugin.getPlayerAPI().getGuild(player)).equals(status)) {
                            this.plugin.getGildenAPI().setGuildState(this.plugin.getPlayerAPI().getGuild(player), status);
                        }
                        if(invitedName != null) {
                            if (!invitedName.equals("")) {
                                this.plugin.getGildenAPI().createInvite(this.plugin.getPlayerAPI().getGuild(player), player, invitedName);
                            }
                        }
                        if(guildName != null) {
                            if (!guildName.equals("")) {
                                if (!guildName.equals(this.plugin.getPlayerAPI().getGuild(player))) {
                                    this.plugin.getGildenAPI().renameGuild(this.plugin.getPlayerAPI().getGuild(player), guildName);
                                }
                            }
                        }
                        else
                        {
                            player.sendMessage("§cDu kannst deinen Gildennamen nicht leer lassen!");
                        }
                        if(deleteGuild.equals(true))
                        {
                            FormWindowCustom deleteGuildWindow = new FormWindowCustom("§cLösche "+this.plugin.getPlayerAPI().getGuild(player));
                            deleteGuildWindow.addElement(new ElementLabel("§cWas passiert?:"));
                            deleteGuildWindow.addElement(new ElementLabel("  §c1. Alles Geld auf der Gildenbank wird an alle Mitglieder verteilt!"));
                            deleteGuildWindow.addElement(new ElementLabel("  §c2. Alle Gildenplots werden an die ursprünglichen Besitzer zurückgegeben!"));
                            deleteGuildWindow.addElement(new ElementLabel("  §c3. Alle Mitglieder werden aus der Gilde geworfen und alle Items in der Gildentruhe werden gelöscht!"));
                            deleteGuildWindow.addElement(new ElementLabel("  §c4. Jedes Mitglied erhält seinen Status als Einsiedler zurück und Gildenpremien verfallen!"));
                            deleteGuildWindow.addElement(new ElementLabel("  §c5. Du kannst dies nicht rückgängig machen! Möchtest du dich nicht umentscheiden?!"));
                            deleteGuildWindow.addElement(new ElementInput("§cGebe den Namen deiner Gilde als Bestätigung ein!", this.plugin.getPlayerAPI().getGuild(player)));
                            deleteGuildWindow.addElement(new ElementToggle("§cKlicke und setze den Schalter um, um die Gilde zu löschen und klicke dann auf \"Senden\"!", false));
                            player.showFormWindow(deleteGuildWindow);
                        }
                    }
                    else if(this.plugin.getGildenAPI().getGuildState(this.plugin.getPlayerAPI().getGuild(player)).equals("privat") && this.plugin.getGildenAPI().getGuildAdmins(this.plugin.getPlayerAPI().getGuild(player)).contains(player.getName()))
                    {
                        String invitedName = formResponseCustom.getInputResponse(4);
                        if(!invitedName.equals(""))
                        {
                            this.plugin.getGildenAPI().createInvite(this.plugin.getPlayerAPI().getGuild(player), player, invitedName);
                        }
                    }
                }
                else if(formWindowCustom.getTitle().equals("§cLösche "+this.plugin.getPlayerAPI().getGuild(player)))
                {
                    if(formResponseCustom.getInputResponse(6).equals(this.plugin.getPlayerAPI().getGuild(player)) && formResponseCustom.getToggleResponse(7))
                    {
                        player.sendMessage("§aDeine Gilde wurde erfolgreich gelöscht. Wir hoffen, du wirst deine Entscheidung nicht bereuen.");
                        this.plugin.getGildenAPI().removeGuild(this.plugin.getPlayerAPI().getGuild(player));
                    }
                    else
                    {
                        player.sendMessage("§cEine der Bedingungen zum Löschen der Gilde wurden nicht erfüllt!");
                    }
                }
            }
        }
    }
}
