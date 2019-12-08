package behavior;

import model.Vec2Double;

public interface Behavior {
	public void setTarget();
	public double getVelocity();
	public Vec2Double getAim();
	public boolean getJump();
	public boolean getJumpDown();
	public boolean getShoot();
	public boolean getReload();
	public boolean getSwapWeapon();
	public boolean getPlantMine();
	public String getBehaviorName();
	public Vec2Double getTargetPosition();
}
