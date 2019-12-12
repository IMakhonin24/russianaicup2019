package behavior.attack;

import behavior.*;
import model.*;
import strategy.*;

public class Attack_default implements Behavior
{
	private String behaviorName = "Стандартная атака";

	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Attack_default(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		Unit unit = globalParams.getUnit();
		
		targetPosition = Helper.getDistanceForPosition(globalParams, 10.0);
		
		double velocity = getVelocity();
		action.setVelocity(velocity);
	
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
		jump = false;
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
	    boolean swapWeapon = false;
	    boolean plantMine = false;
		if(unit.getMines() > 0) {
			plantMine = true;
		}
				
		MissBullet missBullet = new MissBullet(globalParams);
		int bulletMissStrategy = missBullet.getBulletMissStrategy();	
		
		if(bulletMissStrategy == MissBullet.ACTION_JUMP) {
			jump = true;
		}

		action.setJump(jump);
		action.setJumpDown(jumpDown);
		action.setAim(aim);
		action.setShoot(shoot);
	    action.setReload(reload);
		action.setSwapWeapon(swapWeapon);
	    action.setPlantMine(plantMine);

		return action;
	}
	
	private double getVelocity()
	{
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

		
		globalParams.setVelocity(velocity);
		return velocity;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
}