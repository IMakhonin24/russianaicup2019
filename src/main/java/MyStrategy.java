import behavior.*;
import behavior.attack.*;
import behavior.defence.*;
import behavior.other.*;
import model.*;
import strategy.*;
import сontroller.*;

public class MyStrategy {
	
	
	public UnitAction getAction(Unit unit, Game game, Debug debug) {
				
		
		ParamsBuilder globalParams = new ParamsBuilder(unit, game);
		
		//---------------------ENEMY-----------------------
		EnemyController enemyController = new EnemyController(globalParams);
		Unit nearestEnemy = enemyController.getNearestEnemy();	
		globalParams.setEnemyController(enemyController);
		//---------------------ENEMY-----------------------		
		
		if(unit.getWeapon() != null) {
			int magazine = unit.getWeapon().getMagazine();
			debug.draw(new CustomData.Log("magazine: " + magazine));
		}
		
		
		
  
		//---------------------LOOT-----------------------
		LootBoxController lootBoxController = new LootBoxController(globalParams);
		globalParams.setLootBoxController(lootBoxController);
		
		LootBox nearestHealthPack = lootBoxController.getNearestHealthPack();
		LootBox nearestWeapon = lootBoxController.getNearestWeapon();
		//---------------------LOOT-----------------------
					
		Behavior behavior = new Empty(globalParams);
		if( nearestEnemy != null ) {
			behavior = new Attack_default(globalParams);
		}
	
		if( nearestHealthPack != null && unit.getHealth() <= Helper.criticalHeath ) {
			behavior = new Defence_healing(globalParams);
		}
		if ( nearestWeapon != null && unit.getWeapon() == null ) {
			behavior = new Other_findAnyWeapon(globalParams);
		}
		debug.draw(new CustomData.Log("Current behavior: " + behavior.getBehaviorName()));
		
		Vec2Double targetP = behavior.getTargetPosition();
		Vec2Double aim = behavior.getAim();
		
		//Target vector
		debug.draw(new CustomData.Line(
	      new Vec2Float((float) unit.getPosition().getX(), (float) unit.getPosition().getY()),
	      new Vec2Float((float) targetP.getX(), (float) targetP.getY()),
	      0.1f,
	      new ColorFloat(0.255f, 0.0f, 0.0f, 1f) ) );
		
		//Aim vector
		debug.draw(new CustomData.Line(
			      new Vec2Float((float) unit.getPosition().getX(), (float) unit.getPosition().getY()),
			      new Vec2Float((float) (aim.getX()+unit.getPosition().getX()), (float) (aim.getY()+unit.getPosition().getY())),
			      0.1f,
			      new ColorFloat(0.255f, 0.255f, 0.0f, 1f) ) );
		
		Bullet[] arBullets = game.getBullets();
		
		for (Bullet someBullet : arBullets) {
			if (someBullet.getPlayerId() == unit.getPlayerId()) continue;
			
			
			Vec2Double bulletVelocity = someBullet.getVelocity();
			
			double x = game.getProperties().getUnitSize().getX();
			double y = game.getProperties().getUnitSize().getY();			
			
			debug.draw(new CustomData.Line(
				      new Vec2Float((float) nearestEnemy.getPosition().getX(), (float) (nearestEnemy.getPosition().getY()+(y/2))),
				      new Vec2Float((float) (bulletVelocity.getX()+nearestEnemy.getPosition().getX()), (float) (bulletVelocity.getY()+	(nearestEnemy.getPosition().getY()+(y/2)))),
				      0.1f,
				      new ColorFloat(0.255f, 0.255f, 0.0f, 0.06f) ) );
			
		}
		
		
		UnitAction action = new BuildAction( behavior ).build();
		return action;
  }
 
}