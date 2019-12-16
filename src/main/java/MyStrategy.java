import behavior.*;
import behavior.attack.*;
import behavior.defence.*;
import behavior.other.*;
import debug.Debug;
import model.*;
import model.CustomData.Log;
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
		
		//---------------------UNIT-----------------------
		UnitController unitController = new UnitController(globalParams);
		globalParams.setUnitController(unitController);
		//---------------------UNIT-----------------------
			
			
		//---------------------LOOT-----------------------
		LootBoxController lootBoxController = new LootBoxController(globalParams);
		globalParams.setLootBoxController(lootBoxController);
		
		LootBox nearestHealthPack = lootBoxController.getNearestHealthPack();
		LootBox nearestWeapon = lootBoxController.getNearestWeapon();
		//---------------------LOOT-----------------------
					
		int unitRole = unitController.getRole(unit);
		
		Behavior behavior = new Empty(globalParams);
		
		if( unitController.checkExistAnyEnemy() ) {
			if ( unit.getWeapon() != null ) {
				switch ( unit.getWeapon().getTyp() ) {
					case PISTOL:
						behavior = new Attack_pistol(globalParams);	
						break;
					case ASSAULT_RIFLE:
						behavior = new Attack_AR(globalParams);	
						break;
					case ROCKET_LAUNCHER:
						behavior = new Attack_RL(globalParams);	
						break;
				default:
					behavior = new Attack_default(globalParams);	
					break;
				}
			}
		}
		
		if( nearestHealthPack != null && unit.getHealth() <= Helper.criticalHeath ) {
			behavior = new Defence_healing(globalParams);
		}	
		
		
		
		Weapon unitWeapon = unit.getWeapon();
		if(unitWeapon != null) {
			if(unitRole == UnitController.ROLE_MAIN ) {
				if( (lootBoxController.getNearestPistol() != null) && (unitWeapon.getTyp() != WeaponType.PISTOL) ) {
					behavior = new Other_findPistol(globalParams);
				}
				if( (lootBoxController.getNearestAR() != null) && (unitWeapon.getTyp() != WeaponType.PISTOL) && (unitWeapon.getTyp() != WeaponType.ASSAULT_RIFLE) ) {
					behavior = new Other_findAR(globalParams);
				}
			}else {
				if( (lootBoxController.getNearestRL() != null) && (unitWeapon.getTyp() != WeaponType.ROCKET_LAUNCHER) ) {
					behavior = new Other_findRL(globalParams);
				}
				if( (lootBoxController.getNearestAR() != null) && (unitWeapon.getTyp() != WeaponType.ROCKET_LAUNCHER) && (unitWeapon.getTyp() != WeaponType.ASSAULT_RIFLE) ) {
					behavior = new Other_findAR(globalParams);
				}
			}
		}
		
	
		if ( nearestWeapon != null && unit.getWeapon() == null ) {
			behavior = new Other_findAnyWeapon(globalParams);
		}
		
		if (Helper.checkMine(globalParams)) {
			behavior = new Defence_leaveFromMine(globalParams);
		}
		
		
		
		
		
		
		
		//==============test===========
		
		
		if(unitRole == UnitController.ROLE_MAIN ) {
			debug.draw(new CustomData.Log("Main. Current behavior: " + behavior.getBehaviorName()));
		}else {
			debug.draw(new CustomData.Log("Supp. Current behavior: " + behavior.getBehaviorName()));
		}
		
		UnitAction action = new BuildAction( behavior ).build();		
		return action;
  }
	
	

	
	
}