package org.wookie.tamer;

public record Skein(String colour, int orderNumber, FibreLength fibreLength) {

    /**
     * Given a wookieColor, get usable fur from the wookieColor.
     *
     * @param wookieColor the wookieColor whose fur will be spun into a skein of yarn
     */
    public Skein(WookieColor wookieColor, int orderNumber) {
			this(wookieColor.name().toLowerCase(), orderNumber, FibreLength.Long);
    }
}
