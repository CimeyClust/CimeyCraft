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
        return this.config.getString("plot."+chunk.getIndex()+".status");
    }

    public void buyPlot(FullChunk chunk, Player player)
    {
        this.config.set("plot."+chunk.getIndex()+".status", "owned");
        this.config.set("plot."+chunk.getIndex()+".amount", null);
        this.config.set("plot."+chunk.getIndex()+".owner", player.getName());
        this.config.save(this.file);
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
