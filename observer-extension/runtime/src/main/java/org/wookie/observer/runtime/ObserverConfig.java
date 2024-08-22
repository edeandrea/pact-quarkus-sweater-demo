package org.wookie.observer.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus")
public interface ObserverConfig {

    /**
     * The recorder server's observability base URL
     */
    @WithDefault("http://localhost:8088/recorder/")
    String baseURL();
}
