package сontroller;

import model.*;
import strategy.*;

public class UnitController {

	public static final int ROLE_MAIN = 1;
	public static final int ROLE_SUPPORT = 2;
	
	private ParamsBuilder globalParams;
	
	public static extUnit myUnit1;
	public static extUnit myUnit2;
	
	public static Unit enemyUnit1;
	public static Unit enemyUnit2;
	
	public UnitController(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		unitInit();
	}
	
	
	private void unitInit() 
	{
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		Unit[] allUnitsOnMap = game.getUnits();	
		for (Unit someUnit : allUnitsOnMap) {
			if (someUnit.getPlayerId() == unit.getPlayerId()) {
				if (myUnit1 == null) {
					myUnit1 = new extUnit(someUnit);
					myUnit1.setRole(ROLE_MAIN);
					continue;
				}
				if (myUnit2 == null) {
					myUnit2 = new extUnit(someUnit);
					myUnit2.setRole(ROLE_SUPPORT);
					continue;
				}
			}else {
				if (enemyUnit1 == null) {
					enemyUnit1 = someUnit;
					continue;
				}
				if (enemyUnit2 == null) {
					enemyUnit2 = someUnit;
					continue;
				}
			}
		}
	}
	
	public boolean checkExistAnyEnemy() 
	{
		return enemyUnit1 != null || enemyUnit2 != null;
	}
	
	public Unit getEnemy1()
	{
		return enemyUnit1;
	}
	
	public Unit getEnemy2()
	{
		return enemyUnit2;
	}
	
	public Unit getAnyEnemy() {
		if( enemyUnit1 != null ) return enemyUnit1;
		if( enemyUnit2 != null ) return enemyUnit2;
		return null;
	}
	
	public int getRole(Unit unit) {
		if( myUnit1.getUnit().getId() == unit.getId() ) {
			return myUnit1.getRole();
		}
		if( myUnit2.getUnit().getId() == unit.getId() ) {
			return myUnit2.getRole();
		}
		return ROLE_MAIN;
	}
	
}
