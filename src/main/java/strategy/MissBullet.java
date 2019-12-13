package strategy;

import model.*;
import debug.*;

public class MissBullet {

	final public static int ACTION_EMPTY = 0;
	final public static int ACTION_LEFT = 1;
	final public static int ACTION_RIGHT = 2;
	final public static int ACTION_LEFT_JUMP = 3;
	final public static int ACTION_RIGHT_JUMP = 4;
	final public static  int ACTION_LEFT_DOWN = 5;
	final public static int ACTION_RIGHT_DOWN = 6;
	final public static int ACTION_JUMP = 7;
	final public static int ACTION_DOWN = 8;
	
	final public static int SIDE_LEFT = 1;
	final public static int SIDE_RIGHT = 2;
	
	final static double countNexTick = 15;
	
	protected static ParamsBuilder globalParams;
	protected static UnitSaveWrapper unitSaveWrapper;
	
	protected int bulletMissStrategy;
	
	public MissBullet(ParamsBuilder globalParams) 
	{
		MissBullet.globalParams = globalParams;
		MissBullet.unitSaveWrapper = initUnitSaveWrapper();
		bulletMissStrategy = analizeBulletsMiss();
	}
	
	public int getBulletMissStrategy() {
		return bulletMissStrategy;
	}
	
	private UnitSaveWrapper initUnitSaveWrapper()
	{
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		
		double unitPosX = unit.getPosition().getX();
		double unitPosY = unit.getPosition().getY();
		double unitSizeY = unit.getSize().getY();
		double deviationTop = unitSizeY/5;	
		double deviationDown = unitSizeY/2;	

		Vec2Double pointTop = new Vec2Double(unitPosX, unitPosY-deviationDown);
		Vec2Double pointBottom= new Vec2Double(unitPosX, unitPosY+unitSizeY+deviationTop);

		debug.draw(new CustomData.Line(Coordinate.toV2F(pointBottom),Coordinate.toV2F(pointTop),0.1f,Color.YELLOW ) );

		return new UnitSaveWrapper(unitPosX, unitPosY-deviationDown, unitPosX, unitPosY+unitSizeY+deviationTop );
		
	}
	
	//Логика уворотов от пуль
	public int analizeBulletsMiss()
	{
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		
		Bullet[] arBullets = game.getBullets();
		for (Bullet someBullet : arBullets) {
			if (someBullet.getPlayerId() == unit.getPlayerId()) continue;
			if( !isBulletIntersectionForUnit(someBullet, unit) ) continue;
			if( !isCriticalDistanceBetweenBulletAndUnit(someBullet, unit) ) continue;

			//debug--------------------------------
					double countTickPerSecond = game.getProperties().getTicksPerSecond();
								
					double bulletPositionX = someBullet.getPosition().getX();
					double bulletPositionY = someBullet.getPosition().getY();
					double bulletVelocityX = someBullet.getVelocity().getX();
					double bulletVelocityY = someBullet.getVelocity().getY();
					
					double bulletDeltaVelocityTickX = bulletVelocityX / countTickPerSecond;
			        double bulletDeltaVelocityTickY = bulletVelocityY / countTickPerSecond;
					
			        double bulletNextPositionX = bulletPositionX + (bulletDeltaVelocityTickX * countNexTick);
			        double bulletNextPositionY = bulletPositionY + (bulletDeltaVelocityTickY * countNexTick);
					Vec2Double p1_test = new Vec2Double( bulletPositionX + ( bulletDeltaVelocityTickX * countNexTick ), bulletPositionY + ( bulletDeltaVelocityTickY * countNexTick ) );
					Vec2Double p2_test = new Vec2Double( bulletPositionX + ( bulletDeltaVelocityTickX * countNexTick ), bulletPositionY + ( bulletDeltaVelocityTickY * countNexTick ) +5 );
					
					debug.draw(new CustomData.Line(Coordinate.toV2F(p1_test),Coordinate.toV2F(p2_test),0.1f,Color.YELLOW ) );
			//debug--------------------------------
			
			double angle = getAngleBetweenBulletAndUnit(someBullet, unit);

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
			
//					Надпись Miss|Hit
//			debug.draw(new CustomData.PlacedText(bulletTitle, Coordinate.toV2F(unit.getPosition(), 0, +10), TextAlignment.CENTER, 25f, color));
//					Line Bullet
//			debug.draw(new CustomData.Line(Coordinate.toV2F(someBullet.getPosition()),Coordinate.toV2F(someBullet.getVelocity(),someBullet.getPosition().getX(),someBullet.getPosition().getY()),0.1f,color ) );

			

			if( bulletNextPositionX < unitSaveWrapper.getAx() ) {
				//Пуля слева
				if( (angle >= 0) && (angle < 45) ) {
					return ACTION_RIGHT; //turn right
				} else if ( (angle >= 45) && (angle < 70) ) {
					return ACTION_RIGHT_JUMP;//turn right and jump
				} else if ( (angle >= 70) && (angle < 110) ){
					return ACTION_JUMP;// jump
				} else if ( (angle >= 110) && (angle < 135) ){
					return ACTION_RIGHT_JUMP;//turn right and jump
				} else {
					return ACTION_RIGHT; //turn right
				}
				
			} else {
				//Пуля справа
				if( (angle >= 0) && (angle < 45) ) {
					return ACTION_LEFT; //turn left
				} else if ( (angle >= 45) && (angle < 70) ) {
					return ACTION_LEFT_JUMP;//turn left and jump
				} else if ( (angle >= 70) && (angle < 110) ){
					return ACTION_JUMP;// jump
				} else if ( (angle >= 110) && (angle < 135) ){
					return ACTION_LEFT_JUMP;//turn left and jump
				} else {
					return ACTION_LEFT; //turn left
				}
			}
		}
		
		return ACTION_EMPTY;
	}
	
	
	//Вернет id с какой стороны летит пуля
	public static int getBulletSideForUser( Bullet bullet, Unit unit ) 
	{	
		Vec2Double bulletPosition = bullet.getPosition();
		Vec2Double unitPositionX = unit.getPosition();
		
		if( bulletPosition.getX() < unitPositionX.getX()) {
			return SIDE_LEFT;
		}else {
			return SIDE_RIGHT;
		}
	}
	
