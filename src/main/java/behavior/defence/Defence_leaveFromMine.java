
package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Defence_leaveFromMine implements Behavior{

	private final String behaviorName = "Убежать от мины";
	
	private Vec2Double targetPosition;

	private ParamsBuilder globalParams;
	
	public Defence_leaveFromMine(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.setTarget();
	}	
	
	public void setTarget() {
		Mine nearestPlantMine = globalParams.getGameController().getNearestPlantMine();
		if(Helper.getDitectionForX(globalParams, nearestPlantMine.getPosition()) == 3) {
			this.targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() - 5, nearestPlantMine.getPosition().getY());
		}else {
			this.targetPosition = new Vec2Double(nearestPlantMine.getPosition().getX() + 5, nearestPlantMine.getPosition().getY());
		}
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
		boolean jump = true;
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
		boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
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