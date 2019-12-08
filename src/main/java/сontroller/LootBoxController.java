package сontroller;

import model.Game;
import model.Item;
import model.LootBox;
import model.Unit;
import strategy.Helper;
import strategy.ParamsBuilder;

public class LootBoxController {

	protected ParamsBuilder globalParams;
	
	protected LootBox nearestHealthPack = null;
	protected LootBox nearestWeapon = null;
	protected LootBox nearestMine = null;
	
	public LootBoxController(ParamsBuilder globalParams) {
		this.globalParams = globalParams;
		this.findLootBoxes();
	}
	
	private void findLootBoxes() {
		Game game = globalParams.getGame();
		Unit myUnit = globalParams.getUnit();
		
		for (LootBox lootBox : game.getLootBoxes()) {			
			double distanceToTargetLoot = Helper.distanceSqr(myUnit.getPosition(), lootBox.getPosition());
			if (lootBox.getItem() instanceof Item.HealthPack) {
				if (nearestHealthPack == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestHealthPack.getPosition())) {
					nearestHealthPack = lootBox;
		        }
			} else if (lootBox.getItem() instanceof Item.Weapon) {
				if (nearestWeapon == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestWeapon.getPosition())) {
					nearestWeapon = lootBox;
		        }
			} else if (lootBox.getItem() instanceof Item.Mine) {
				if (nearestMine == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestMine.getPosition())) {
					nearestMine = lootBox;
		        }
			}
		}
	}
	
	public LootBox getNearestHealthPack() {
		return nearestHealthPack;
	}

	public LootBox getNearestWeapon() {
		return nearestWeapon;
	}

	public LootBox getNearestMine() {
		return nearestMine;
	}
}