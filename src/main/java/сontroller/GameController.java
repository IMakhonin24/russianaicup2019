package сontroller;

import model.*;
import strategy.*;

public class GameController {
	
	protected ParamsBuilder globalParams;
	
	protected Mine nearestPlantMine = null;
	
	public GameController(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.findPlantMines();
	}
	
	public void findPlantMines() {
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		for (Mine mine : game.getMines()) {
	        double saveDistance = mine.getExplosionParams().getRadius() + 1;
			double distanceToTargetMine = Helper.distanceSqr(unit.getPosition(), mine.getPosition());

	        double deltaX = Math.abs(unit.getPosition().getX() - mine.getPosition().getX());
	        double deltaY = Math.abs(unit.getPosition().getY() - mine.getPosition().getY());
	        
	        if (mine.getState()==MineState.TRIGGERED) {
	        	if (deltaX <= saveDistance && deltaY <= saveDistance) {
	        		
	        		if (nearestPlantMine == null || distanceToTargetMine < Helper.distanceSqr(unit.getPosition(), nearestPlantMine.getPosition())) {
	        			nearestPlantMine = mine;
			        }
	        	}
	        }
		}
	}

	public Mine getNearestPlantMine() {
		return nearestPlantMine;
	}
	
}