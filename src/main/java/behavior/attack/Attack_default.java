package behavior.attack;

import behavior.*;
import model.*;
import strategy.*;

public class Attack_default implements Behavior{

	private String behaviorName = "Стандартная атака";

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
		Unit unit = globalParams.getUnit();
		double velocity = 0;
		
		double deltaX = Math.abs(targetPosition.getX() - unit.getPosition().getX());
		
		double distanceMax = 15;
		double distanceMin = 10;

		if(unit.getWeapon().getTyp().equals(WeaponType.PISTOL)) {
			this.behaviorName = "Нападение с пистолетом";
			distanceMax = 20;
			distanceMin = 15;
		} else if(unit.getWeapon().getTyp().equals(WeaponType.ASSAULT_RIFLE)){
			this.behaviorName = "Нападение с винтовкой";
			distanceMax = 12;
			distanceMin = 5;
		} else if(unit.getWeapon().getTyp().equals(WeaponType.ROCKET_LAUNCHER)) {
			this.behaviorName = "Нападение с базукой";
			distanceMax = 20;
			distanceMin = 15;
		}
		
		if(deltaX > distanceMax) {
			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		} else if ( deltaX < distanceMin ) {
			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
			velocity = velocity * -1;
		}

		int bulleMissStrategy = Helper.analizeBulletsMiss(globalParams);
		
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
		jump = false;
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
		
		Unit unit = globalParams.getUnit();
		
		if(unit.getMines() > 0) {
			plantMine = true;
		}
		
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