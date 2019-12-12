package strategy;

import model.Vec2Double;
import model.Vec2Float;

public class Coordinate {

	public static Vec2Float toV2F(Vec2Double Vec2Double) {
		return new Vec2Float((float) Vec2Double.getX(), (float) Vec2Double.getY());
	}
	public static Vec2Float toV2F(Vec2Double Vec2Double, double x, double y) {
		return new Vec2Float((float) (Vec2Double.getX() + x), (float) (Vec2Double.getY() + y));
	}	
	
}
