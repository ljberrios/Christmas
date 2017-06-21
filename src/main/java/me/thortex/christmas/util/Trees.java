package me.thortex.christmas.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public final class Trees {

    private Trees() {}

    public static boolean placeTree(Location location, File schematic) {
        if (!schematic.exists()) {
            throw new IllegalStateException("Schematic file 'tree.schematic' doesn't exist");
        }

        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        EditSession session = worldEdit.getWorldEdit().getEditSessionFactory()
            .getEditSession(new BukkitWorld(location.getWorld()), Integer.MAX_VALUE);
        try {
            MCEditSchematicFormat.getFormat(schematic).load(schematic)
                .paste(session, new Vector(location.getX(), location.getY(), location.getZ()), false);
            return true;
        } catch (MaxChangedBlocksException | DataException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
