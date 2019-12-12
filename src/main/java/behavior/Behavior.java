package behavior;

import model.UnitAction;
import model.Vec2Double;

public interface Behavior {
	public String getBehaviorName();	
	public UnitAction buildAction();
}
