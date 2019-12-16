package strategy;

import debug.Debug;
import model.*;
import сontroller.*;

public class ParamsBuilder {

	protected Unit unit;
	protected Game game;
	protected Debug debug;
	protected UnitController unitController;
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
	public ParamsBuilder setLootBoxController(LootBoxController lootBoxController) {
		this.lootBoxController = lootBoxController;
		return this;
	}
	
	public ParamsBuilder setGameController(GameController gameController) {
		this.gameController = gameController;
		return this;
	}	
	
	public ParamsBuilder setUnitController(UnitController unitController) {
		this.unitController = unitController;
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

	public LootBoxController getLootBoxController() {
		return lootBoxController;
	}
	
	public GameController getGameController() {
		return gameController;
	}
	
	public UnitController getUnitController() {
		return unitController;
	}
    
}
