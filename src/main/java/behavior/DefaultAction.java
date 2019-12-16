package behavior;

import model.*;
import strategy.*;

public class DefaultAction {

	private static ParamsBuilder globalParams;
	private static Vec2Double targetPosition;
	
	public DefaultAction(ParamsBuilder globalParams, Vec2Double targetPosition )
	{
		DefaultAction.globalParams = globalParams;
		DefaultAction.targetPosition = targetPosition;
	}
	
	public static boolean jump()
	{
		Game game = globalParams.getGame();
		Unit unit = globalParams.getUnit();
		
		boolean jump = false;
				
		int unitDirection = Helper.getUnitDirectionForY( unit, targetPosition );
		
		
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
	
}
