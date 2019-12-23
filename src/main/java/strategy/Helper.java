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
import сontroller.UnitController;

public class Helper {
	
	//Направления движения unit к target
	public static final int DIRECTION_NONE = 0;
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_TOP = 3;
	public static final int DIRECTION_DOWN = 4;
	
	
	public static final double maxSpeed = 1000;
	
	public static final int lowLevelHeath = 85;
	public static final int criticalHeath = 50;
	
	
	public static final int criticalMagazinePistol = 3;
	public static final int criticalMagazineAssaultRifle = 7;
	public static final int criticalMagazineRockerLauncher = 1;

	public static double distanceSqr(Vec2Double a, Vec2Double b) {
		return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY());
	}	
	
	
	public static Vec2Double getPerfectPosition(ParamsBuilder globalParams, int x1, int x2) {
		
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		UnitController unitController = globalParams.getUnitController();

		Unit nearestEnemy = unitController.getAnyEnemy();
		
		int[] altitude = new int[40];
		
		boolean isRightPos = false;
		
		
		
		if (nearestEnemy.getPosition().getX() - unit.getPosition().getX() < 0) {
		      isRightPos = true;
		    } else {
		      isRightPos = false;
		    }

		
	    int highestX = 7;
	    for (int x = x1; x<x2; x++) {
	      for (int y =1; y<=30; y++) {
	        if (getTile(game, x, y) == Tile.WALL) {
	          altitude[x] = y+1;
	          break;
	        }
	      }
	      if (isRightPos) {
	        if (altitude[highestX] <= altitude[x]) {
	          highestX = x;
	        }
	      }
	    }
	    if (isRightPos) {
	      return new Vec2Double( 40 -highestX , altitude[highestX]);
	    } else {
	      return new Vec2Double(highestX, altitude[highestX]);
	    }
	  }
	
	
	
	public static Tile getTile(Game game, double x, double y) {
	    return game.getLevel().getTiles()[(int) (x)][(int) (y)];
	  }
	
	
	public static void debugDrawTarget(ParamsBuilder globalParams,Vec2Double target) 
	{
		Debug debug = globalParams.getDebug();
		Unit unit = globalParams.getUnit();
		
		debug.draw(new CustomData.Line( Coordinate.toV2F(unit.getPosition(), 0, unit.getSize().getY()/2),Coordinate.toV2F(target),0.05f, Color.TARGET ) );
	};
	
	public static Vec2Double getDefaultAim(ParamsBuilder globalParams) {
		Vec2Double aim = new Vec2Double(0, 0);
		
		Unit unit = globalParams.getUnit();
		
		UnitController unitController = globalParams.getUnitController();
		Unit nearestEnemy = unitController.getAnyEnemy();
		
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
	
	
	//Проверит наличие тайла checkTile для unit через countStep шагов
	public static boolean checkTileForUnitByStepN(Game game, Tile checkTile, Unit unit, Vec2Double target, double countStep) 
	{
		Tile[][] tileMatrix = game.getLevel().getTiles();
		
		double unitPositionX = unit.getPosition().getX();
		double unitPositionY = unit.getPosition().getY();	
		
		int unitDirection = getUnitDirectionForX(unit, target);
		double nextUnitPositionX = ( unitDirection == DIRECTION_RIGHT ) ? unitPositionX + countStep : unitPositionX - countStep; 

		Tile findTile = getTileTypeByXY(tileMatrix, nextUnitPositionX, unitPositionY);
		
		if ( (unitDirection == DIRECTION_RIGHT) && (findTile == checkTile) ) {
			return true;
		}
		
		return false;
	}
	
	
	public static boolean checkWallinNStepByX (ParamsBuilder globalParams, Vec2Double targetPosition, double n) 
	{
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		double unitPositionX = unit.getPosition().getX();
		double unitPositionY = unit.getPosition().getY();
	
		Tile[][] tileMatrix = game.getLevel().getTiles();
		
		int unitDirection = getUnitDirectionForX(unit, targetPosition);
		double nextUnitPositionX = ( unitDirection == DIRECTION_RIGHT ) ? unitPositionX + n : unitPositionX - n; 
		
		Tile tile = getTileTypeByXY(tileMatrix, nextUnitPositionX, unitPositionY);
		
		if ( (unitDirection == DIRECTION_RIGHT) && (tile == Tile.WALL) ) {
			return true;
		}
		return false;
	}
	
	// Можно удалить. Есть аналог
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
	
	
	
	// Можно удалить. Есть аналог
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
	
	//Вернет направление движения unit к target по оси Y
	public static int getUnitDirectionForY(Unit unit, Vec2Double target ) 
	{
		double unitPositionY = unit.getPosition().getY();
		double targetPositionY = target.getY();
		
		if ( unitPositionY < targetPositionY ) {
			return DIRECTION_TOP;
		} else if (targetPositionY < unitPositionY){
			return DIRECTION_DOWN;
		} else {
			return DIRECTION_NONE;
		}
	}
	
	
	//Перезарядка
	public static boolean getDefaultReload(ParamsBuilder globalParams, Vec2Double targetPosition) {
		Unit unit = globalParams.getUnit();
		
		UnitController unitController = globalParams.getUnitController();
		Unit nearestEnemy = unitController.getAnyEnemy();
			
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
		UnitController unitController = globalParams.getUnitController();
		Unit nearestEnemy = unitController.getAnyEnemy();
		
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
		UnitController unitController = globalParams.getUnitController();

		Unit unit = globalParams.getUnit();
		Vec2Double unitPosition = unit.getPosition();
		
		Unit enemyUnit = unitController.getAnyEnemy(); 
		Vec2Double enemyUnitPosition = enemyUnit.getPosition();
		Vec2Double coordinate;
		
		if ( unitPosition.getX() > enemyUnitPosition.getX()) {
			coordinate = new Vec2Double(enemyUnitPosition.getX() + x, enemyUnitPosition.getY());
		}else {
			coordinate = new Vec2Double(enemyUnitPosition.getX() - x, enemyUnitPosition.getY());
		}

		return coordinate;
	
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

	
	
	
	
	
	
	
	
	
	
	
	//Вернет направление движения unit к target по оси X
	public static int getUnitDirectionForX(Unit unit, Vec2Double target ) 
	{		
		return getDirectionByCoordinatesX(unit.getPosition(), target);
	}
		
	//Вернет отношение по горизонтали point1 к point2
	public static int getDirectionByCoordinatesX(Vec2Double point, Vec2Double pointTarget) 
	{
		double pointX = point.getX();
		double pointTargetX = pointTarget.getX();
		
		if ( pointX < pointTargetX ) {
			return DIRECTION_RIGHT;
		} else if (pointTargetX < pointX){
			return DIRECTION_LEFT;
		} else {
			return DIRECTION_NONE;
		}
	}
		
	//Вернет координату пола по Y с параметром по X
	public static double findWallByX(Game game, double x) 
	{
		Tile[][] tileMatrix = game.getLevel().getTiles();
		int tileX = (int) x;
		for (int i = 0; i < tileMatrix[tileX].length; i++) {
			Tile tile = tileMatrix[tileX][i];
			if ( (tile==Tile.WALL) || (tile==Tile.PLATFORM) || (tile==Tile.LADDER) ) {
				return i+1;
			}
		}		
		return 0;
	}
	
	//Вернет tile по указанным координатам
	public static Tile getTileTypeByXY(Tile[][] tile, double x, double y) {	
		return tile[ (int) x ][ (int) y ];
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
	