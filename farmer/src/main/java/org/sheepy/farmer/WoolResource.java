package org.sheepy.farmer;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/wool")
public class WoolResource {

    @Path("/order")
    @POST
    public Skein shearSheep(Order order) {
        WookieColor wookieColor;
        try {
            wookieColor = WookieColor.valueOf(order.colour().toUpperCase());
        } catch (IllegalArgumentException e) {
            wookieColor = WookieColor.BROWN;
        }

        return new Skein(wookieColor, order.orderNumber());
    }
}