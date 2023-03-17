package org.wookie.carpetshopper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/carpet")
@RegisterRestClient(configKey = "knitter-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface KnitterService {

    @Path("/order")
    @POST
    Carpet knitCarpet(CarpetOrder order);
}
