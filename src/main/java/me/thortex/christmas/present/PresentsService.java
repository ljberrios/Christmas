package me.thortex.christmas.present;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vexsoftware.votifier.model.VotifierEvent;
import lombok.Data;
import me.thortex.christmas.present.votes.VotesEntry;
import me.thortex.christmas.tree.TreeConfig;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import me.thortex.christmas.common.service.Service;
import me.thortex.christmas.common.util.Colors;
import me.thortex.christmas.present.votes.VotesConfig;

import java.util.*;

@Singleton
@Data
public class PresentsService implements Service, Listener {

    private static final Calendar CALENDAR = new GregorianCalendar();

    private final JavaPlugin plugin;
    private final TreeConfig treesConfig;
    private final PresentsConfig presentsConfig;
    private final VotesConfig votesConfig;

    private final List<Present> presents = new ArrayList<>();

    @Inject
    public PresentsService(JavaPlugin plugin, TreeConfig treesConfig,
                           PresentsConfig presentsConfig, VotesConfig votesConfig) {
        this.plugin = plugin;
        this.treesConfig = treesConfig;
        this.presentsConfig = presentsConfig;
        this.votesConfig = votesConfig;
    }

    @Override
    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        updatePresents();
    }

    @Override
    public void stop() {
        clearPresents();
        HandlerList.unregisterAll(this);
    }

    public void addPresent(Present present) {
        presents.add(present);
    }

    public Present getPresent(Entity entity) {
        for (Present present : presents) {
            if (present.getStand().getUniqueId().equals(entity.getUniqueId())) {
                return present;
            }
        }
        return null;
    }

    public void updatePresents() {
        clearPresents();
        LinkedList<VotesEntry> entries = new LinkedList<>(votesConfig.getVotes());
        for (Location location : treesConfig.getPresentLocations()) {
            if (entries.peek() == null || isAlreadyPlaced(entries.peek().getUuid())) continue;
            Present present = new Present(Bukkit.getOfflinePlayer(entries.pop().getUuid()));
            present.place(location);
            presents.add(present);
        }
    }

    public void clearPresents() {
        presents.forEach(present -> present.getStand().remove());
        presents.clear();
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        if (CALENDAR.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER
            && CALENDAR.get(GregorianCalendar.DAY_OF_MONTH) < 25) {
            votesConfig.addVote(Bukkit.getOfflinePlayer(event.getVote().getUsername()).getUniqueId());
            updatePresents();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        Present present = getPresent(entity);
        if (present != null && present.isOwner(player.getUniqueId())) {
            event.setCancelled(true);
            if (isChristmas()) {
                if (votesConfig.getVote(player.getUniqueId()) == null) {
                    player.sendMessage(Colors.RED + "You already claimed your present!");
                    return;
                }

                presentsConfig.getItems().forEach(item -> player.getInventory().addItem(item));

                presents.remove(present);
                entity.remove();

                playFireworks(entity.getLocation().clone().add(0, 1.3, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
                Bukkit.broadcastMessage(Colors.RED + Colors.BOLD + player.getName() +
                    " has claimed his/her ChristmasPlugin reward!");
                votesConfig.removeVote(player.getUniqueId());
            } else {
                player.sendMessage(Colors.GRAY + "You must wait till December 25th to open your present!");
                player.sendMessage(Colors.GRAY + "Keep voting so you don't lose it. " +
                    "Only the top 10 voters will get one.");
            }
        }
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (getPresent(event.getRightClicked()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (getPresent(event.getEntity()) != null) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    private void playFireworks(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta data = firework.getFireworkMeta();
        data.addEffects(FireworkEffect.builder().withColor(org.bukkit.Color.RED)
            .with(FireworkEffect.Type.STAR).trail(true).withFade(Color.GREEN).build());
        data.setPower(1);
        firework.setFireworkMeta(data);
    }

    private boolean isChristmas() {;
        return CALENDAR.get(GregorianCalendar.MONTH) == GregorianCalendar.DECEMBER
            && CALENDAR.get(GregorianCalendar.DAY_OF_MONTH) == 25;
    }

    private boolean isAlreadyPlaced(UUID uuid) {
        for (Present present : presents) {
            return present.isOwner(uuid);
        }
        return false;
    }

}
