import java.awt.Graphics;

public class Map {
	private int width, height;
	private Block[][] blocks;
	
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		this.blocks = new Block[width][height];
		Refs.setMap(this);
	}
	
	public void paint(Graphics g){
		//paints all non-air blocks
		for(Block[] b : blocks)
			for(Block bb : b)
				if(bb.getType() != BlockData.AIR) bb.paint(g);
	}
	
	public void paintEdgesAndProperties(Graphics g){
		//paints edges last (so edges will overlap)
		for(Block[] b : blocks)
			for(Block bb : b){
				if(BlockData.isWater(bb.getType())) bb.paintProperties(g);
				if(!bb.isSolid()) bb.paintEdge(g);
			}
	}
	
	public void update(){
		for(Block[] b : blocks)
			for(Block bb : b)
				bb.update();
	}
	
	public void cleanUp(){
		for(Block[] b : blocks)
			for(Block bb : b)
				if(bb.shouldKill())
					setBlock(bb.getX(), bb.getY(), BlockData.AIR, null);
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setBlock(int x, int y, int type, Location source){
		int tempEdgeID = blocks[x][y].getEdgeID();
		try{
			blocks[x][y] = new Block(x, y, type, this, source);
			blocks[x][y].setEdgeID(tempEdgeID);
		}catch(Exception e){}
	}
	
	public void setBlock(Location loc, int type, Location source){
		int x = loc.getX();
		int y = loc.getY();
		int tempEdgeID = blocks[x][y].getEdgeID();
		try{
			blocks[x][y] = new Block(x, y, type, this, source);
			blocks[x][y].setEdgeID(tempEdgeID);
		}catch(Exception e){}
	}
	
	public Block getBlock(Location location){
		return blocks[location.getX()][location.getY()];
	}
	
	public void setBlockType(int x, int y, int type){
		try{
			blocks[x][y].setType(type);
		}catch(Exception e){}
	}
	
	public void setBlockType(Location loc, int type){
		try{
			blocks[loc.getX()][loc.getY()].setType(type);
		}catch(Exception e){}
	}
	
	public int getBlockType(Location loc){
		try{
			 return blocks[loc.getX()][loc.getY()].getType();
		}catch(Exception e){
			return BlockData.AIR;
		} 
	}
	
	public boolean isBlockType(Location loc, int type){
		try{
			 return blocks[loc.getX()][loc.getY()].getType() == type;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean isPlantableSpace(Location loc, boolean isLit){
		boolean result = false;
		try{
			if(getBlockType(loc) == BlockData.AIR &&
			   blocks[loc.getX()][loc.getY()+1].getType() == BlockData.DIRT)
					result = true;
		}catch(Exception e){ result = false; }
		if(!isLit || !result) return result;
		//if has to be lit and passed previous criteria, check for light
		return isLitRecursive(loc);
	}
	
	public boolean isLitRecursive(Location loc){
		try{
			if(blocks[loc.getX()][loc.getY()-1].isSolid())
				return false;
			else
				return isLitRecursive(new Location(loc.getX(), loc.getY()-1));
		}catch(Exception e){
			return true;
		}
	}
	
	public boolean isBottomed(Location loc){
		try{
			if(blocks[loc.getX()][loc.getY()+1].isSolid())
				return true;
			else
				return false;
		}catch(Exception e){
			return true;
		}
	}
	
	public boolean isMinable(Location loc){
		try{
			if(BlockData.isMinable(getBlockType(loc)))
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Generates a simple map.
	 * @param surfaceDepth number of upper layers to always be air
	 * @param molarity the density of dirt blocks, 0-300.
	 */
	public void generateMap(int surfaceDepth, int molarity){
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[0].length; j++){
				if(j < surfaceDepth) blocks[i][j] = new Block(i, j, BlockData.AIR, this, null);
				else{
					int chance = Util.randomIntInclusive(0, blocks[0].length-1);
					if(chance <= j*(molarity/100.)) blocks[i][j] = new Block(i, j, BlockData.DIRT, this, null);
					else blocks[i][j] = new Block(i, j, BlockData.AIR, this, null);
					//chance = Util.randomIntInclusive(0, 60);
					//if(chance == 0) blocks[i][j] = new Block(i, j, BlockData.WATER_SPRING, this, null);
				}
			}
		}
		//place sprouts //TODO make bottom-dependency property on properties class
		for(int i = 0; i < blocks.length; i++){
			for(int j = 0; j < blocks[0].length; j++){
				if(isPlantableSpace(new Location(i, j), false) &&
				   Util.randomIntInclusive(0, 2) == 0) //33% chance
					setBlock(i, j, BlockData.SPROUT, null);
			}
		}
	}
	
	public int getAdjacencyCode(int x, int y){
		int count = 0;
		try{ if(blocks[x][y-1].isEdgeable()) count += 1; }catch(Exception e){}
		try{ if(blocks[x+1][y].isEdgeable()) count += 2; }catch(Exception e){}
		try{ if(blocks[x][y+1].isEdgeable()) count += 4; }catch(Exception e){}
		try{ if(blocks[x-1][y].isEdgeable()) count += 8; }catch(Exception e){}
		return count;
	}
	
	public int getWaterAdjacencyCode(int x, int y){
		int count = 0;
		try{ if(BlockData.isWater(blocks[x][y-1].getType())) count += 1; }catch(Exception e){}
		try{ if(BlockData.isWater(blocks[x+1][y].getType())) count += 2; }catch(Exception e){}
		try{ if(BlockData.isWater(blocks[x][y+1].getType())) count += 4; }catch(Exception e){}
		try{ if(BlockData.isWater(blocks[x-1][y].getType())) count += 8; }catch(Exception e){}
		return count;
	}
	
	public boolean getWaterAdjacency(int x, int y){
		try{ if(BlockData.isWater(blocks[x-1][y].getType())) return true; }catch(Exception e){} //left
		try{ if(BlockData.isWater(blocks[x][y-1].getType())) return true; }catch(Exception e){} //up
		try{ if(BlockData.isWater(blocks[x+1][y].getType())) return true; }catch(Exception e){} //right
		return false;
	}
	
	public void spreadWater(int x, int y, Location source){
		Block cur;
		try{
			cur = blocks[x-1][y]; //left
			if(!cur.isSolid() && !BlockData.isWater(cur.getType()) && blocks[x][y+1].isSolid())
				setBlock(x-1, y, BlockData.WATER, source);
		}catch(Exception e){}
		try{
			cur = blocks[x][y+1]; //up
			if(!cur.isSolid() && !BlockData.isWater(cur.getType()))
				setBlock(x, y+1, BlockData.WATER, source);
		}catch(Exception e){}
		try{
			cur = blocks[x+1][y]; //right
			if(!cur.isSolid() && !BlockData.isWater(cur.getType()) && blocks[x][y+1].isSolid())
				setBlock(x+1, y, BlockData.WATER, source);
		}catch(Exception e){}
	}
}