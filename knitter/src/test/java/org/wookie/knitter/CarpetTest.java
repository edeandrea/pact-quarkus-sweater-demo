package org.wookie.knitter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CarpetTest {

    @Test
    public void carpetColourShouldMatchFurColour() {
        String colour = "mauve";
        Skein skein = new Skein(colour);
        Carpet carpet = new Carpet(skein, 10);
        assertEquals(colour, carpet.colour());
    }

}
