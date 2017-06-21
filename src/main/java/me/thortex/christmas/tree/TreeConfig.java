package me.thortex.christmas.tree;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.thortex.christmas.common.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeConfig extends Configuration {

    @Inject
    public TreeConfig(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "tree.yml"), "tree.yml");
    }

    public Location getTreeLocation() {
        return stringToLocation(config.getString("tree"));
    }

    public void setTreeLocation(Location location) {
        set("tree", locationToString(location), true);
    }

    public List<Location> getPresentLocations() {
        return getConfig().getStringList("presents").stream()
            .map(this::stringToLocation)
            .collect(Collectors.toList());
    }

    public void addPresentLocation(Location location) {
        List<String> presents = getConfig().getStringList("presents");
        if (presents == null) {
            set("presents", new ArrayList<String>(), true);
            presents = getConfig().getStringList("presents");
        }

        presents.add(locationToString(location));
        set("presents", presents, true);
    }

    public File getTreeSchematic() {
        File schematic = new File(getPlugin().getDataFolder(), "tree.schematic");
        if (!schematic.exists()) {
            getPlugin().saveResource("tree.schematic", true);
        }
        return schematic;
    }

    private Location stringToLocation(String toParse) {
        World world = null;
        int x = 0, y = 0, z = 0;
        if (toParse == null) {
            return new Location(Bukkit.getWorlds().get(0), 0, 80, 0);
        }

        String[] split = toParse.split(" ");
        for (String str : split) {
            String[] data = str.split(":");
            switch (data[0]) {
                case "world":
                    world = Bukkit.getWorld(data[1]);
                    break;
                case "x":
                    x = Integer.parseInt(data[1]);
                    break;
                case "y":
                    y = Integer.parseInt(data[1]);
                    break;
                case "z":
                    z = Integer.parseInt(data[1]);
                    break;
            }
        }
        return new Location(world, x, y, z);
    }

    private String locationToString(Location location) {
        return "world:" + location.getWorld().getName() + " x:" + location.getBlockX()
            + " y:" + location.getBlockY() + " z:" + location.getBlockZ();
    }

}
