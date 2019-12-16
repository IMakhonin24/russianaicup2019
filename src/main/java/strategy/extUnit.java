package strategy;

import model.Unit;

public class extUnit
{

	private int role;
	private Unit unit;
	
	public extUnit(Unit unit) 
	{
		this.unit = unit;
	}
	
	public void setRole(int role) 
	{
		this.role = role;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public int getRole() {
		return role;
	}
}