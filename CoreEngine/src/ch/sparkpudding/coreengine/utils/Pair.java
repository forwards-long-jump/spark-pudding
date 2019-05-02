package ch.sparkpudding.coreengine.utils;

public class Pair<X, Y> {
	private final X x;
	private final Y y;
	
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public X first() {
		return x;
	}
	
	public Y second() {
		return y;
	}
}
