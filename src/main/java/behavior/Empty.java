package behavior;

import model.*;
import strategy.*;

public class Empty implements Behavior{

	private final String behaviorName = "Ничего неделает";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Empty(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction() {
		Unit unit = globalParams.getUnit();
		
		targetPosition = unit.getPosition();
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		action.setVelocity(velocity);
		
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    action.setJump(jump);
	    
	    boolean jumpDown = !jump;
	    action.setJumpDown(jumpDown);
	    
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    action.setAim(aim);
	    
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    action.setShoot(shoot);
	    
	    boolean reload = false;
	    action.setReload(reload);
	    
	    boolean swapWeapon = false;
	    action.setSwapWeapon(swapWeapon);
	    
	    boolean plantMine = false;
	    action.setPlantMine(plantMine);
	    
		return action;
	}

	public String getBehaviorName() {
		return behaviorName;
	}
}