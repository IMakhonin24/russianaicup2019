import behavior.Behavior;
import model.UnitAction;

public class BuildAction {
	protected Behavior behavior;
	protected UnitAction unitAction;

	public BuildAction(Behavior behavior) {
		this.behavior = behavior;
	}
	
	public UnitAction build() {	
		UnitAction action = behavior.buildAction();
		action.setShoot(false);
		return action;
	}
}