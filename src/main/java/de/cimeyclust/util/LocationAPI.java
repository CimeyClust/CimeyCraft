package de.cimeyclust.util;

import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import de.cimeyclust.CimeyCraft;

import java.io.File;

public class LocationAPI
{
    private CimeyCraft plugin;
    private File file;
    private Config config;

    public LocationAPI(CimeyCraft plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "player.yml");
        this.config = new Config(this.file, Config.YAML);
    }

    public void addLocation(String path, Location location)
    {
        this.config.set("player."+path+".teleport-point.World", location.getLevel().getFolderName());
        this.config.set("player."+path+".teleport-point.X", location.getX());
        this.config.set("player."+path+".teleport-point.Y", location.getY());
        this.config.set("player."+path+".teleport-point.Z", location.getZ());
        this.config.set("player."+path+".teleport-point.Yaw", location.getYaw());
        this.config.set("player."+path+".teleport-point.Pitch", location.getPitch());
        this.config.save(this.file);
    }

    public Location getLocation(String path)
    {
        Location location = null;
        Level level = this.plugin.getServer().getLevelByName(this.config.getString("player."+path+".teleport-point.World"));
        if(level != null){
            double x = this.config.getDouble("player."+path+".teleport-point.X");
            double y = this.config.getDouble("player."+path+".teleport-point.Y");
            double z = this.config.getDouble("player."+path+".teleport-point.Z");
            double yaw = this.config.getDouble("player."+path+".teleport-point.Yaw");
            double pitch = this.config.getDouble("player."+path+".teleport-point.Pitch");
            return new Location(x, y, z, yaw, pitch, level);
        }
        return location;
    }
}
