package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Defence_healing implements Behavior{

	private final String behaviorName = "Хил";
	
	private Vec2Double targetPosition;

	private ParamsBuilder globalParams;
	
	public Defence_healing(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.setTarget();
	}	
	
	public void setTarget() {
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		this.targetPosition = lootBoxController.getNearestHealthPack().getPosition();
	}
	
	public double getVelocity() {
		return strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
	}

	public Vec2Double getAim() {
		return Helper.getDefaultAim(globalParams);
	}

	public boolean getJump() {
		return strategy.Helper.getDefaultJump(globalParams, targetPosition);
	}

	public boolean getJumpDown() {
		return !strategy.Helper.getDefaultJump(globalParams, targetPosition);
	}

	public boolean getShoot() {
		return strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	}

	public boolean getSwapWeapon() {
		return false;
	}
	
	public boolean getReload() {
		return strategy.Helper.getDefaultReload(globalParams, targetPosition);
	}

	public boolean getPlantMine() {
		return false;
	}
	
	public String getBehaviorName() {
		return behaviorName;
	}
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}

}