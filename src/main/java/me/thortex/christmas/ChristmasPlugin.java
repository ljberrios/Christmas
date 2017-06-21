package me.thortex.christmas;

import com.google.inject.Guice;
import org.bukkit.plugin.java.JavaPlugin;

public class ChristmasPlugin extends JavaPlugin {

    private ChristmasLoader loader;

    @Override
    public void onEnable() {
        loader = new ChristmasLoader(Guice.createInjector(new ChristmasModule(this)));
        loader.start();
    }

    @Override
    public void onDisable() {
        loader.stop();
    }

}
