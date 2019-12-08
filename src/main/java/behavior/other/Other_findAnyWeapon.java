package behavior.other;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Other_findAnyWeapon implements Behavior{

	private final String behaviorName = "Поиск любого оружия";
	
	private Vec2Double targetPosition;
	private ParamsBuilder globalParams;
	
	public Other_findAnyWeapon(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.setTarget();
	}
	
	public void setTarget() {
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		this.targetPosition = lootBoxController.getNearestWeapon().getPosition();
	}
	
	public double getVelocity() {
		return strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
	}

	public Vec2Double getAim() {
		return strategy.Helper.getDefaultAim(globalParams);
	}

	public boolean getJump() {
		return strategy.Helper.getDefaultJump(globalParams, targetPosition);
	}

	public boolean getJumpDown() {
		return !strategy.Helper.getDefaultJump(globalParams, targetPosition);
	}

	public boolean getShoot() {
		return false;
	}

	public boolean getSwapWeapon() {
		return false;
	}
	
	public boolean getReload() {
		return false;
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