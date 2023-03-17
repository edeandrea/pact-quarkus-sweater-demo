package org.wookie.knitter;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;

@Path("/carpet")
public class CarpetResource {
	@Inject
	@RestClient
	WookieService wookieService;

	@Path("/order")
	@POST
	public Carpet knitCarpet(CarpetOrder order) {
		Log.infof("Got order #%d for carpet: %s", order.orderNumber(), order);
		var furOrder = new FurOrder(order.colour(), order.orderNumber());
		var skein = this.wookieService.getFur(furOrder);
		var carpet = new Carpet(skein, order.orderNumber());

		Log.infof("Fulfilling order #%d with %s", order.orderNumber(), carpet);
		return carpet;
	}
}