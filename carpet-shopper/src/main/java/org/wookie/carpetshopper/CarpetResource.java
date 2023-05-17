package org.wookie.carpetshopper;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;

/**
 * This is a BFF for the carpet shopper frontend.
 * Here, BFF could be "backend for frontend" or "best friend forever."
 */
@Path("/bff")
public class CarpetResource {
    @Inject
    @RestClient
    WeaverService weaver;

    @Path("/order")
    @POST
    public Carpet order(CarpetOrder order) {
			Log.infof("Got order #%d for carpet: %s", order.orderNumber(), order);
	    var carpet = this.weaver.weaveCarpet(order);

			Log.infof("Fulfilling order #%d with %s", order.orderNumber(), carpet);
			return carpet;
    }
}