import model.Game;
import model.Item;
import model.LootBox;

public class LootBoxMap {
	
	public LootBoxMap(Game game) {
		this.game = game;
		
//		this.findLootBoxes();
	}


	protected Game game;
	
	protected LootBox[] mapHealthPack;
	protected LootBox[] mapWeapon;
	protected LootBox[] mapMine;
	
	
	public void findLootBoxes() {
		
//		LootBox[] allLootBoxesOnMap = game.getLootBoxes();
//		
//		for (LootBox lootBox : allLootBoxesOnMap) {
//			
//			Item item = lootBox.getItem();
//			
//			
//			
////			if ( item instanceof Item.Weapon) {
////				LootBox
////			}
//			
//		      if (lootBox.getItem() instanceof Item.Weapon) {
//		        if (targetLootBox == null || distanceSqr(unit.getPosition(),
//		            lootBox.getPosition()) < distanceSqr(unit.getPosition(), targetLootBox.getPosition())) {
//		        	targetLootBox = lootBox;
//		        }
//		      }
//	    }
		
	}
	
	
//	public void findLootBox() {
//		LootBox targetLootBox = null;
//		
//		Game game = getGame();
//		LootBox[] allLootOnMap = game.getLootBoxes();
//		
////		todo Придумать логику
//	    for (LootBox lootBox : allLootOnMap) {
//	      if (lootBox.getItem() instanceof Item.Weapon) {
//	        if (targetLootBox == null || distanceSqr(unit.getPosition(),
//	            lootBox.getPosition()) < distanceSqr(unit.getPosition(), targetLootBox.getPosition())) {
//	        	targetLootBox = lootBox;
//	        }
//	      }
//	    }
//	    
//	    this.targetLootBox = targetLootBox;
//	}
	
}



