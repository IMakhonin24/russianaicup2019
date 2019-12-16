package behavior.defence;

import behavior.*;
import debug.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Defence_setPositionAroundHealthPack implements Behavior
{
	private final String behaviorName = "Занять позицию около аптечки";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Defence_setPositionAroundHealthPack(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		Unit unit = globalParams.getUnit();
		Game game = globalParams.getGame();
		Debug debug = globalParams.getDebug();
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestHealthPack().getPosition();

		debug.draw(new CustomData.Line(Coordinate.toV2F(unit.getPosition()),Coordinate.toV2F(targetPosition),0.1f, Color.ORANGE ) );

		
		double velocity = getVelocity();

		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    shoot = false;
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
	    boolean swapWeapon = false;
	    boolean plantMine = false;
	    
	    
	    MissBullet missBullet = new MissBullet(globalParams);
		int bulletMissStrategy = missBullet.getBulletMissStrategy();	
		
		if(bulletMissStrategy == MissBullet.ACTION_JUMP) {
			jump = true;
		}
	    
	    
	    action.setVelocity(velocity);
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
		

//		double deltaX = Math.ceil(Math.abs( nearestHealthPackPosition.getX() - nearestEnemyPosition.getX() ) / 5);
//		
//		if( Helper.getDirectionByCoordinatesX(nearestHealthPackPosition, nearestEnemyPosition) == Helper.DIRECTION_RIGHT ) {
//			double targetX = nearestHealthPackPosition.getX() + deltaX; fgd
//			targetPosition = new Vec2Double( targetX, Helper.findWallByX(game, targetX) );	
//		}else {
//			double targetX = nearestHealthPackPosition.getX() - deltaX;
//			targetPosition = new Vec2Double( targetX, Helper.findWallByX(game, targetX) );	
//		}
		
		
		
		Unit unit = globalParams.getUnit();
		
		Vec2Double unitPosition = unit.getPosition();
		EnemyController enemyController = globalParams.getEnemyController();
		Vec2Double nearestEnemyPosition = enemyController.getNearestEnemy().getPosition();
		
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		Vec2Double nearestHealthPackPosition = lootBoxController.getNearestHealthPack().getPosition();
				
		double velocity = 0;
		
//		double deltaX = Math.abs(targetPosition.getX() - unit.getPosition().getX());
		double deltaUnitToHealth = Math.abs( unitPosition.getX() - nearestHealthPackPosition.getX() );
		double deltaUnitToEnemy = Math.abs( unitPosition.getX() - nearestEnemyPosition.getX() );


		double distanceMax = 30;
		double distanceMin = 15;
//		if( !Helper.isUnitVision(globalParams, globalParams.getEnemyController().getNearestEnemy()) ) {
//			double distanceDelta = Math.abs(distanceMax-deltaX);
//			distanceMax = distanceMax - distanceDelta - 1;
//		}
		
		int directionHealthPahkToEnemy = Helper.getDirectionByCoordinatesX(nearestHealthPackPosition, nearestEnemyPosition);
		
		if( deltaUnitToHealth/2 < deltaUnitToEnemy ) {
//			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
		} else if ( deltaUnitToHealth/2 < deltaUnitToEnemy ) {
//			velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);
//			velocity = velocity * -1;
		}
	
		return velocity;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
}