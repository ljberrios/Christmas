package me.thortex.christmas.present;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.thortex.christmas.common.config.Configuration;
import me.thortex.christmas.util.item.ItemParsing;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PresentsConfig extends Configuration {

    @Inject
    public PresentsConfig(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "presents.yml"), "presents.yml");
    }

    public List<ItemStack> getItems() {
        return config.getStringList("items").stream()
            .map(str -> ItemParsing.parseItemStack(str).getStack())
            .collect(Collectors.toList());
    }

}
