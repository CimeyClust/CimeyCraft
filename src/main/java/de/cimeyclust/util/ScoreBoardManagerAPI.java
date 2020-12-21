package de.cimeyclust.util;

import cn.nukkit.Player;
import de.theamychan.scoreboard.api.ScoreboardAPI;
import de.theamychan.scoreboard.network.DisplayEntry;
import de.theamychan.scoreboard.network.DisplaySlot;
import de.theamychan.scoreboard.network.Scoreboard;
import de.theamychan.scoreboard.network.ScoreboardDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ScoreBoardManagerAPI
{
    private Scoreboard scoreboard;
    private ScoreboardDisplay scoreboardDisplay;
    private Map<Integer, AtomicReference<DisplayEntry>> entry = new HashMap<>();

    public ScoreBoardManagerAPI(String title)
    {
        this.scoreboard = ScoreboardAPI.createScoreboard();
        this.scoreboardDisplay = this.scoreboard.addDisplay(DisplaySlot.SIDEBAR, "main", " "+title);
    }

    public void setScoreBoard(Player player)
    {
        ScoreboardAPI.setScoreboard(player, this.scoreboard);
    }

    public void addEntry(String line, int score)
    {
        AtomicReference<DisplayEntry> atmReference = new AtomicReference<>(this.scoreboardDisplay.addLine(line, score));
        this.entry.put(score, atmReference);
    }

    public void updateBoard(String line, int score)
    {
        if(this.entry.containsKey(score))
        {
            this.scoreboardDisplay.removeEntry(this.entry.get(score).get());
            this.entry.get(score).set(this.scoreboardDisplay.addLine(line, score));
        }
    }
}
