package org.wookie.carpetshopper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/carpet")
@RegisterRestClient(configKey = "weaver-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface WeaverService {

    @Path("/order")
    @POST
    Carpet weaveCarpet(CarpetOrder order);
}
