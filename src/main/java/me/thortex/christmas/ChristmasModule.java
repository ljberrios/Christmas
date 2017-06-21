package me.thortex.christmas;

import com.google.inject.AbstractModule;
import org.bukkit.plugin.java.JavaPlugin;

public class ChristmasModule extends AbstractModule {

    private final JavaPlugin plugin;

    public ChristmasModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
    }

}
