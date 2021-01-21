package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.blockentity.BlockEntityEnderChest;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import de.cimeyclust.CimeyCraft;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;

public class GildenAPI
{
    private CimeyCraft plugin;
    private Config config;
    private File file;

    public GildenAPI(CimeyCraft plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "gilden.yml");
        this.config = new Config(this.file, Config.YAML);
    }

    public String getGuildOwner(String name)
    {
        return this.config.getString("guild."+name+".owner");
    }

    public List<String> getGuildAdmins(String name)
    {
        return this.config.getStringList("guild."+name+".verwalter");
    }

    public List<String> getGuildMember(String name)
    {
        return this.config.getStringList("guild."+name+".member");
    }

    public List<Integer> getGuildPlotsX(String name)
    {
        return this.config.getIntegerList("guild."+name+".plots.plotX");
    }
    public List<Integer> getGuildPlotsZ(String name)
    {
        return this.config.getIntegerList("guild."+name+".plots.plotZ");
    }

    public String getGuildState(String guildName)
    {
        return this.config.getString("guild."+guildName+".status");
    }

    public List<String> getGuilds()
    {
        return this.config.getStringList("guilds");
    }

    public List<String> getInvites(String guildName)
    {
        return this.config.getStringList("guild."+guildName+".invites");
    }

    public double getGuildChestX(String guildName)
    {
        return this.config.getDouble("guild." + guildName + ".guildChestPositionX");
    }

    public double getGuildChestY(String guildName)
    {
        return this.config.getDouble("guild." + guildName + ".guildChestPositionY");
    }

    public double getGuildChestZ(String guildName)
    {
        return this.config.getDouble("guild." + guildName + ".guildChestPositionZ");
    }

    public long getFloatingTextParticle(String guildName)
    {
        return this.config.getLong("guild." + guildName + ".floatingTextID");
    }

    public List<String> getJoinRequest(String guildName)
    {
        return this.config.getStringList("guild." + guildName + ".anfragen");
    }

    public void createGuild(String guildName, Player player, String status, BlockEntityChest chest) {
        List<String> list = new ArrayList<String>();
        list = this.getGuilds();
        list.add(guildName);
        this.config.set("guilds", list);
        this.config.set("guild." + guildName + ".owner", player.getName());
        this.config.set("guild." + guildName + ".verwalter", new ArrayList<String>(){{
            this.add(player.getName());
        }});
        this.config.set("guild." + guildName + ".member", new ArrayList<String>());
        this.config.set("guild." + guildName + ".plots.plotX", new ArrayList<Integer>(){{
            this.add(chest.getChunk().getX());
        }});
        this.config.set("guild." + guildName + ".plots.plotZ", new ArrayList<Integer>(){{
            this.add(chest.getChunk().getZ());
        }});
        this.config.set("guild." + guildName + ".guildChestPositionX", chest.getX());
        this.config.set("guild." + guildName + ".guildChestPositionY", chest.getY());
        this.config.set("guild." + guildName + ".guildChestPositionZ", chest.getZ());
        if(status.equals("privat")) {
            this.config.set("guild." + guildName + ".anfragen", new ArrayList<String>());
        }
        this.addPlotToGuild(guildName, player.getChunkX(), player.getChunkZ());
        this.config.set("guild." + guildName + ".status", status);
        if(status.equals("privat"))
        {
            this.config.set("guild." + guildName + ".invites", new ArrayList<String>());
        }
        this.config.set("guild." + guildName + ".auftraege", new ArrayList<Dictionary>());
        Level level = Server.getInstance().getLevelByName("hub");
        Location loc = new Location(this.getGuildChestX(guildName), (this.getGuildChestY(guildName)+2), this.getGuildChestZ(guildName), 0, 0, level);
        FloatingTextParticle floatingTextParticle = new FloatingTextParticle(loc, "§a"+guildName, "Gilden-Truhe");
        level.addParticle(floatingTextParticle);
        this.config.set("guild." + guildName + ".floatingTextID", floatingTextParticle.getEntityId());
        this.config.save(this.file);
    }

    public void removeGuildFloatingText(String guildName)
    {
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.eid = this.getFloatingTextParticle(guildName);
        Server.getInstance().getOnlinePlayers().values().forEach(all -> all.dataPacket(removeEntityPacket));
    }

    public void createGuildFloatingText(String guildName)
    {
        Level level = Server.getInstance().getLevelByName("hub");
        Location loc = new Location(this.getGuildChestX(guildName), (this.getGuildChestY(guildName)+2), this.getGuildChestZ(guildName), 0, 0, level);
        FloatingTextParticle floatingTextParticle = new FloatingTextParticle(loc, "§a"+guildName, "Gilden-Truhe");
        level.addParticle(floatingTextParticle);
        this.config.set("guild." + guildName + ".floatingTextID", floatingTextParticle.getEntityId());
        this.config.save(this.file);
    }

    public void createInvite(String guildName, Player player, String invitedPlayerName)
    {
        if(this.getGuildAdmins(guildName).contains(player.getName()) || this.getGuildOwner(guildName).equals(player.getName())) {
            if(!this.getInvites(guildName).contains(invitedPlayerName)) {
                List<String> list = new ArrayList<String>();
                list = this.getInvites(guildName);
                list.add(invitedPlayerName);
                this.config.set("guild." + guildName + ".invites", list);
                this.config.save(this.file);
                player.sendMessage("§a" + invitedPlayerName + " erfolgreich auf die Einladungsliste gesetzt!");
            }
            else
            {
                player.sendMessage("§cDieser Spieler ist bereits auf der Einladungsliste!");
            }
        }
        else
        {
            player.sendMessage("§cDu musst Verwalter oder Owner der Gilde sein! Außerdem muss die Gilde privat gestellt sein, damit du Einladungen erstellen kannst.\nIn publike Gilden kann jeder Spieler joinen!");
        }
    }

    public void removeInvite(String guildName, Player player, String invitedPlayerName)
    {
        if((this.getGuildAdmins(guildName).contains(player.getName()) || this.getGuildOwner(guildName).equals(player.getName())) && this.getGuildState(guildName).equals("privat")) {
            if(this.getInvites(guildName).contains(invitedPlayerName)){
                List<String> list = new ArrayList<String>();
                list = this.getInvites(guildName);
                list.remove(invitedPlayerName);
                this.config.set("guild." + guildName + ".invites", list);
                this.config.save(this.file);

                player.sendMessage("§a"+invitedPlayerName+" erfolgreich von der Einladungsliste entfernt!\nNoch vorhanden:");
                for(String invite : this.getInvites(guildName))
                {
                    player.sendMessage(invite);
                }
            }
            else
            {
                player.sendMessage("§cDer Spielername wurde nicht in der Einladungsliste gefunden!\nVorhanden sind:");
                for(String invite : this.getInvites(guildName))
                {
                    player.sendMessage(invite);
                }
            }
        }
        else
        {
            player.sendMessage("§cDu musst Verwalter oder Owner der Gilde sein! Außerdem muss die Gilde privat gestellt sein, damit du Einladungen erstellen kannst.\nIn publike Gilden kann jeder Spieler joinen!");
        }
    }

    public void addPlotToGuild(String guildName, Integer plotX, Integer plotZ)
    {
        FullChunk chunk = this.plugin.getServer().getLevelByName("hub").getChunk(plotX, plotZ);
        this.plugin.getPlotAPI().inheritGuild(chunk, guildName);
    }

    public void removeGuild(String guildName)
    {
        List<Player> playerList = new ArrayList<Player>();
        for(String playerName : this.getGuildMember(guildName))
        {
            Player player = (Player) Server.getInstance().getOfflinePlayer(playerName);
            if(player.isOnline())
            {
                player = Server.getInstance().getPlayer(playerName);
                player.sendMessage("§cDie Gilde, der du angehörst wurde gelöscht!");

                playerList.add(player);
            }
            else
            {
                player = (Player) Server.getInstance().getOfflinePlayer(playerName);
            }
            this.plugin.getPlayerAPI().removeGuild(player);
            this.plugin.getPlayerAPI().setPlayerGuildState(player, "Einsiedler");
        }

        Integer i = 0;
        while(i < this.getGuildPlotsX(guildName).size())
        {
            Integer x = this.getGuildPlotsX(guildName).get(i);
            Integer z = this.getGuildPlotsZ(guildName).get(i);
            FullChunk chunk = this.plugin.getServer().getLevelByName("hub").getChunk(x, z);
            this.plugin.getPlotAPI().uninheritGuild(chunk);

            i += 1;
        }
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.eid = this.getFloatingTextParticle(guildName);
        Server.getInstance().getOnlinePlayers().values().forEach(all -> all.dataPacket(removeEntityPacket));

        List<String> list = new ArrayList<String>();
        list = this.getGuilds();
        list.remove(guildName);
        this.config.set("guilds", list);
        ConfigSection section = this.config.getSection("guild");
        section.remove(guildName);
        this.config.save(this.file);

        this.config.save(this.file);

        for(Player player : playerList) {
            ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
            scoreBoardManagerAPI.updateBoard("  §aStatus: §9" + this.plugin.getPlayerAPI().getPlayerGuildState(player) + " ", 2);
            if (!this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler")) {
                scoreBoardManagerAPI.updateBoard("  §aGilde: §9" + this.plugin.getPlayerAPI().getGuild(player) + " ", 3);
            }
            else
            {
                scoreBoardManagerAPI.updateBoard(" ", 3);
            }
        }
    }

    public void leaveGuild(Player player, String guildName)
    {
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        this.getGuildMember(guildName).remove(player.getName());
        Integer i = 0;
        while(i < this.getGuildPlotsX(guildName).size())
        {
            Integer x = this.getGuildPlotsX(guildName).get(i);
            Integer z = this.getGuildPlotsZ(guildName).get(i);
            FullChunk chunk = this.plugin.getServer().getLevelByName("hub").getChunk(x, z);
            if(this.plugin.getPlotAPI().getPlotOwner(chunk).equals(player.getName()))
            {
                this.plugin.getPlotAPI().uninheritGuild(chunk);
            }

            i++;
        }

        this.plugin.getPlayerAPI().setGuild(player, "");
        this.plugin.getPlayerAPI().setPlayerGuildState(player, "Einsiedler");
        scoreBoardManagerAPI.updateBoard("  §aStatus: §9" + this.plugin.getPlayerAPI().getPlayerGuildState(player) + " ", 2);
        if (!this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler")) {
            scoreBoardManagerAPI.updateBoard("  §aGilde: §9" + this.plugin.getPlayerAPI().getGuild(player) + " ", 3);
        }
        else
        {
            scoreBoardManagerAPI.updateBoard(" ", 3);
        }
    }

    public void setGuildState(String guildName, String status)
    {
        if(status.equals("privat")) {
            this.config.set("guild." + guildName + ".anfragen", new ArrayList<String>());
            this.config.set("guild." + guildName + ".invites", new ArrayList<String>());
            this.config.set("guild." + guildName + ".status", status);
        }
        else
        {
            this.config.remove("guild." + guildName + ".anfragen");
            this.config.remove("guild." + guildName + ".invites");
            this.config.set("guild." + guildName + ".status", status);
        }
        this.config.save(this.file);
    }

    public void joinGuild(String guildName, Player player)
    {
        List<String> list = new ArrayList<String>();
        list = this.getGuildMember(guildName);
        list.add(player.getName());
        this.config.set("guild." + guildName + ".member", list);
        this.plugin.getPlayerAPI().setPlayerGuildState(player, "Gildenmitglied");
        this.plugin.getPlayerAPI().setGuild(player, guildName);

        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        if(!this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler"))
        {
            scoreBoardManagerAPI.updateBoard("  §aStatus: §9"+this.plugin.getPlayerAPI().getCurrentStatus(player)+" ", 2);
            scoreBoardManagerAPI.updateBoard("  §aGilde: §9"+this.plugin.getPlayerAPI().getGuild(player)+" ", 3);
        }

        this.config.save(this.file);
    }

    public void renameGuild(String oldGuildName, String newGuildName)
    {
        Integer i = 0;
        while(i < this.getGuilds().size())
        {
            if(oldGuildName.equals(this.getGuilds().get(i)))
            {
                break;
            }
            i++;
        }
        List<String> list = new ArrayList<String>();
        list = this.getGuilds();
        list.set(i, newGuildName);
        this.config.set("guilds" ,list);
        for(String member : this.getGuildMember(oldGuildName))
        {
            if(this.plugin.getServer().getOfflinePlayer(member).isOnline())
            {
                Player player = this.plugin.getServer().getPlayer(member);
                this.plugin.getPlayerAPI().setGuild(player, newGuildName);
                player.sendMessage("Der Gildenname deiner Gilde wurde von " + oldGuildName + " zu " + newGuildName + " geändert!");
            }
            else
            {
                this.plugin.getPlayerAPI().setGuildWithPlayName(member, newGuildName);
            }
        }

        String owner = this.getGuildOwner(oldGuildName);
        List<String> admins = this.getGuildAdmins(oldGuildName);
        List<String> members = this.getGuildMember(oldGuildName);
        List<Integer> plotsX = this.getGuildPlotsX(oldGuildName);
        List<Integer> plotsZ = this.getGuildPlotsZ(oldGuildName);
        Double chestPositionX = this.getGuildChestX(oldGuildName);
        Double chestPositionY = this.getGuildChestY(oldGuildName);
        Double chestPositionZ = this.getGuildChestZ(oldGuildName);
        String status = this.getGuildState(oldGuildName);
        List<String> anfragen = null;
        List<String> invites = null;
        if(this.getGuildState(oldGuildName).equals("privat")) {
            anfragen = this.getJoinRequest(oldGuildName);
            invites = this.getInvites(oldGuildName);
        }
        Long floatingTextID = this.getFloatingTextParticle(oldGuildName);

        this.config.remove("guild."+oldGuildName);
        this.config.save(this.file);

        this.config.set("guild." + newGuildName + ".owner", owner);
        this.config.set("guild." + newGuildName + ".verwalter", admins);
        this.config.set("guild." + newGuildName + ".member", members);
        this.config.set("guild." + newGuildName + ".plots.plotX", plotsX);
        this.config.set("guild." + newGuildName + ".plots.plotZ", plotsZ);
        this.config.set("guild." + newGuildName + ".guildChestPositionX", chestPositionX);
        this.config.set("guild." + newGuildName + ".guildChestPositionY", chestPositionY);
        this.config.set("guild." + newGuildName + ".guildChestPositionZ", chestPositionZ);
        this.config.set("guild." + newGuildName + ".status", status);
        if(status.equals("privat")) {
            this.config.set("guild." + newGuildName + ".anfragen", anfragen);
        }
        if(status.equals("privat"))
        {
            this.config.set("guild." + newGuildName + ".invites", invites);
        }
        Integer k = 0;
        while(k < plotsX.size()) {
            this.addPlotToGuild(newGuildName, plotsX.get(k), plotsZ.get(k));
            k++;
        }

        this.config.set("guild." + newGuildName + ".floatingTextID", floatingTextID);
        this.config.save(this.file);

        this.removeGuildFloatingText(oldGuildName);
        this.createGuildFloatingText(newGuildName);
    }

    public void addDefault()
    {
        if(!this.config.exists("guilds")) {
            this.config.set("guilds", new ArrayList<String>());
            this.config.save(this.file);
        }
    }
}
