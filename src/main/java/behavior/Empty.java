package behavior;

import model.*;
import strategy.*;

public class Empty implements Behavior{

	private final String behaviorName = "Ничего неделает";
	
	private Vec2Double targetPosition;
	private ParamsBuilder globalParams;
	
	
	public Empty(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.setTarget();
	}	
	
	public void setTarget() {
		Unit myUnit = globalParams.getUnit();
		this.targetPosition = myUnit.getPosition();
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