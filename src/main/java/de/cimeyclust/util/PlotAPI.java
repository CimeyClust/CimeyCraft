package de.cimeyclust.util;

import cn.nukkit.Player;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;

public class PlotAPI
{
    private CimeyCraft plugin;
    private File file;
    private Config config;

    public PlotAPI(CimeyCraft plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "plots.yml");
        this.config = new Config(this.file, Config.YAML);
    }

    public void claimPlot(FullChunk chunk, Player player)
    {
        Integer x = chunk.getX();
        Integer z = chunk.getZ();
        this.config.set("plot."+chunk.getIndex()+".X", x);
        this.config.set("plot."+chunk.getIndex()+".Z", z);
        this.config.set("plot."+chunk.getIndex()+".owner", player.getName());
        this.config.set("plot."+chunk.getIndex()+".status", "owned");
        this.config.save(this.file);
    }

    public String getPlotOwner(FullChunk chunk)
    {
        return this.config.getString("plot."+chunk.getIndex()+".owner", null);
    }

    public String getPlotStatus(FullChunk chunk)
    {
        return this.config.getString("plot."+chunk.getIndex()+".status", null);
    }

    public void buyPlot(FullChunk chunk, Player player)
    {
        this.config.set("plot."+chunk.getIndex()+".status", "owned");
        this.config.remove("plot."+chunk.getIndex()+".amount");
        this.config.set("plot."+chunk.getIndex()+".owner", player.getName());
        this.config.save(this.file);
        ScoreBoardManagerAPI scoreBoardManagerAPI = this.plugin.getScoreBoardManagerAPIMap().get(player.getUniqueId());
        scoreBoardManagerAPI.updateBoard("  §aPlotowner: §9"+this.plugin.getPlotAPI().getPlotOwner(player.getLocation().getChunk()), 5);
        scoreBoardManagerAPI.updateBoard("  §aX: §9"+ player.getLocation().getChunk().getX(), 6);
        scoreBoardManagerAPI.updateBoard("  §aY: §9"+player.getLocation().getChunk().getZ(), 7);
        scoreBoardManagerAPI.updateBoard("  §aStatus: §9"+this.plugin.getPlotAPI().getPlotStatus(player.getLocation().getChunk()), 8);
    }

    public Integer getPlotAmount(FullChunk chunk)
    {
        return this.config.getInt("plot."+chunk.getIndex()+".amount");
    }

    public void sell(FullChunk chunk, Integer amount)
    {
        this.config.set("plot."+chunk.getIndex()+".status", "selling");
        this.config.set("plot."+chunk.getIndex()+".amount", amount);
        this.config.save(this.file);
    }
}
