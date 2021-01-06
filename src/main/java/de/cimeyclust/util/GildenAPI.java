package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

    public void createGuild(String guildName, Player player, String status) {
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
            this.add(player.getChunk().getX());
        }});
        this.config.set("guild." + guildName + ".plots.plotZ", new ArrayList<Integer>(){{
            this.add(player.getChunk().getZ());
        }});
        this.config.set("guild."+guildName+".anfragen", new ArrayList<String>());
        this.addPlotToGuild(guildName, player.getChunkX(), player.getChunkZ());
        this.config.set("guild." + guildName + ".status", status);
        if(status.equals("privat"))
        {
            this.config.set("guild." + guildName + ".invites", new ArrayList<String>());
        }
        this.config.save(this.file);
    }

    public void createInvite(String guildName, Player player, String invitedPlayerName)
    {
        if((this.getGuildAdmins(guildName).contains(player.getName()) || this.getGuildOwner(guildName).equals(player.getName())) && this.getGuildState(guildName).equals("privat")) {
            List<String> list = new ArrayList<String>();
            list = this.getInvites(guildName);
            list.add(invitedPlayerName);
            this.config.set("guild." + guildName + ".invites", list);
            this.config.save(this.file);
            player.sendMessage("§a"+invitedPlayerName+" erfolgreich auf die Einladungsliste gesetzt!");
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
        for(String playerName : this.getGuildMember(guildName))
        {
            Player player = (Player) Server.getInstance().getOfflinePlayer(playerName);
            if(player.isOnline())
            {
                player = Server.getInstance().getPlayer(playerName);
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
        this.getGuilds().remove(guildName);

        this.config.remove("guild." + guildName);
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
            scoreBoardManagerAPI.updateBoard("  §aGilde: §9"+this.plugin.getPlayerAPI().getGuild(player)+" ", 3);
        }

        this.config.save(this.file);
    }

    public void addDefault()
    {
        if(!this.config.exists("guilds")) {
            this.config.set("guilds", new ArrayList<String>());
            this.config.save(this.file);
        }
    }
}
