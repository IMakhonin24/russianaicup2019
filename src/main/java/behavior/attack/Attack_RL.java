package behavior.attack;

import behavior.*;
import debug.*;
import model.*;
import strategy.*;
import сontroller.UnitController;

public class Attack_RL implements Behavior
{
	private String behaviorName = "Атака с RL";

	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	private double velocity;
	
	public Attack_RL(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		Game game = globalParams.getGame();
		
		initTarget();
		
		velocity = getVelocity();
		action.setVelocity(velocity);
	
		boolean jump = getJump();
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

		double distanceMax = 30;
		double distanceMin = 15;
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
	
	private boolean getJump() 
	{
		boolean jump = false;
		Unit unit = globalParams.getUnit();
		Game game = globalParams.getGame();
		int unitDirection = Helper.getUnitDirectionForY( unit, targetPosition );
		
		if (unitDirection == Helper.DIRECTION_TOP) {
			jump = true;
		}
		
		
		if (targetPosition.getX() > unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() + 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
	    if (targetPosition.getX() < unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() - 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
		    
//		if(Helper.checkTileForUnitByStepN(game, Tile.WALL, unit, targetPosition, 1)) {
//			jump = true;
//		}else if(velocity == 0) {
//			jump = false;
//		}

	    return jump;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
}	