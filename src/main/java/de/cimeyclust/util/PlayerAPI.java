package de.cimeyclust.util;

import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;

public class PlayerAPI {

    private CimeyCraft plugin;
    private Config config;
    private File file;

    public PlayerAPI(CimeyCraft plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "player.yml");
        this.config = new Config(this.file, Config.YAML);
    }

    public Integer getPlayerCoins(String path) {
        return this.config.getInt("player."+path+".coins");
    }

    public void pay(Integer amount, String path)
    {
        this.config.set("player."+path+".coins", (this.config.getInt("player."+path+".coins")-amount));
        this.config.save(this.file);
    }

    public String getPlayerGuildState(String path) {
        return this.config.getString("player."+path+".zugehoerigkeits-status");
    }

    public void addDefault(String path) {
        if(this.getPlayerCoins(path) != null) {
            this.config.set("player." + path + ".coins", 900);
            this.config.set("player." + path + ".zugehoerigkeits-status", "Einsiedler");
            this.config.save(this.file);
        }
    }
}