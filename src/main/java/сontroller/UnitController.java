package сontroller;

import debug.Debug;
import model.*;
import strategy.*;

public class UnitController {

	public static final int ROLE_MAIN = 1;
	public static final int ROLE_SUPPORT = 2;
	
	private ParamsBuilder globalParams;
	
	private extUnit myUnit1;
	private extUnit myUnit2;
	
	private Unit enemyUnit1 = null;
	private Unit enemyUnit2 = null;
	
	public UnitController(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		unitInit();
	}
	
	
	private void unitInit() 
	{
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		Debug debug = globalParams.getDebug();
		
		Unit[] allUnitsOnMap = game.getUnits();	
		for (Unit someUnit : allUnitsOnMap) {
			if (someUnit.getPlayerId() == unit.getPlayerId()) {
				if (this.myUnit1 == null) {
					this.myUnit1 = new extUnit(someUnit);
					this.myUnit1.setRole(ROLE_MAIN);
					continue;
				}
				if (this.myUnit2 == null) {
					this.myUnit2 = new extUnit(someUnit);
					this.myUnit2.setRole(ROLE_SUPPORT);
					continue;
				}
			}else {
				if (this.enemyUnit1 == null) {
					this.enemyUnit1 = someUnit;
					continue;
				}
				if (this.enemyUnit2 == null) {
					this.enemyUnit2 = someUnit;
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
