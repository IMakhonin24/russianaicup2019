package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Defence_healing implements Behavior{

	private final String behaviorName = "Хил";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Defence_healing(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction() {
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestHealthPack().getPosition();
		
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
	    
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
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