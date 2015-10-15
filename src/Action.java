import java.util.ArrayList;

public class Action{
	private ArrayList<Location> criteriaLocs;
	private ArrayList<Integer> criteriaTypes;
	private Map map;
	private Entity parent;
	private EntityHandler ents;
	private int type;
	private int frame;
	private int displaceX, displaceY;
	
	public final static int                     WALK_LEFT = 0, WALK_RIGHT = 1, FALL = 2, GRIT = 3, CLIMB_UP_LEFT = 4, CLIMB_UP_RIGHT = 5, CLIMB_DOWN_LEFT = 6, CLIMB_DOWN_RIGHT = 7, LADDER_UP = 8, LADDER_DOWN = 9, DIE_GENERIC = 10, PLUCK_BEET = 11, DRILL_DOWN = 12, MINE_LEFT = 13, MINE_RIGHT = 14;
	public final static int[]         FRAMES = {            8,              8,        8,       32,                16,                 16,                  16,                   16,             8,               8,               32,              16,              32,             32,              32};
	public final static int[]    X_DIRECTION = {           -1,              1,        0,        0,                -1,                  1,                  -1,                    1,             0,               0,                0,               0,               0,             -1,               1}; //left = -1, right = 1
	public final static int[]    Y_DIRECTION = {            0,              0,        1,        0,                -1,                 -1,                   1,                    1,            -1,               1,                0,               0,               1,              0,               0}; //up = -1, down = 1
	public final static int NUM_ACTIONS = FRAMES.length;
	
	//TODO ANIMATION FOR DRILLING, ITEM FOR DRILLING?
	
	public Action(int type, Map map){
		this.criteriaLocs = new ArrayList<Location>();
		this.criteriaTypes = new ArrayList<Integer>();
		this.map = map;
		this.type = type;
		this.frame = -1;
		this.displaceX = 0;
		this.displaceY = 0;
	}
	
	public void update(){
		frame++;
		//actions for each frame of each action
		switch(type){ //TODO FIX THIS? MAKE STRING SOLUTION?
		case WALK_LEFT:
		case WALK_RIGHT:
		case LADDER_UP:
		case LADDER_DOWN:
			if(frame == 7){
				displaceX = 0;
				displaceY = 0;
				parent.changeX(X_DIRECTION[type]);
				parent.changeY(Y_DIRECTION[type]);
			}else if(frame < 7){
				displaceX += 4*X_DIRECTION[type];
				displaceY += 4*Y_DIRECTION[type];
			}
			break;
		case FALL:
			if(frame == 7){
				displaceY = 0;
				parent.changeY(1);
			}else if(frame < 7)
				displaceY += 4;
			break;
		case GRIT:
			if(frame == 3)
				parent.getInventory().remove(Item.BEET); //TODO TEMP make a grit item to replace this
			break;
		case CLIMB_UP_LEFT:
		case CLIMB_UP_RIGHT:
			if(frame == 10)
				parent.changeY(Y_DIRECTION[type]);
			else if(frame == 12)
				parent.changeX(X_DIRECTION[type]);
			break;
		case CLIMB_DOWN_LEFT:
		case CLIMB_DOWN_RIGHT:
			if(frame == 7)
				parent.changeX(X_DIRECTION[type]);
			else if(frame == 9)
				parent.changeY(Y_DIRECTION[type]);
			break;
		case DIE_GENERIC:
			if(frame == 31){
				parent.kill();
				if(map.isBottomed(new Location(parent.getX(), parent.getY())))
					map.setBlock(new Location(parent.getX(), parent.getY()), BlockData.GRAVE, null);
			}
			break;
		case PLUCK_BEET:
			if(frame == 7){
				map.setBlock(new Location(parent.getX(), parent.getY()), BlockData.AIR, null);
				parent.getInventory().add(Item.BEET);
			}
			break;
		case DRILL_DOWN:
			if(frame == 31){
				map.setBlock(new Location(parent.getX(), parent.getY()+Y_DIRECTION[type]), BlockData.AIR, null);
				ents.addItem(new Location(parent.getX(), parent.getY()+Y_DIRECTION[type]), Item.DIRT);
			}
			break;
		case MINE_LEFT:
		case MINE_RIGHT:
			if(frame == 31){
				map.setBlock(new Location(parent.getX()+X_DIRECTION[type], parent.getY()), BlockData.AIR, null);
				ents.addItem(new Location(parent.getX()+X_DIRECTION[type], parent.getY()), Item.DIRT);
			}
		}
	}
	
	public void setParent(Entity parent){
		this.parent = parent;
	}
	
	public void addCriteria(Entity parent){
		switch(type){
		case WALK_LEFT:
		case WALK_RIGHT:
		case MINE_LEFT:
		case MINE_RIGHT:
			addCriteriaUsingLocation(new Location(parent.getX(), parent.getY()+1));
			addCriteriaUsingLocation(new Location(parent.getX()+X_DIRECTION[type], parent.getY()));
			break;
		case FALL:
		case GRIT:
		case PLUCK_BEET:
		case DRILL_DOWN:
			addCriteriaUsingLocation(new Location(parent.getX(), parent.getY()+1));
			break;
		case CLIMB_UP_LEFT:
		case CLIMB_UP_RIGHT:
			addCriteriaUsingLocation(new Location(parent.getX()+X_DIRECTION[type], parent.getY()));
			break;
		case CLIMB_DOWN_LEFT:
		case CLIMB_DOWN_RIGHT:
			addCriteriaUsingLocation(new Location(parent.getX()+X_DIRECTION[type], parent.getY()+1));
			addCriteriaUsingLocation(new Location(parent.getX(), parent.getY()+1));
			break;
		case LADDER_UP:
		case LADDER_DOWN:
			addCriteriaUsingLocation(new Location(parent.getX(), parent.getY()));
			break;
		case DIE_GENERIC:
			break;
		}
	}
	
	public void addCriteriaUsingLocation(Location loc){
		criteriaLocs.add(loc);
		criteriaTypes.add(map.getBlockType(loc));
	}
	
	public boolean criteriaMatch(){
		for(int i = 0; i < criteriaLocs.size(); i++){
			if(map.getBlockType(criteriaLocs.get(i)) != criteriaTypes.get(i))
				return false;
		}
		return true;
	}
	
	public int getXDisplacement(){
		return displaceX;
	}
	
	public int getYDisplacement(){
		return displaceY;
	}
	
	public int getType(){
		return type;
	}
	
	public int getFrame(){
		if(frame < FRAMES[type]) return frame;
		else return FRAMES[type]-1;
	}
	
	public boolean isValid(){
		//if not already dying and parent is on a water block
		if(type != DIE_GENERIC &&
		   BlockData.isWater(map.getBlockType(new Location(parent.getX(), parent.getY()))))
			return false;
		//if action is over
		if(frame == FRAMES[type]-1)
			return false;
		//checking criteria
		if(!criteriaMatch())
			return false;
		return true;
	}
	
	public void addEntityHandler(EntityHandler ents){
		this.ents = ents;
	}
	
	public boolean equals(int type){
		try{
			if(this.type == type)
				return true;
			else return false;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean equals(Action other){
		try{
			if(this.type == other.getType())
				return true;
			else return false;
		}catch(Exception e){
			return false;
		}
	}
}
