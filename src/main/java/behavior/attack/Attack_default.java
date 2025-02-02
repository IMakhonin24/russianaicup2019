package behavior.attack;

import behavior.*;
import debug.*;
import model.*;
import strategy.*;
import сontroller.LootBoxController;
import сontroller.UnitController;

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
		Debug debug = globalParams.getDebug();
		
		initTarget();
		
		double velocity = getVelocity();
		action.setVelocity(velocity);
	
		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
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
	
	private void initTarget() 
	{	
		UnitController unitController = globalParams.getUnitController();
		targetPosition = unitController.getAnyEnemy().getPosition();
		Helper.debugDrawTarget(globalParams, targetPosition);
	}
	
	private double getVelocity()
	{
		Unit unit = globalParams.getUnit();
		
		double velocity = 0;
		
		double deltaX = Math.abs(targetPosition.getX() - unit.getPosition().getX());
		
		double distanceMax = 15;
		double distanceMin = 10;
		if( !Helper.isUnitVision(globalParams, globalParams.getUnitController().getAnyEnemy()) ) {
			double distanceDelta = Math.abs(distanceMax-deltaX);
			distanceMax = distanceMax - distanceDelta - 1;
		}
		
		if(deltaX > distanceMax) {
			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		} else if ( deltaX < distanceMin ) {
			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
			velocity = velocity * -1;
		}

		return velocity;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
}	