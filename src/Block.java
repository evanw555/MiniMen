import java.awt.Graphics;

public class Block{
	private int x, y, type, id, edgeID, waterAge;
	private Map map;
	private Properties props;
	
	public Block(int x, int y, int type, Map map, Location source){
		this.x = x;
		this.y = y;
		this.type = type;
		this.map = map;
		this.id = Util.randomIntInclusive(0, BlockData.NUM_IDS-1);
		this.edgeID = Util.randomIntInclusive(0, BlockData.NUM_IDS-1);
		if(BlockData.hasProperties(type))
			props = new Properties(this, map, source);
	}
	
	public void paint(Graphics g){
		g.drawImage(ImageHandler.getBlockImage(type, id), x*32, y*32, null);
	}
	
	public void paintEdge(Graphics g){
		g.drawImage(ImageHandler.getEdgeImage(
				map.getAdjacencyCode(x, y), edgeID), x*32, y*32, null);
	}
	
	public void paintProperties(Graphics g){
		if(props != null) props.paint(g);
	}
	
	public void update(){
		if(props != null) props.update();
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getID(){
		return id;
	}
	
	public int getEdgeID(){
		return edgeID;
	}
	
	public void setEdgeID(int edgeID){
		this.edgeID = edgeID;
	}
	
	public boolean shouldKill(){
		if(props == null) return false;
		else return props.shouldKill();
	}
	
	public void resetID(){
		id = Util.randomIntInclusive(0, BlockData.NUM_IDS-1);
	}
	
	public boolean isSolid(){
		return BlockData.isSolid(type);
	}
	
	public boolean isEdgeable(){
		return BlockData.isEdgeable(type);
	}
	
	
}
