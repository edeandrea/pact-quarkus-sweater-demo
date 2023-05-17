package org.wookie.weaver;

public record Skein(String colour, String weight) {
	public Skein(String colour) {
		this(colour, null);
	}
}
