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
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		globalParams.setVelocity(velocity);
		return velocity;
	}

	public Vec2Double getAim() {
		Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
		globalParams.setAim(aim);
		return aim;
	}

	public boolean getJump() {
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
		globalParams.setJump(jump);
		return jump;
	}

	public boolean getJumpDown() {
		boolean jumpDown = !globalParams.getJump();
		globalParams.setJumpDown(jumpDown);
		return jumpDown;
	}

	public boolean getShoot() {
		boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
		globalParams.setShoot(shoot);
		return shoot;
	}

	public boolean getSwapWeapon() {
		boolean swapWeapon = false;
		globalParams.setSwapWeapon(swapWeapon);
		return swapWeapon;
	}
	
	public boolean getReload() {
		boolean reload = false;
		globalParams.setReload(reload);
		return reload;
	}

	public boolean getPlantMine() {
		boolean plantMine = false;
		globalParams.setPlantMine(plantMine);
		return plantMine;
	}
	
	public String getBehaviorName() {
		return behaviorName;
	}
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}

}