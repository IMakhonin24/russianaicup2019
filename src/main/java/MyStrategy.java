import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import behavior.*;
import behavior.attack.*;
import behavior.defence.*;
import behavior.other.*;
import debug.Debug;
import model.*;
import strategy.*;
import сontroller.*;

public class MyStrategy {
	
	
	public UnitAction getAction(Unit unit, Game game, Debug debug) {
		
		//Оси координат
		debug.draw(new CustomData.Line(new Vec2Float((float) 0, (float) 0),new Vec2Float((float) 40, (float) 0),0.1f, Color.ORANGE ) );
		debug.draw(new CustomData.Line(new Vec2Float((float) 0, (float) 0),new Vec2Float((float) 0, (float) 40),0.1f, Color.ORANGE ) );

		
		//==============================Start======================
		
		ParamsBuilder globalParams = new ParamsBuilder(unit, game, debug);
		
		//---------------------GAME-----------------------
		GameController gameController = new GameController(globalParams);
		globalParams.setGameController(gameController);
		//---------------------GAME-----------------------
		
		//---------------------ENEMY-----------------------
		EnemyController enemyController = new EnemyController(globalParams);
		Unit nearestEnemy = enemyController.getNearestEnemy();	
		globalParams.setEnemyController(enemyController);
		//---------------------ENEMY-----------------------		
			
		//---------------------LOOT-----------------------
		LootBoxController lootBoxController = new LootBoxController(globalParams);
		globalParams.setLootBoxController(lootBoxController);
		
		LootBox nearestHealthPack = lootBoxController.getNearestHealthPack();
		LootBox nearestWeapon = lootBoxController.getNearestWeapon();
		
		//---------------------LOOT-----------------------
					
		Behavior behavior = new Empty(globalParams);
		if( nearestEnemy != null ) {
			if( (unit.getWeapon() != null) && (unit.getWeapon().getTyp() == WeaponType.PISTOL) ) {
				behavior = new Attack_pistol(globalParams);			
			}else {
				behavior = new Attack_default(globalParams);			
			}
		}
		if( nearestHealthPack != null && unit.getHealth() <= Helper.criticalHeath ) {
			behavior = new Defence_healing(globalParams);
		}	
		if ( (unit.getWeapon() != null) && (unit.getWeapon().getTyp() != WeaponType.PISTOL) && (lootBoxController.getNearestPistol() != null) ) {
			behavior = new Other_findPistol(globalParams);
		}
		if ( nearestWeapon != null && unit.getWeapon() == null ) {
			behavior = new Other_findAnyWeapon(globalParams);
		}
		if (Helper.checkMine(globalParams)) {
			behavior = new Defence_leaveFromMine(globalParams);
		}
		
		
		
				
//		//Aim vector
//		debug.draw(new CustomData.Line(
//			      new Vec2Float((float) unit.getPosition().getX(), (float) unit.getPosition().getY()),
//			      new Vec2Float((float) (aim.getX()+unit.getPosition().getX()), (float) (aim.getY()+unit.getPosition().getY())),
//			      0.05f,
//			      new ColorFloat(0.255f, 0.255f, 0.0f, 1f) ) );
		
		
		
		
		
		//==============test===========
		
		
		
		debug.draw(new CustomData.Log("Current behavior: " + behavior.getBehaviorName()));
		UnitAction action = new BuildAction( behavior ).build();		
		return action;
  }
	
	

	
	
}