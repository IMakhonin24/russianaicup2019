package behavior.other;

import behavior.*;
import debug.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Other_findAnyWeapon implements Behavior
{
	private final String behaviorName = "Поиск любого оружия";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Other_findAnyWeapon(ParamsBuilder globalParams) 
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}
	
	public UnitAction buildAction() 
	{
		initTarget();
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    boolean jumpDown = !jump;
	    
	    action.setVelocity(velocity);
	    action.setJump(jump);
	    action.setJumpDown(jumpDown);
	    action.setAim(targetPosition);
	    action.setShoot(false);
	    action.setReload(false);
	    action.setSwapWeapon(false);
	    action.setPlantMine(false);
	    
		return action;
	}

	private void initTarget() {
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestWeapon().getPosition();
		Helper.debugDrawTarget(globalParams, targetPosition);
	}
	
	public String getBehaviorName() {
		return behaviorName;
	}
}