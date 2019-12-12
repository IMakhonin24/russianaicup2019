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

		shoot = false;
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
	
	
	//Логика уворотов от пуль
	public static int analizeBulletsMiss(ParamsBuilder globalParams) {
		final int EMPTY = 0;
		final int LEFT = 1; final int RIGHT = 2;
		final int LEFT_JUMP = 3; final int RIGHT_JUMP = 4;
		final int LEFT_DOWN = 5; final int RIGHT_DOWN = 6;
		final int JUMP = 7; final int DOWN = 8;
		
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		
		Bullet[] arBullets = game.getBullets();
		for (Bullet someBullet : arBullets) {
			if (someBullet.getPlayerId() == unit.getPlayerId()) continue;
			if( !isBulletIntersectionForUnit_One(someBullet, unit) ) continue;
			
			double angle = angle(someBullet, unit);

			double unitPosX = unit.getPosition().getX();
			double unitPosY = unit.getPosition().getY();
			double unitSizeY = unit.getSize().getY();
			
			Vec2Double pointA = new Vec2Double(unitPosX, unitPosY);
			Vec2Double pointB = new Vec2Double(unitPosX, unitPosY+unitSizeY);	
			Vec2Double pointG = someBullet.getPosition();
			Vec2Double pointH = new Vec2Double(someBullet.getVelocity().getX() + someBullet.getPosition().getX(), someBullet.getVelocity().getY() + someBullet.getPosition().getY() );
			
			ColorFloat color;
			String bulletTitle; 
		
			bulletTitle = "Hit " + (int) angle;
			color = Color.ORANGE;
			
//				Надпись Miss|Hit
			debug.draw(new CustomData.PlacedText(bulletTitle, Coordinate.toV2F(unit.getPosition(), 0, +10), TextAlignment.CENTER, 25f, color));
//				Line Bullet
			debug.draw(new CustomData.Line(Coordinate.toV2F(someBullet.getPosition()),Coordinate.toV2F(someBullet.getVelocity(),someBullet.getPosition().getX(),someBullet.getPosition().getY()),0.1f,color ) );

			
			if( pointG.getX() < unitPosX ) {
				System.out.println("Bullet at left. Angle: "+angle);
				//Пуля слева
				if( (angle >= 0) && (angle < 45) ) {
					return RIGHT; //turn right
				} else if ( (angle >= 45) && (angle < 70) ) {
					return RIGHT_JUMP;//turn right and jump
				} else if ( (angle >= 70) && (angle < 110) ){
					return JUMP;// jump
				} else if ( (angle >= 110) && (angle < 135) ){
					return RIGHT_JUMP;//turn right and jump
				} else {
					return RIGHT; //turn right
				}
				
			} else {
				System.out.println("Bullet at right. Angle: "+angle);

				//Пуля справа
				if( (angle >= 0) && (angle < 45) ) {
					return LEFT; //turn left
				} else if ( (angle >= 45) && (angle < 70) ) {
					return LEFT_JUMP;//turn left and jump
				} else if ( (angle >= 70) && (angle < 110) ){
					return JUMP;// jump
				} else if ( (angle >= 110) && (angle < 135) ){
					return LEFT_JUMP;//turn left and jump
				} else {
					return LEFT; //turn left
				}
			}
		}
		
		return EMPTY;
	}

	//Проверит пересечение пули с unit
	public static boolean isBulletIntersectionForUnit_One(Bullet bullet, Unit unit) {

		double unitPosX = unit.getPosition().getX();
		double unitPosY = unit.getPosition().getY();
		double unitSizeY = unit.getSize().getY();
		
		Vec2Double pointA = new Vec2Double(unitPosX, unitPosY);
		Vec2Double pointB = new Vec2Double(unitPosX, unitPosY+unitSizeY);	
		Vec2Double pointG = bullet.getPosition();
		Vec2Double pointH = new Vec2Double(bullet.getVelocity().getX() + bullet.getPosition().getX(), bullet.getVelocity().getY() + bullet.getPosition().getY() );
		
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pGx = pointG.getX(); double pGy = pointG.getY();
		double pHx = pointH.getX(); double pHy = pointH.getY();
		
		double check1 = (pHx-pGx)*(pAy-pGy)-(pHy-pGy)*(pAx-pGx);		
		double check2 = (pHx-pGx)*(pBy-pGy)-(pHy-pGy)*(pBx-pGx);
		double check3 = (pBx-pAx)*(pGx-pAy)-(pBy-pAy)*(pGx-pAx);
		double check4 = (pBx-pAx)*(pHx-pAy)-(pBy-pAy)*(pHx-pAx);
		
		if ( ((check1*check2 < 0)&&(check3*check4 < 0)) ) {
			return true;
		}
		
		return false;
	}

	
	//Вернет градус 
	public static double angle( Bullet bullet, Unit unit )
	{
		double unitPosX = unit.getPosition().getX();
		double unitPosY = unit.getPosition().getY();
		double unitSizeY = unit.getSize().getY();
		
		Vec2Double pointA = new Vec2Double(unitPosX, unitPosY);
		Vec2Double pointB = new Vec2Double(unitPosX, unitPosY+unitSizeY);	
		Vec2Double pointG = bullet.getPosition();
		Vec2Double pointH = new Vec2Double(bullet.getVelocity().getX() + bullet.getPosition().getX(), bullet.getVelocity().getY() + bullet.getPosition().getY() );
		
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pGx = pointG.getX(); double pGy = pointG.getY();
		double pHx = pointH.getX(); double pHy = pointH.getY();
		
		double a =  Math.atan((pBy-pAy)/(pBx-pAx)) - Math.atan((pHy-pGy)/(pHx-pGx));
	    return a * 180 / Math.PI;
	}

	

	
	
	//Заготовка на 4 строны
	//Вернет 0 если промах, или 1,2,3,4 В зависимости от стороны попадания
	public static int isBulletIntersectionForUnit(Bullet bullet, Unit unit) {
		
		final int MISS = 0;
		final int LEFT = 1;
		final int TOP = 2;
		final int RIGHT = 3;
		final int BOTTOM = 4;
		
		//Прямоугольник user -  ABCD с левого нижнего угла
		//Отрезок пули - GH
				
		double pogr = 0;
		double unitPosX = unit.getPosition().getX();
		double unitPosY = unit.getPosition().getY();
		double unitSizeX = unit.getSize().getX()/2;
		double unitSizeY = unit.getSize().getY();
		
		Vec2Double pointA = new Vec2Double(unitPosX-unitSizeX-pogr, unitPosY-pogr);
		Vec2Double pointB = new Vec2Double(unitPosX-unitSizeX-pogr, unitPosY+unitSizeY+pogr);
		Vec2Double pointC = new Vec2Double(unitPosX+unitSizeX+pogr, unitPosY+unitSizeY+pogr);
		Vec2Double pointD = new Vec2Double(unitPosX+unitSizeX+pogr, unitPosY-pogr);
		
		pointA = new Vec2Double(unitPosX, unitPosY);
		pointB = new Vec2Double(unitPosX, unitPosY+unitSizeY);
		
		Vec2Double pointG = bullet.getPosition();
		Vec2Double pointH = new Vec2Double(bullet.getVelocity().getX() + bullet.getPosition().getX(), bullet.getVelocity().getY() + bullet.getPosition().getY() );
		
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pCx = pointC.getX(); double pCy = pointC.getY();
		double pDx = pointD.getX(); double pDy = pointD.getY();
		
		double pGx = pointG.getX(); double pGy = pointG.getY();
		double pHx = pointH.getX(); double pHy = pointH.getY();
		
		//left or test center line
		double checkLeft1 = (pHx-pGx)*(pAy-pGy)-(pHy-pGy)*(pAx-pGx);		
		double checkLeft2 = (pHx-pGx)*(pBy-pGy)-(pHy-pGy)*(pBx-pGx);
		double checkLeft3 = (pBx-pAx)*(pGx-pAy)-(pBy-pAy)*(pGx-pAx);
		double checkLeft4 = (pBx-pAx)*(pHx-pAy)-(pBy-pAy)*(pHx-pAx);
		
		//top
		double checkTop1 = (pHx-pGx)*(pBy-pGy)-(pHy-pGy)*(pBx-pGx);		
		double checkTop2 = (pHx-pGx)*(pCy-pGy)-(pHy-pGy)*(pCx-pGx);
		double checkTop3 = (pCx-pBx)*(pGx-pBy)-(pCy-pBy)*(pGx-pBx);
		double checkTop4 = (pCx-pBx)*(pHx-pBy)-(pCy-pBy)*(pHx-pBx);
		
		//right
		double checkRight1 = (pHx-pGx)*(pCy-pGy)-(pHy-pGy)*(pCx-pGx);		
		double checkRight2 = (pHx-pGx)*(pDy-pGy)-(pHy-pGy)*(pDx-pGx);
		double checkRight3 = (pDx-pCx)*(pGx-pCy)-(pDy-pCy)*(pGx-pCx);
		double checkRight4 = (pDx-pCx)*(pHx-pCy)-(pDy-pCy)*(pHx-pCx);
		
		//bottom
		double checkBottom1 = (pHx-pGx)*(pDy-pGy)-(pHy-pGy)*(pDx-pGx);		
		double checkBottom2 = (pHx-pGx)*(pAy-pGy)-(pHy-pGy)*(pAx-pGx);
		double checkBottom3 = (pAx-pDx)*(pGx-pDy)-(pAy-pDy)*(pGx-pDx);
		double checkBottom4 = (pAx-pDx)*(pHx-pDy)-(pAy-pDy)*(pHx-pDx);
		
		if ( ((checkLeft1*checkLeft2 < 0)&&(checkLeft3*checkLeft4 < 0)) ) {
			return LEFT;
		}
		if ( ((checkTop1*checkTop2 < 0)&&(checkTop3*checkTop4 < 0)) ) {
			return TOP;
		}
		if ( ((checkRight1*checkRight2 < 0)&&(checkRight3*checkRight4 < 0)) ) {
			return RIGHT;
		}
		if ( ((checkBottom1*checkBottom2 < 0)&&(checkBottom3*checkBottom4 < 0)) ) {
			return BOTTOM;
		}
		return MISS;
	}


		
	//Вернет градус test
	public static double angle_test( Unit enemy, Unit unit)
	{
		double pogr = 0;
		double unitPosX = unit.getPosition().getX();
		double unitPosY = unit.getPosition().getY();
		double unitSizeX = unit.getSize().getX()/2;
		double unitSizeY = unit.getSize().getY();
		
		Vec2Double pointA = new Vec2Double(unitPosX-unitSizeX-pogr, unitPosY-pogr);
		Vec2Double pointB = new Vec2Double(unitPosX-unitSizeX-pogr, unitPosY+unitSizeY+pogr);
		Vec2Double pointC = new Vec2Double(unitPosX+unitSizeX+pogr, unitPosY+unitSizeY+pogr);
		Vec2Double pointD = new Vec2Double(unitPosX+unitSizeX+pogr, unitPosY-pogr);
		Vec2Double pointG = new Vec2Double(enemy.getPosition().getX(), enemy.getPosition().getY() + unitSizeY/2 );
		Vec2Double pointH = new Vec2Double(unitPosX, unitPosY + (unitSizeY/2) );
		
		double pAx = pointA.getX(); double pAy = pointA.getY();
		double pBx = pointB.getX(); double pBy = pointB.getY();
		double pCx = pointC.getX(); double pCy = pointC.getY();
		double pDx = pointD.getX(); double pDy = pointD.getY();
		
		double pGx = pointG.getX(); double pGy = pointG.getY();
		double pHx = pointH.getX(); double pHy = pointH.getY();
				
		double a =  Math.atan((pBy-pAy)/(pBx-pAx)) - Math.atan((pHy-pGy)/(pHx-pGx));
		
	    return a * 180 / Math.PI;
	}
	

}
	