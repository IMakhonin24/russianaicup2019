package behavior.other;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Other_findAnyWeapon implements Behavior{

	private final String behaviorName = "Поиск любого оружия";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Other_findAnyWeapon(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		action = new UnitAction();
	}
	
	public UnitAction buildAction() {
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestWeapon().getPosition();
		
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = false;
	    boolean reload = false;
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

	public String getBehaviorName() {
		return behaviorName;
	}
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}
	
}