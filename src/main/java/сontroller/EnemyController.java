package сontroller;

import model.*;
import strategy.*;

public class EnemyController {
	
	protected ParamsBuilder globalParams;
	
	protected Unit nearestEnemy;
	
	public EnemyController(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.findEnemies();
	}
	
	public void findEnemies() {
		Game game = globalParams.getGame();
		Unit myUnit = globalParams.getUnit();
		
		Unit[] allUnitsOnMap = game.getUnits();	
		for (Unit someUnit : allUnitsOnMap) {
			if (someUnit.getPlayerId() == myUnit.getPlayerId()) continue;
		
			if (nearestEnemy == null || Helper.distanceSqr(myUnit.getPosition(), someUnit.getPosition()) < Helper.distanceSqr(myUnit.getPosition(), nearestEnemy.getPosition())) {
				nearestEnemy = someUnit;
			}
		}
	}
	
	public Unit getNearestEnemy() {
		return nearestEnemy;
	}
	
}