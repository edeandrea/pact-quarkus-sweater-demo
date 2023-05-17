package org.wookie.weaver;

public record Carpet(String colour, int orderNumber, Style style) {
	public Carpet(Skein fur, int orderNumber) {
		this(fur.colour(), orderNumber, Style.Normal);
	}
}