	//Проверяет приближена ли пуля на критическое расстояние к unit
	private static boolean isCriticalDistanceBetweenBulletAndUnit( Bullet bullet, Unit unit ) 
	{
		Game game = globalParams.getGame();

		double countTickPerSecond = game.getProperties().getTicksPerSecond();
	
		double bulletPositionX = bullet.getPosition().getX();
		double bulletPositionY = bullet.getPosition().getY();
		
		double bulletVelocityX = bullet.getVelocity().getX();
		double bulletVelocityY = bullet.getVelocity().getY();
		
		double bulletDeltaVelocityTickX = bulletVelocityX / countTickPerSecond;
        double bulletDeltaVelocityTickY = bulletVelocityY / countTickPerSecond;
		
        double bulletNextPositionX = bulletPositionX + (bulletDeltaVelocityTickX * countNexTick);
        double bulletNextPositionY = bulletPositionY + (bulletDeltaVelocityTickY * countNexTick);
        
		if ( getBulletSideForUser(bullet, unit) == SIDE_LEFT ) {
			if( ( bulletNextPositionX >= unitSaveWrapper.getAx() )  && ( (bulletNextPositionY <= unitSaveWrapper.getBy()) && (bulletNextPositionY >= unitSaveWrapper.getAy()) ) ) {
				return true;
			}
		}else {
			if( ( bulletNextPositionX <= unitSaveWrapper.getAx() )  && ( (bulletNextPositionY <= unitSaveWrapper.getBy()) && (bulletNextPositionY >= unitSaveWrapper.getAy()) ) ) {
				return true;
			}
		}
		return false;
	}

	//Вернет градус межуд вектором пули и unit
	private static double getAngleBetweenBulletAndUnit( Bullet bullet, Unit unit )
	{
		Vec2Double pointA = new Vec2Double(unitSaveWrapper.getAx(), unitSaveWrapper.getAy());
		Vec2Double pointB = new Vec2Double(unitSaveWrapper.getBx(), unitSaveWrapper.getBy());	
		Vec2Double pointG = bullet.getPosition();
		Vec2Double pointH = new Vec2Double(bullet.getVelocity().getX() + bullet.getPosition().getX(), bullet.getVelocity().getY() + bullet.getPosition().getY() );
		
		double angle = Helper.getAngle(pointA, pointB, pointG, pointH);
	    return angle;	    
	}

	//Проверит пересечение вектора пули с unit
	private static boolean isBulletIntersectionForUnit(Bullet bullet, Unit unit) 
	{
		Vec2Double pointA = new Vec2Double(unitSaveWrapper.getAx(), unitSaveWrapper.getAy());
		Vec2Double pointB = new Vec2Double(unitSaveWrapper.getBx(), unitSaveWrapper.getBy());	
		Vec2Double pointG = bullet.getPosition();
		Vec2Double pointH = new Vec2Double(bullet.getVelocity().getX() + bullet.getPosition().getX(), bullet.getVelocity().getY() + bullet.getPosition().getY() );
		
		return Helper.isIntersection(pointA, pointB, pointG, pointH);
	}
	

	
	//Заготовка на 4 строны
	//Вернет 0 если промах, или 1,2,3,4 В зависимости от стороны попадания
	private static int isBulletIntersectionForUnit_develop(Bullet bullet, Unit unit) 
	{	
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

	
	//Класс описывающий враппер юнита
	class UnitSaveWrapper
	{
		protected double Ax;
		protected double Ay;
		protected double Bx;
		protected double By;
		
		public UnitSaveWrapper(double Ax, double Ay, double Bx, double By) 
		{
			this.Ax = Ax;
			this.Ay = Ay;
			this.Bx = Bx;
			this.By = By;
		}
		
		public double getAx() 
		{
			return Ax;
		}
		
		public double getAy() 
		{
			return Ay;
		}
		
		public double getBx() 
		{
			return Bx;
		}
		
		public double getBy() 
		{
			return By;
		}
		
	}
	
}
