package org.wookie.tamer;

import java.util.Random;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import io.quarkus.logging.Log;

@Path("/fur")
public class FurResource {

	@Path("/order")
	@POST
	public Skein shaveWookie(Order order) {
		Log.infof("Got order #%d for wookie fur: %s", order.orderNumber(), order);
		WookieColor wookieColor;
		try {
			wookieColor = WookieColor.valueOf(order.colour().toUpperCase());
		} catch (IllegalArgumentException e) {
			wookieColor = WookieColor.BROWN;
		}

		var skein = new Skein(wookieColor, order.orderNumber(), new Random().nextBoolean());

		Log.infof("Fulfilling order #%d with %s", order.orderNumber(), skein);

		return skein;
	}
}