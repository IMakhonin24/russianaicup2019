package behavior.attack;

import behavior.*;
import debug.*;
import model.*;
import strategy.*;

public class Attack_pistol implements Behavior
{
	private String behaviorName = "Атака с пистолетом";

	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Attack_pistol(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		
		targetPosition = Helper.getDistanceForPosition(globalParams, 10.0);
		Vec2Float targetP1 = new Vec2Float((float) unit.getPosition().getX(), (float) unit.getPosition().getY());
		Vec2Float targetP2 = new Vec2Float((float) targetPosition.getX(), (float) targetPosition.getY());
		debug.draw(new CustomData.Line(targetP1,targetP2,0.1f,Color.GREEN ) );
		
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
	
	private double getVelocity()
	{
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		double velocity = 0;
		
		double deltaX = Math.abs(targetPosition.getX() - unit.getPosition().getX());

		double distanceMax = 30;
		double distanceMin = 15;
		if( !Helper.isUnitVision(globalParams, globalParams.getEnemyController().getNearestEnemy()) ) {
			double distanceDelta = Math.abs(distanceMax-deltaX);
			distanceMax = distanceMax - distanceDelta - 1;
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