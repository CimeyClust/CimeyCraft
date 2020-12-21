package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;
import java.util.ArrayList;

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



    public void createGuild(String path, Player player) {
        this.config.set("guild." + path + ".owner", player.getName());
        this.config.set("player." + path + ".verwalter", new ArrayList<String>().add(player.getName()));
        this.config.set("player." + path + ".member", new ArrayList<String>().add(player.getName()));
        this.config.set("player." + path + ".status", "private");
        this.config.save(this.file);
    }
}
