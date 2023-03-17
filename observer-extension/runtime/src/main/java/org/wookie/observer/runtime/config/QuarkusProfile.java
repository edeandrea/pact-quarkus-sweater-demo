package org.wookie.observer.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "quarkus")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface QuarkusProfile {


    /**
     * The profile we are running with
     */
    String profile();
}
