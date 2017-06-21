package me.thortex.christmas.tree;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Data;
import me.thortex.christmas.common.service.Service;
import me.thortex.christmas.common.update.UpdateEvent;
import me.thortex.christmas.common.update.UpdateType;
import me.thortex.christmas.util.Particles;
import net.minecraft.server.v1_11_R1.EnumParticle;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@Singleton
@Data
public class TreeService implements Service, Listener {

    private final JavaPlugin plugin;
    private final TreeConfig config;

    @Inject
    public TreeService(JavaPlugin plugin, TreeConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Location tree = config.getTreeLocation();
        new BukkitRunnable() {
            int radius = 20;
            double down = 0;
            double up = 0;

            @Override
            public void run() {
                down += Math.PI / 32;
                up -= Math.PI / 32;
                double downX = radius * Math.cos(down);
                double downY = down;
                double downZ = radius * Math.sin(down);

                double upX = radius * Math.sin(up);
                double upY = 10 - up;
                double upZ = radius * Math.cos(up);

                Particles.playWorldParticle(EnumParticle.FIREWORKS_SPARK,
                    tree.clone().add(downX, downY, downZ), 0, 0, 0, 0, 1);
                Particles.playWorldParticle(EnumParticle.HEART,
                    tree.clone().add(downX, downY, downZ), 0, 0, 0, 0, 1);

                Particles.playWorldParticle(EnumParticle.FIREWORKS_SPARK,
                    tree.clone().add(upX, upY, upZ), 0, 0, 0, 0, 1);
                Particles.playWorldParticle(EnumParticle.HEART,
                    tree.clone().add(upX, upY, upZ), 0, 0, 0, 0, 1);


                if (down > 10 && up < -10) {
                    down = 0;
                    up = 0;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            config.getPresentLocations().forEach(location -> Particles.playWorldParticle(
                EnumParticle.CRIT, location, 0, 0, 0, 0.2F, 5));
        }
    }

}
