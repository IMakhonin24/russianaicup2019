
package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;

public class Defence_leaveFromMine implements Behavior
{
	private final String behaviorName = "Убежать от мины";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Defence_leaveFromMine(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		Mine nearestPlantMine = globalParams.getGameController().getNearestPlantMine();
		if(Helper.getDitectionForX(globalParams, nearestPlantMine.getPosition()) == 3) {
			targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() - 5, nearestPlantMine.getPosition().getY());
		}else {
			targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() + 5, nearestPlantMine.getPosition().getY());
		}
		
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		boolean jump = true;
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
	    boolean swapWeapon = false;
	    boolean plantMine = false;
	    
	    
	    action.setVelocity(velocity);
	    action.setJump(jump);
	    action.setJumpDown(jumpDown);
	    action.setAim(aim);
	    action.setShoot(shoot);
	    action.setReload(reload);
	    action.setSwapWeapon(swapWeapon);
	    action.setPlantMine(plantMine);
	    
		return action;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
}