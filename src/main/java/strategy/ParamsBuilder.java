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
	
	private double velocity;
	private boolean jump;
	private boolean jumpDown;
	private Vec2Double aim;
	private boolean shoot;
	private boolean reload;
	private boolean swapWeapon;
    private boolean plantMine;	
    
	public ParamsBuilder(Unit unit, Game game) {
		this.unit = unit;
		this.game = game;
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

	public EnemyController getEnemyController() {
		return enemyController;
	}

	public LootBoxController getLootBoxController() {
		return lootBoxController;
	}
	
	public double getVelocity() { 
		return velocity; 
	}
	
    public boolean hetJump() { 
    	return jump;
	}
    
    public boolean getJumpDown() { 
    	return jumpDown;
	}

    public Vec2Double getAim() { 
    	return aim;
    }
    
    public boolean getShoot() { 
    	return shoot;
    }
    
    public boolean getReload() {
    	return reload;
    }
    
    public boolean getSwapWeapon() {
    	return swapWeapon;
    }
    
    public boolean getPlantMine() {
    	return plantMine;
    }
    
}
