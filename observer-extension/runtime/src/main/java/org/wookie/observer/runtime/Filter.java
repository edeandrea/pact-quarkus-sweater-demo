package org.wookie.observer.runtime;

import java.io.IOException;
import java.util.Optional;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class Filter implements ContainerResponseFilter {

    public static final String X_CARPET_CORRELATION_ID = "X-carpet-correlation-id";

    @Inject
    ObjectMapper mapper;

    @Inject
    Instance<QuarkusConfig> appConfig;
    // Why an instance? See https://github.com/quarkusio/quarkus/issues/18333 and https://stackoverflow.com/questions/68769397/jar-rs-filter-injection-of-a-cdi-singleton-that-reference-a-configmapping-objec
    // ... but also come back and try with @StaticInitSafe once https://github.com/quarkusio/quarkus/pull/30964 is in a release

    @Inject
    Instance<RecorderService> service;

    @ConfigProperty(name = "demo.interaction-latency", defaultValue = "336")
    int latency;

    public Filter() {
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        var payload = mapper.writeValueAsString(responseContext.getEntity());
        var recorder = service.get();
        var interaction = new Interaction();

        interaction.setMethodName("[response]"); // It's a bit hard finding out the method name or URL, so pretend we don't care
        interaction.setType(Type.Response);
        interaction.setOwningComponent(appConfig.get().name());
        interaction.setPayload(payload);

				Optional.ofNullable(requestContext.getHeaderString(X_CARPET_CORRELATION_ID))
					.ifPresent(correlationId -> {
						interaction.setCorrelationId(correlationId);
						responseContext.getHeaders().add(X_CARPET_CORRELATION_ID, correlationId);
					});

        try {
            Thread.sleep(latency);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        recorder.recordInteraction(interaction);
    }
}

