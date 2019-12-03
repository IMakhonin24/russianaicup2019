import model.*;
import model.CustomData.Log;

public class MyStrategy {
	
	protected Game game;
	protected Unit unit;
	protected Debug debug;
	protected Unit targetUnit = null;
	protected LootBox targetLootBox = null;
	protected Vec2Double targetPosition;
	protected Vec2Double aim;
	protected boolean jump;
	
	static double distanceSqr(Vec2Double a, Vec2Double b) {
		return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY());
	}

	public void findEnemy(){
		Unit targetUnit = null;
		
		Game game = getGame();
		Unit[] allUnitsOnMap = game.getUnits();
		
		for (Unit someUnit : allUnitsOnMap) {
			if (someUnit.getPlayerId() == unit.getPlayerId()) break;
			if (targetUnit == null || distanceSqr(unit.getPosition(), someUnit.getPosition()) < distanceSqr(unit.getPosition(), targetUnit.getPosition())) {
				targetUnit = someUnit;
	        }
		}
		
		this.targetUnit = targetUnit;
	}
	
	public void findLootBox() {
		LootBox targetLootBox = null;
		
		Game game = getGame();
		LootBox[] allLootOnMap = game.getLootBoxes();
		
	    for (LootBox lootBox : allLootOnMap) {
	    	
	    	
	    	Item item = lootBox.getItem();
	    	this.debug.draw(new CustomData.Log("loot box = " + item.getClass()  ));
	    	
	      if (lootBox.getItem() instanceof Item.Weapon) {
	        if (targetLootBox == null || distanceSqr(unit.getPosition(),
	            lootBox.getPosition()) < distanceSqr(unit.getPosition(), targetLootBox.getPosition())) {
	        	targetLootBox = lootBox;
	        }
	      }
	    }
	    
	    this.targetLootBox = targetLootBox;
	}
	
	public void findTargetPosition() {

		Unit unit = getUnit();

		Vec2Double targetPosition = unit.getPosition();
		
	    if (unit.getWeapon() == null && this.targetLootBox != null) {
	    	targetPosition = this.targetLootBox.getPosition();
	    } else if (this.targetUnit != null) {
	    	targetPosition = this.targetUnit.getPosition();
	    }
	    
	    this.targetPosition = targetPosition;
	}
	
	public void setAim() {
		Vec2Double aim;
	    if (this.targetUnit != null) {
	    	double aimX = this.targetUnit.getPosition().getX() - unit.getPosition().getX();
	    	double aimY = this.targetUnit.getPosition().getX() - unit.getPosition().getX();
	    	aim = new Vec2Double(aimX,aimY);
	    }else {
	    	aim = new Vec2Double(0, 0);
	    }
	    this.aim = aim;
	}
	
	public void setJump() {
		boolean jump = false;
		
	    if (this.targetPosition.getX() > unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() + 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
	    if (this.targetPosition.getX() < unit.getPosition().getX() && game.getLevel()
	        .getTiles()[(int) (unit.getPosition().getX() - 1)][(int) (unit.getPosition().getY())] == Tile.WALL) {
	      jump = true;
	    }
		
	    this.jump = jump;
	}
	
	public UnitAction getAction(Unit unit, Game game, Debug debug) {
		
		this.setUnit(unit);
		this.setGame(game);
		this.debug = debug;
		
		this.findEnemy();
		
//		LootBoxMap lootBoxMap = new LootBoxMap();
		
		this.findLootBox();
		
		this.findTargetPosition();
		this.setAim();
		this.setJump();
    
		
		
		
	    UnitAction action = new UnitAction();
	    
	    action.setVelocity(this.targetPosition.getX() - unit.getPosition().getX());

	    action.setJump(this.jump);
	    action.setJumpDown(!this.jump);
	    action.setAim(this.aim);
	    action.setShoot(true);
	    action.setSwapWeapon(false);
	    action.setPlantMine(false);
	    
	    return action;
  }	
  
	public void setGame(Game game) {
		this.game = game;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public Game getGame() {
		return this.game;	
	}

	public Unit getUnit() {
		return this.unit;
	}

  
}