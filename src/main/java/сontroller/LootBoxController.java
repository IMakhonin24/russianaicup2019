package сontroller;

import model.Game;
import model.Item;
import model.LootBox;
import model.Unit;
import model.WeaponType;
import strategy.Helper;
import strategy.ParamsBuilder;

public class LootBoxController {

	protected ParamsBuilder globalParams;
	
	protected LootBox nearestHealthPack = null;
	protected LootBox nearestWeapon = null;
	protected LootBox nearestPistol = null;
	protected LootBox nearestAssaultRifle = null;
	protected LootBox nearestRocketLauncher = null;
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
				Item.Weapon weapon = (Item.Weapon) lootBox.getItem();
				switch (weapon.getWeaponType()) {
				   case  PISTOL:
					   if (nearestPistol == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestPistol.getPosition())) {
						   nearestPistol = lootBox;
					   }
					   break;
				   case ASSAULT_RIFLE:
					   if (nearestAssaultRifle == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestAssaultRifle.getPosition())) {
						   nearestAssaultRifle = lootBox;
					   }
				       break;
				   case ROCKET_LAUNCHER:
					   if (nearestRocketLauncher == null || distanceToTargetLoot < Helper.distanceSqr(myUnit.getPosition(), nearestRocketLauncher.getPosition())) {
						   nearestRocketLauncher = lootBox;
					   }
				       break;
				}
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