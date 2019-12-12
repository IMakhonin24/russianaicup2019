package strategy;

import debug.Debug;
import model.*;
import сontroller.*;

public class ParamsBuilder {

	protected Unit unit;
	protected Game game;
	protected Debug debug;
	protected EnemyController enemyController;
	protected LootBoxController lootBoxController;
	protected GameController gameController;
	
	private double velocity;
	private boolean jump;
	private boolean jumpDown;
	private Vec2Double aim;
	private boolean shoot;
	private boolean reload;
	private boolean swapWeapon;
    private boolean plantMine;	
    
	public ParamsBuilder(Unit unit, Game game, Debug debug) {
		this.unit = unit;
		this.game = game;
		this.debug = debug;
	}

	//---------------Setter block----------------
	
	public ParamsBuilder setEnemyController(EnemyController enemyController) {
		this.enemyController = enemyController;
		return this;
	}
	
	public ParamsBuilder setLootBoxController(LootBoxController lootBoxController) {
		this.lootBoxController = lootBoxController;
		return this;
	}
	
	public ParamsBuilder setGameController(GameController gameController) {
		this.gameController = gameController;
		return this;
	}
	
	public ParamsBuilder setJump(boolean jump) { 
    	this.jump = jump;
    	return this;
    }
	
	public ParamsBuilder setVelocity(double velocity) { 
		this.velocity = velocity;
		return this;
	}
    
	public ParamsBuilder setAim(model.Vec2Double aim) { 
		this.aim = aim; 
		return this;
	}
    
	public ParamsBuilder setShoot(boolean shoot) { 
		this.shoot = shoot;
		return this;
	}
    
	public ParamsBuilder setReload(boolean reload) { 
		this.reload = reload;
		return this;
	}
    
	public ParamsBuilder setSwapWeapon(boolean swapWeapon) { 
		this.swapWeapon = swapWeapon;
		return this;
	}
    
	public ParamsBuilder setPlantMine(boolean plantMine) { 
		this.plantMine = plantMine;
		return this;
	}
	
	public ParamsBuilder setJumpDown(boolean jumpDown) { 
		this.jumpDown = jumpDown;
		return this;
	}
    
	
	//---------------Getter block----------------
	
	public Unit getUnit() {
		return unit;
	}

	public Game getGame() {
		return game;
	}
	
	public Debug getDebug() {
		return debug;
	}

	public EnemyController getEnemyController() {
		return enemyController;
	}

	public LootBoxController getLootBoxController() {
		return lootBoxController;
	}
	
	public GameController getGameController() {
		return gameController;
	}
	
    
}
