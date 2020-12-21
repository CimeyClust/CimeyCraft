package de.cimeyclust;

import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import de.cimeyclust.command.CommandClaimPlot;
import de.cimeyclust.command.CommandSellPlot;
import de.cimeyclust.command.CommandSetLocation;
import de.cimeyclust.command.InfoCommand;
import de.cimeyclust.listener.*;
import de.cimeyclust.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CimeyCraft extends PluginBase
{
    private PlayerAPI playerAPI;
    private GildenAPI gildenAPI;
    private LocationAPI locationAPI;
    private PlotAPI plotAPI;
    private Map<UUID, ScoreBoardManagerAPI> scoreBoardManagerAPIMap = new HashMap<>();


    @Override
    public void onEnable()
    {
        this.playerAPI = new PlayerAPI(this);
        this.locationAPI = new LocationAPI(this);
        this.plotAPI = new PlotAPI(this);
        this.gildenAPI = new GildenAPI(this);

        this.registerCommand();
        this.registerListener();
        getLogger().info("§aDas Plugin wurde erfolgreich gestartet!");
    }

    @Override
    public void onDisable()
    {
        getLogger().info("§cDas Plugin wurde erfolgreich deaktiviert!");
    }

    private void registerCommand()
    {
        SimpleCommandMap commandMap = this.getServer().getCommandMap();

        commandMap.register("help", new CommandSetLocation("setLocation", "Setzt deine Teleport-Punkt-Lokation.", "§cUsage: /setLocation <name>", this));
        commandMap.register("help", new CommandClaimPlot("claim", "Claime ein freies oder zum Verkauf stehendes Plot für den Kostenbetrag!", "§cUsage: /claim", new String[]{"c"}, this));
        commandMap.register("help", new CommandSellPlot("sell", "Verkaufe dein Plot für einen festgelegten Betrag! Du hast danach keinen Zugriff mehr auf dein Plot!", "§cUsage: /sell <Betrag>", this));
        commandMap.register("help", new InfoCommand("info", "Gibt die Information des Servers und des Plugins an.", "§cUsage: /info", new String[]{"i"}));
    }

    private void registerListener()
    {
        PluginManager manager = this.getServer().getPluginManager();

        manager.registerEvents(new PlayerJoinListener(this), this);
        manager.registerEvents(new PlayerQuitListener(), this);
        manager.registerEvents(new PlayerInteractListener(), this);
        manager.registerEvents(new FormWindowResponseListener(this), this);
    }

    public PlayerAPI getPlayerAPI() {
        return playerAPI;
    }

    public LocationAPI getLocationAPI() {
        return locationAPI;
    }

    public PlotAPI getPlotAPI()
    {
        return plotAPI;
    }

    public Map<UUID, ScoreBoardManagerAPI> getScoreBoardManagerAPIMap() {
        return scoreBoardManagerAPIMap;
    }

    public GildenAPI getGildenAPI() {
        return gildenAPI;
    }
}
