package org.wookie.observer.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.wookie.observer.runtime.config.QuarkusProfile;

@Recorder
public class ComponentRecorder {

    ObserverConfig config;
    QuarkusConfig appConfig;
    RecorderService recorder;

    public ComponentRecorder(ObserverConfig config, QuarkusConfig appConfig, QuarkusProfile profile) {
        this.recorder = new RecorderService(config, profile);
        this.appConfig = appConfig;
    }


    public void registerComponent() {
        recorder.registerComponent(appConfig.name());
    }

}