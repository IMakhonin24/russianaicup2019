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
		action.setVelocity(velocity);
		
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    action.setJump(jump);
	    
	    boolean jumpDown = !jump;
	    action.setJumpDown(jumpDown);
	    
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    action.setAim(aim);
	    
	    boolean shoot = false;
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
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}
	
}