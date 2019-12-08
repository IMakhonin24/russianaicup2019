import behavior.Behavior;
import model.UnitAction;

public class BuildAction {

	protected Behavior behavior;
	protected UnitAction unitAction;

	public BuildAction(Behavior behavior) {
		this.behavior = behavior;
		this.unitAction = new UnitAction(); 
	}
	
	public UnitAction build() {
		unitAction.setVelocity(behavior.getVelocity());
		
		
		unitAction.setJump(behavior.getJump());
		unitAction.setJumpDown(behavior.getJumpDown());
		unitAction.setAim(behavior.getAim());
		unitAction.setShoot(behavior.getShoot());
		unitAction.setReload(behavior.getReload());
		unitAction.setSwapWeapon(behavior.getSwapWeapon());
		unitAction.setPlantMine(behavior.getPlantMine());
		return unitAction;
	}
		
}
