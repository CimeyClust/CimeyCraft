package de.cimeyclust.util;

import cn.nukkit.Player;
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

    public void pay(Integer amount, Player player)
    {
        this.config.set("player."+player.getName()+".coins", (this.config.getInt("player."+player.getName()+".coins")-amount));
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aCimey-Coins: §9", 1);
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