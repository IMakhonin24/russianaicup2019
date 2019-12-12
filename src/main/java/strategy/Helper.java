package strategy;

import debug.Debug;
import model.Bullet;
import model.ColorFloat;
import model.CustomData;
import model.Game;
import model.Mine;
import model.MineState;
import model.TextAlignment;
import model.Tile;
import model.Unit;
import model.Vec2Double;
import model.Vec2Float;
import model.Weapon;
import model.WeaponType;
import сontroller.EnemyController;

public class Helper {
	
	
	public static final double maxSpeed = 1000;
	public static final int criticalHeath = 75;
	
	public static final int criticalMagazinePistol = 3;
	public static final int criticalMagazineAssaultRifle = 7;
	public static final int criticalMagazineRockerLauncher = 1;

	public static double distanceSqr(Vec2Double a, Vec2Double b) {
		return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY());
	}	
	
	public static Vec2Double getDefaultAim(ParamsBuilder globalParams) {
		Vec2Double aim = new Vec2Double(0, 0);
		
		Unit unit = globalParams.getUnit();
		EnemyController enemyController = globalParams.getEnemyController();
		Unit nearestEnemy = enemyController.getNearestEnemy();
		
		if (nearestEnemy != null) {
			aim = new Vec2Double(nearestEnemy.getPosition().getX() - unit.getPosition().getX(), nearestEnemy.getPosition().getY() - unit.getPosition().getY());
	    }
		return aim;
	}
	
	public static boolean getDefaultJump(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		boolean jump = getDitectionForY(globalParams, targetPosition) == 3 ;
		
	    if (targetPosition.getX() > unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() + 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
	    if (targetPosition.getX() < unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() - 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
	    
		return jump;
	}
	
	public static boolean checkMine(ParamsBuilder globalParams) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();

		for (Mine mine : game.getMines()) {
	        double saveDistance = mine.getExplosionParams().getRadius() + 1;
	        
	        double deltaX = Math.abs(unit.getPosition().getX() - mine.getPosition().getX());
	        double deltaY = Math.abs(unit.getPosition().getY() - mine.getPosition().getY());
	        
	        if (mine.getState()==MineState.TRIGGERED) {
	        	if (deltaX <= saveDistance && deltaY <= saveDistance) {
	        		return true;
	        	}
	        }
		}
		return false;
	}
	

	
	
	//Проверить наличие платформы снизу на N шагов
	public static boolean checkPlatforminNStepByBottom (ParamsBuilder globalParams, double n) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		int tileX = (int) unit.getPosition().getX();
		int tileY = (int) (unit.getPosition().getY() - n);
		
		if(game.getLevel().getTiles()[tileX][tileY] == Tile.PLATFORM) {
			return true;
		}
		return false;
	}
	
	//Проверисть стену по оси X на N шагов
	public static boolean checkWallinNStepByX (ParamsBuilder globalParams, Vec2Double targetPosition, double n) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		if ( 
				(getDitectionForX(globalParams, targetPosition) == 3 && game.getLevel().getTiles()[(int) (unit.getPosition().getX() + n)][(int) (unit.getPosition().getY())] == Tile.WALL) || 
				(getDitectionForX(globalParams, targetPosition) == 2 && game.getLevel().getTiles()[(int) (unit.getPosition().getX() - n)][(int) (unit.getPosition().getY())] == Tile.WALL))
		{
			return true;
		}
		return false;
	}
	
	// возвразает направление юнита по оси X
	// 1 - target cлева
	// 2 - target и unit на одной координате
	// 3 - target справа
	public static int getDitectionForX(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Unit unit = globalParams.getUnit();
		if (targetPosition.getX() > unit.getPosition().getX()) {
			return 3;
		} else if (targetPosition.getX() < unit.getPosition().getX()) {
			return 1;
		} else {
			return 2;
		}
	}
	
	// возвразает направление юнита по оси Y
	// 1 - unit двигается вниз
	// 2 - unit достиг target
	// 3 - unit двигается вверх
	public static int getDitectionForY(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Unit unit = globalParams.getUnit();
		if (targetPosition.getY() > unit.getPosition().getY()) {
			return 3;
		} else if (targetPosition.getY() < unit.getPosition().getY()) {
			return 1;
		} else {
			return 2;
		}
	} 
	
	
	//Перезарядка
	public static boolean getDefaultReload(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Unit unit = globalParams.getUnit();
		Unit nearestEnemy = globalParams.getEnemyController().getNearestEnemy();
		
		if(!isUnitVision(globalParams, nearestEnemy)) {
			Weapon weapon = unit.getWeapon();
			if ( weapon != null ) {
				int weaponMagazine = weapon.getMagazine();
				if ( (weapon.getTyp().equals(WeaponType.PISTOL)) && (weaponMagazine < criticalMagazinePistol) ) {
					return true;
				} else if ( (weapon.getTyp().equals(WeaponType.ASSAULT_RIFLE)) && (weaponMagazine < criticalMagazineAssaultRifle) ) {
					return true;
				} else if ( (weapon.getTyp().equals(WeaponType.ROCKET_LAUNCHER)) && (weaponMagazine < criticalMagazineRockerLauncher) ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean getDefaultShoot(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		Unit nearestEnemy = globalParams.getEnemyController().getNearestEnemy();
		
		boolean shoot = false;
		Weapon weapon = unit.getWeapon();
		
		if(isUnitVision(globalParams, nearestEnemy)) {
			shoot = true;
			if(weapon != null) {
				int weaponType = weapon.getTyp().discriminant;
				
				if( weaponType == model.WeaponType.ROCKET_LAUNCHER.discriminant ) {
					
					boolean wall = false;
					for(int i = 1; i < 6; i++) {
						if ( targetPosition.getX() > unit.getPosition().getX() && game.getLevel().getTiles()[(int) (unit.getPosition().getX() + i)][(int) (unit.getPosition().getY())] == Tile.WALL) {
							wall = true;
							break;
					    }
					    if ( targetPosition.getX() < unit.getPosition().getX() && game.getLevel().getTiles()[(int) (unit.getPosition().getX() - i)][(int) (unit.getPosition().getY())] == Tile.WALL) {
					    	wall = true;
							break;
					    }
					}
					
					if (wall == true) {
						shoot = false;
					}
				}
			}
		}

		return shoot;
	}
	
	
	
	public static double getDefaultVelocity(ParamsBuilder globalParams, Vec2Double targetPosition) {
		
		Unit myUnit = globalParams.getUnit();
		
		double velocity = maxSpeed;
		
		if ( targetPosition.getX() < myUnit.getPosition().getX() ) {
			velocity = velocity * -1;
		}
		
		return velocity;
	}
	
	
	public static Vec2Double getDistanceForPosition(ParamsBuilder globalParams, double x ) {
		Unit unit = globalParams.getUnit();
		EnemyController enemyController = globalParams.getEnemyController();
		
		Vec2Double enemyPosition = enemyController.getNearestEnemy().getPosition();
		Vec2Double coorditane;
		
		if (unit.getPosition().getX() > enemyPosition.getX()) {
			coorditane = new Vec2Double(enemyPosition.getX() + x, enemyPosition.getY());
		}else {
			coorditane = new Vec2Double(enemyPosition.getX() - x, enemyPosition.getY());
		}
		
		coorditane = enemyPosition;
//		coordinate = unit.getPosition();

		return coorditane;
	
	}
	
	
	//Проверка видимости unitTarget true - враг виден
	public static boolean isUnitVision(ParamsBuilder globalParams, Unit targetUnit) {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
	    boolean targenVision = true;
	    
	    Vec2Double unitPosition = new Vec2Double(unit.getPosition().getX(), unit.getPosition().getY()+unit.getSize().getY()/2);
	    Vec2Double targetUnitPosition = new Vec2Double(targetUnit.getPosition().getX(), targetUnit.getPosition().getY()+targetUnit.getSize().getY()/2);
	    
	    int countTicksPerSecont = (int) game.getProperties().getTicksPerSecond();
	    
	    double deltaX = (unitPosition.getX()-targetUnitPosition.getX())/countTicksPerSecont;
	    double deltaY = (unitPosition.getY()-targetUnitPosition.getY())/countTicksPerSecont;
	    
	    double positionX = unitPosition.getX();
	    double positionY = unitPosition.getY();
	    
	    for (int i = 0; i < countTicksPerSecont; i++) {
	      positionX -= deltaX;
	      positionY -= deltaY;
	      int tileX = (int) positionX;
	      int tileY = (int) positionY;
	      Tile tile = game.getLevel().getTiles()[tileX][tileY];
	      if (tile == Tile.WALL) {
	    	  targenVision = false;
	    	  break;
	      }
	    }
	    return targenVision;
	  }

	//Проверит пересечение отрезков
	public static boolean isIntersection( Vec2Double pointA, Vec2Double pointB, Vec2Double pointC, Vec2Double pointD ) 
	{	
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pCx = pointC.getX(); double pCy = pointC.getY();
		double pDx = pointD.getX(); double pDy = pointD.getY();
		
		double check1 = (pDx-pCx)*(pAy-pCy)-(pDy-pCy)*(pAx-pCx);		
		double check2 = (pDx-pCx)*(pBy-pCy)-(pDy-pCy)*(pBx-pCx);
		double check3 = (pBx-pAx)*(pCx-pAy)-(pBy-pAy)*(pCx-pAx);
		double check4 = (pBx-pAx)*(pDx-pAy)-(pBy-pAy)*(pDx-pAx);
		
		return ( ((check1*check2 < 0)&&(check3*check4 < 0)) );
	}
		
	//Вернет градус между отрезками AB и CD
	public static double getAngle( Vec2Double pointA, Vec2Double pointB, Vec2Double pointC, Vec2Double pointD )
	{
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pCx = pointC.getX(); double pCy = pointC.getY();
		double pDx = pointD.getX(); double pDy = pointD.getY();
		double angle = Math.atan((pBy-pAy)/(pBx-pAx)) - Math.atan((pDy-pCy)/(pDx-pCx));
		return angle * 180 / Math.PI;
	}
	

}
	