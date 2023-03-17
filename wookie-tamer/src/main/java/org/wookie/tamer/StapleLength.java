package org.wookie.tamer;

import java.util.Random;

public enum StapleLength {
	Short, Medium, Long;

	public static StapleLength findRandom() {
		return values()[new Random().nextInt(0, values().length)];
	}
}
