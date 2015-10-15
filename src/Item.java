import java.awt.Graphics;

public class Item extends Entity{
	private int x, y, type;
	private boolean killMe;
	private EntityHandler ents;

	public static final int DIRT = 0, BEET = 1, BEET_SEED = 2;
	
	public Item(int x, int y, int type, EntityHandler ents){
		this.x = x;
		this.y = y;
		this.type = type;
		this.ents = ents;
		this.killMe = false;
	}
	
	public void update(){
		switch(type){
		case DIRT:
			if(Util.randomIntInclusive(0, 1000) == 0){
				this.kill();
				Refs.map.setBlockType(x, y, BlockData.DIRT);
			}
			break;
		}
	}

	public void paint(Graphics g){
		g.drawImage(ImageHandler.getItemImage(type), getXOnScreen(), getYOnScreen(), null);
	}

	public Action getAction(){
		return null;
	}
	
	public Inventory getInventory(){
		return null;
	}
	
	public EntityHandler getEntityHandler(){
		return ents;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getXOnScreen(){
		return x*32;
	}

	public int getYOnScreen(){
		return y*32;
	}

	public void changeX(int dx){
		x += dx;
	}

	public void changeY(int dy){
		y += dy;
	}
	
	public Location getLocation(){
		return new Location(x, y);
	}
	
	public void kill(){
		killMe = true;
	}

	public boolean shouldKill(){
		return killMe;
	}
	
	public int take(){
		killMe = true;
		return type;
	}
}
