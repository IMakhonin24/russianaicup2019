package behavior.attack;

import behavior.*;
import model.*;
import strategy.*;

public class Attack_default implements Behavior{

	private final String behaviorName = "Стандартная атака";

	private Vec2Double targetPosition;
	private ParamsBuilder globalParams;
	
	public Attack_default(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.setTarget();
	}	
	
	public void setTarget() {
		this.targetPosition = Helper.getDistanceForPosition(globalParams, 10.0);
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