package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
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

    public boolean getGuildChest(Player player)
    {
        return this.config.getBoolean("player."+player.getName()+".currentGuild.chooseChest");
    }

    public Integer getBounty(String playerName)
    {
        return this.config.getInt("player."+playerName+".bounty.amount");
    }

    public boolean getBountsItemsOnDeath(String playerName)
    {
        return this.config.getBoolean("player."+playerName+".bounty.getItems");
    }

    public String getBountyItemReceiver(String playerName)
    {
        return this.config.getString("player."+playerName+".bounty.receiver");
    }

    public void setGuildChest(Player player)
    {
        this.config.set("player."+player.getName()+".currentGuild.chooseChest", true);
        this.config.save(this.file);
    }

    public String getCurrentName(Player player)
    {
        return this.config.getString("player."+player.getName()+".currentGuild.currentName");
    }

    public String getCurrentStatus(Player player)
    {
        return this.config.getString("player."+player.getName()+".currentGuild.currentStatus");
    }

    public void setCurrent(Player player, String name, String status)
    {
        this.config.set("player."+player.getName()+".currentGuild.currentName", name);
        this.config.set("player."+player.getName()+".currentGuild.currentStatus", status);
        this.config.save(this.file);
    }

    public void setGuild(Player player, String guildName)
    {
        this.config.set("player."+player.getName()+".guild", guildName);
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aGilde: §9"+this.plugin.getPlayerAPI().getGuild(player)+"  ", 3);
    }

    public void setGuildWithPlayName(String playerName, String guildName)
    {
        this.config.set("player."+playerName+".guild", guildName);
        this.config.save(this.file);
    }

    public void setBounty(String playerName, Integer amount)
    {
        this.config.set("player."+playerName+".bounty.amount", amount);
    }

    public void removeBounty(String playerName)
    {
        ConfigSection configSection = this.config.getSection("player."+playerName);
        configSection.remove("bounty");
        this.config.save(this.file);
    }

    public void setBountyGetItems(String playerName, Boolean getItems)
    {
        this.config.set("player."+playerName+".bounty.getItems", getItems);
    }

    public void setBountyGetItemsReceiver(String playerName, String receiverName)
    {
        this.config.set("player."+playerName+".bounty.receiver", receiverName);
    }

    public void removeGuild(Player player)
    {
        this.config.set("player."+player.getName()+".guild", "");
        this.config.save(this.file);
    }

    public boolean checkIfExists(String playerName)
    {
        return this.config.exists("player."+playerName);
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
        if(!this.config.exists("player." + player.getName())) {
            this.config.set("player." + player.getName() + ".coins", 900);
            this.config.set("player." + player.getName() + ".zugehoerigkeits-status", "Einsiedler");
            this.config.set("player."+player.getName()+".guild", "");
        }
        this.config.set("player."+player.getName()+".currentGuild.chooseChest", false);
        this.config.remove("player."+player.getName()+".currentGuild.currentName");
        this.config.remove("player."+player.getName()+".currentGuild.currentStatus");
        this.config.save(this.file);
    }
}