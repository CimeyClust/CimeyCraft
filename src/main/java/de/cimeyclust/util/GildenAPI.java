package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public List<Long> getGuildPlots(String name)
    {
        return this.config.getLongList("guild."+name+".plots");
    }

    public void joinGuild(Player player, String guildName)
    {

    }

    public void createGuild(String path, Player player, String status) {
        this.config.set("guild." + path + ".owner", player.getName());
        this.config.set("guild." + path + ".verwalter", new ArrayList<String>(){{
            this.add(player.getName());
        }});
        this.config.set("guild." + path + ".member", new ArrayList<String>(){{
            this.add(player.getName());
        }});
        this.config.set("guild." + path + ".plots", new ArrayList<Long>(){{
            this.add(player.getChunk().getIndex());
        }});
        this.config.set("guild." + path + ".status", status);
        this.config.save(this.file);
    }
}
