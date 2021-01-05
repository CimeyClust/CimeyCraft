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
        scoreBoardManagerAPI.updateBoard("  §aCimey-Coins: §9"+this.plugin.getPlayerAPI().getPlayerCoins(player.getName()), 1);
    }

    public void incomeByPlayer(Integer amount, Player player)
    {
        this.config.set("player."+player.getName()+".coins", (this.config.getInt("player."+player.getName()+".coins")+amount));
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aCimey-Coins: §9" + this.plugin.getPlayerAPI().getPlayerCoins(player.getName()), 1);
    }

    public void setPlayerGuildState(Player player, String state)
    {
        this.config.set("player."+player.getName()+".zugehoerigkeits-status", state);
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aStatus: §9"+this.plugin.getPlayerAPI().getPlayerGuildState(player)+" ", 2);
        if(!this.plugin.getPlayerAPI().getPlayerGuildState(player).equals("Einsiedler"))
        {
            scoreBoardManagerAPI.updateBoard("  §aGuild: §9"+this.plugin.getPlayerAPI().getGuild(player)+"  ", 3);
        }
    }

    public void setGuild(Player player, String guildName)
    {
        this.config.set("player."+player.getName()+".guild", guildName);
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aGilde: §9"+this.plugin.getPlayerAPI().getGuild(player)+"  ", 3);
    }

    public void removeGuild(Player player)
    {
        this.config.set("player."+player.getName()+".guild", "");
        this.config.save(this.file);
    }

    public String getGuild(Player player)
    {
        return this.config.getString("player."+player.getName()+".guild");
    }

    public void incomeByName(Integer amount, String playerName)
    {
        this.config.set("player."+playerName+".coins", (this.config.getInt("player."+playerName+".coins")+amount));
        this.config.save(this.file);
    }

    public String getPlayerGuildState(Player player) {
        return this.config.getString("player."+player.getName()+".zugehoerigkeits-status");
    }

    public void addDefault(Player player) {
        if(this.getPlayerCoins(player.getName()) != null) {
            this.config.set("player." + player.getName() + ".coins", 900);
            this.config.set("player." + player.getName() + ".zugehoerigkeits-status", "Einsiedler");
            this.config.set("player."+player.getName()+".guild", "");
            this.config.save(this.file);
        }
    }
}