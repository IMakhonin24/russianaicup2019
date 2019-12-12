package behavior.defence;

import behavior.*;
import model.*;
import strategy.*;
import сontroller.*;

public class Defence_healing implements Behavior
{
	private final String behaviorName = "Хил";
	
	private ParamsBuilder globalParams;
	private UnitAction action;
	private Vec2Double targetPosition;
	
	public Defence_healing(ParamsBuilder globalParams)
	{
		this.globalParams = globalParams;
		action = new UnitAction();
	}	
	
	public UnitAction buildAction()
	{
		
		LootBoxController lootBoxController = globalParams.getLootBoxController();
		targetPosition = lootBoxController.getNearestHealthPack().getPosition();
		
		double velocity = strategy.Helper.getDefaultVelocity(globalParams, targetPosition);

		boolean jump = strategy.Helper.getDefaultJump(globalParams, targetPosition);
	    boolean jumpDown = !jump;
	    Vec2Double aim = strategy.Helper.getDefaultAim(globalParams);
	    boolean shoot = strategy.Helper.getDefaultShoot(globalParams, targetPosition);
	    boolean reload = strategy.Helper.getDefaultReload(globalParams, targetPosition);
	    boolean swapWeapon = false;
	    boolean plantMine = false;
	    
	    
	    MissBullet missBullet = new MissBullet(globalParams);
		int bulletMissStrategy = missBullet.getBulletMissStrategy();	
		
		if(bulletMissStrategy == MissBullet.ACTION_JUMP) {
			jump = true;
		}
	    
	    
	    action.setVelocity(velocity);
	    action.setJump(jump);
	    action.setJumpDown(jumpDown);
	    action.setAim(aim);
	    action.setShoot(shoot);
	    action.setReload(reload);
	    action.setSwapWeapon(swapWeapon);
	    action.setPlantMine(plantMine);
	    
		return action;
	}
	
	public String getBehaviorName()
	{
		return behaviorName;
	}
	
	public Vec2Double getTargetPosition() {
		return targetPosition;
	}

}