package me.thortex.christmas.present;

import lombok.Data;
import me.thortex.christmas.util.item.Items;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import me.thortex.christmas.common.util.Colors;

import java.util.UUID;

@Data
public class Present {

    private final OfflinePlayer owner;
    private ArmorStand stand;

    public Present(OfflinePlayer owner) {
        this.owner = owner;
    }

    public void place(Location location) {
        String name = owner.getName();
        String hologram = Colors.GREEN + name + "'s Present";
        ArmorStand stand = location.getWorld().spawn(location.clone().subtract(0, 1.3, 0), ArmorStand.class);
        stand.setGravity(false);
        stand.setRemoveWhenFarAway(false);
        stand.setVisible(false);
        stand.setSmall(false);
        stand.setSilent(true);
        stand.setCustomName(hologram);
        stand.setCustomNameVisible(true);
        stand.setHelmet(Items.createPlayerHead(name, 1, hologram));

        this.stand = stand;
    }

    public boolean isOwner(UUID uuid) {
        return owner.getUniqueId().equals(uuid);
    }

}
