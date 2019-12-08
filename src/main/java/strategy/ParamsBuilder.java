package strategy;

import behavior.*;
import behavior.attack.*;
import behavior.defence.*;
import behavior.other.*;
import model.*;
import strategy.*;
import сontroller.*;

public class ParamsBuilder {

	protected Unit unit;
	protected Game game;
	protected EnemyController enemyController;
	protected LootBoxController lootBoxController;
	
	public ParamsBuilder(Unit unit, Game game) {
		this.unit = unit;
		this.game = game;
	}

	public ParamsBuilder setEnemyController(EnemyController enemyController) {
		this.enemyController = enemyController;
		return this;
	}

	public ParamsBuilder setLootBoxController(LootBoxController lootBoxController) {
		this.lootBoxController = lootBoxController;
		return this;
	}
	
	
	
	public Unit getUnit() {
		return unit;
	}

	public Game getGame() {
		return game;
	}

	public EnemyController getEnemyController() {
		return enemyController;
	}

	public LootBoxController getLootBoxController() {
		return lootBoxController;
	}
	
	
	
}
