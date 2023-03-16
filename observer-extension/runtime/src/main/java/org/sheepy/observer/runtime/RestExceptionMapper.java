package org.sheepy.observer.runtime;


import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper
        implements ExceptionMapper<Exception> {
    @Context
    private RecorderService recorder;

    @Context
    private QuarkusConfig appConfig;

    @Inject
    CorrelationId correlationId;

    @Override
    public Response toResponse(Exception e) {
        Interaction interaction = new Interaction();
        interaction.setPayload("{\"exception\": \"" + e.getMessage() + "\", \"orderNumber\": \"" + correlationId.getCorrelationId() +
                "\"}");
        interaction.setCorrelationId(String.valueOf(correlationId.getCorrelationId()));
        interaction.setOwningComponent(appConfig.name());
        // For now, consider the exceptions to be requests
        interaction.setType(Type.Response);
        recorder.recordInteraction(interaction);

        // We lose some detail about the exceptions here, especially for 404, but we will live with that
        return Response.serverError().build();

    }
}
