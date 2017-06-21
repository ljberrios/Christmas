package me.thortex.christmas;

import com.google.inject.Injector;
import lombok.Data;
import me.thortex.christmas.common.command.CommandLoader;
import me.thortex.christmas.common.service.ServiceLoader;

@Data
public final class ChristmasLoader {

    public static final String PACKAGE = "me.thortex.christmas";

    private final Injector injector;

    private final ServiceLoader services;
    private final CommandLoader commands;

    public ChristmasLoader(Injector injector) {
        this.injector = injector;
        services = new ServiceLoader(injector, PACKAGE);
        commands = new CommandLoader(injector, PACKAGE);
    }

    public void start() {
        services.startAll();
        commands.startAll();
    }

    public void stop() {
        services.stopAll();
        commands.stopAll();
    }

}
