import java.awt.Graphics;

public class Properties{
	private Block parent;
	private Location source;
	private Map map;
	private int type, x, y, id;
	private int killCountDown, actionCountDown;
	
	public Properties(Block parent, Map map, Location source){
		this.parent = parent;
		this.map = map;
		this.type = parent.getType();
		this.x = parent.getX();
		this.y = parent.getY();
		this.id = parent.getID();
		if(source == null)
			this.source = new Location(x, y);
		else
			this.source = source;
		this.source = source;
		killCountDown = -1;
		actionCountDown = -1;
	}
	
	public void update(){
		if(killCountDown > 0) killCountDown--;
		if(actionCountDown > 0) actionCountDown--;
		switch(type){
		case BlockData.WATER:
			if(!BlockData.isWater(map.getBlock(source).getType()) && killCountDown == -1){
//				map.setBlock(x, y, BlockData.AIR, null);
				killCountDown = 2;
				break;
			}
		case BlockData.WATER_SPRING:
			if(actionCountDown == 0 && killCountDown == -1){
				map.spreadWater(x, y, new Location(x, y));
				actionCountDown = -1;
			}
			if(actionCountDown == -1)
				actionCountDown = 4;
			break;
		}
		//if bottom-dependent block is not bottomed, then kill it
		if(BlockData.isBottomDependent(type) && !map.isBottomed(new Location(x, y)))
			killCountDown = 0;
	}
	
	public void paint(Graphics g){
		switch(type){
		case BlockData.WATER:
		case BlockData.WATER_SPRING:
			g.drawImage(ImageHandler.getWaterImage(
					map.getWaterAdjacencyCode(x, y), id), x*32, y*32, null);
			break;
		}
	}
	
	public boolean shouldKill(){
		return killCountDown == 0;
	}
}
