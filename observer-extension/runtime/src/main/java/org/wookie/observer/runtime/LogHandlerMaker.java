package org.wookie.observer.runtime;

import java.util.Optional;
import java.util.logging.Handler;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class LogHandlerMaker {

    public RuntimeValue<Optional<Handler>> create(BeanContainer beanContainer) {
        var recorder = beanContainer.beanInstance(RecorderService.class);
        var handler = new LogHandler(recorder);
        return new RuntimeValue<>(Optional.of(handler));

    }
}

