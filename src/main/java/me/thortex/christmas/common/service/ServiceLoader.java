package me.thortex.christmas.common.service;

import com.google.inject.Injector;
import me.thortex.christmas.common.AbstractLoader;

/**
 * Handles service loading/unloading functionality.
 *
 * @author Thortex
 */
public class ServiceLoader extends AbstractLoader<Service> {

    public ServiceLoader(Injector injector, String packageName) {
        super(injector, packageName, Service.class);
    }

    @Override
    public void startAll() {
        getInstances().forEach(Service::start);
    }

    @Override
    public void stopAll() {
        getInstances().forEach(Service::stop);
    }

}
