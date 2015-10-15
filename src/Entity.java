import java.awt.Graphics;

public abstract class Entity {
	
	public abstract void update();
	
	public abstract void paint(Graphics g);
	
	public abstract Action getAction();
	
	public abstract Inventory getInventory();
	
	public abstract EntityHandler getEntityHandler();
	
	public abstract int getX();
	
	public abstract int getY();
	
	public abstract int getXOnScreen();
	
	public abstract int getYOnScreen();
	
	public abstract void changeX(int dx);
	
	public abstract void changeY(int dy);
	
	public abstract Location getLocation();
	
	public abstract void kill();
	
	public abstract boolean shouldKill();
}