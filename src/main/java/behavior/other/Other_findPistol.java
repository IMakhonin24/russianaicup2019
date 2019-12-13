package behavior.other;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Other_findPistol implements Behavior{

	private final String behaviorName = "Поиск пистолета";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Other_findPistol(ParamsBuilder globalParams) 
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}
	
	public UnitAction buildAction() 
	{
		Unit unit = globalParams.getUnit();
	
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestPistol().getPosition();
		
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
	    boolean swapWeapon = false;
	    if( (Math.abs(targetPosition.getX() - unit.getPosition().getX()) < 2) && (Math.abs(targetPosition.getY() - unit.getPosition().getY()) < 2) ) {
	    	swapWeapon = true;
	    }
	    
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

	public String getBehaviorName() {
		return behaviorName;
	}
}