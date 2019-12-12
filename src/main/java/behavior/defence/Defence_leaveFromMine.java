
package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;

public class Defence_leaveFromMine implements Behavior{

	private final String behaviorName = "Убежать от мины";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Defence_leaveFromMine(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction() {
		
		Mine nearestPlantMine = globalParams.getGameController().getNearestPlantMine();
		if(Helper.getDitectionForX(globalParams, nearestPlantMine.getPosition()) == 3) {
			targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() - 5, nearestPlantMine.getPosition().getY());
		}else {
			targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() + 5, nearestPlantMine.getPosition().getY());
		}
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		action.setVelocity(velocity);
		
		boolean jump = true;
	    action.setJump(jump);
	    
	    boolean jumpDown = !jump;
	    action.setJumpDown(jumpDown);
	    
	    
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    action.setAim(aim);
	    
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    action.setShoot(shoot);
	    
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);;
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
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}

}