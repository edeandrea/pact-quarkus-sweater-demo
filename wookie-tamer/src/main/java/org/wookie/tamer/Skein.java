package org.wookie.tamer;

import java.util.Random;

public record Skein(String colour, int orderNumber, StapleLength stapleLength, boolean tamerSurvived) {

    /**
     * Given a wookieColor, get usable fur from the wookieColor.
     *
     * @param wookieColor the wookieColor whose fur will be spun into a skein of yarn
     */
    public Skein(WookieColor wookieColor, int orderNumber, boolean tamerSurvived) {
			this(wookieColor.name().toLowerCase(), orderNumber, StapleLength.values()[new Random().nextInt(0, StapleLength.values().length)], tamerSurvived);
    }
}
