import java.util.ArrayList;

public class RandomSoul extends Soul{
	private Entity parent;
	private Map map;
	private Action previous;
	private ArrayList<Action> choices;
	private int x, y;
	
	public RandomSoul(Entity parent, Map map){
		this.parent = parent;
		this.map = map;
	}
	
	public Action getAction(){
		//get previous action and initialize choice list, get parent x and y
		previous = parent.getAction();
		choices = new ArrayList<Action>();
		x = parent.getX();
		y = parent.getY();
		
		//TODO MAKE PROPER CHOICE CHART
		
		//if current block is water
		if(BlockData.isWater(map.getBlockType(new Location(x, y)))){
			choices.add(new Action(Action.DIE_GENERIC, map)); //DIE_GENERIC
		//if current block isn't water
		}else{
			//if current block is a ladder
			if(map.isBlockType(new Location(parent.getX(), parent.getY()), BlockData.LADDER)){
				//if block above is a ladder and last action wasn't climbing-ladder-down
				if(map.isBlockType(new Location(parent.getX(), parent.getY()-1), BlockData.LADDER) &&
				   !previousEquals(Action.LADDER_DOWN))
					choices.add(new Action(Action.LADDER_UP, map)); //LADDER_UP
				//if block below is a ladder and last action wasn't climbing-ladder-up
				if(map.isBlockType(new Location(parent.getX(), parent.getY()+1), BlockData.LADDER) &&
				   !previousEquals(Action.LADDER_UP))
					choices.add(new Action(Action.LADDER_DOWN, map)); //LADDER_DOWN
				//if block left isn't solid and block down-left is solid or a ladder
				if(!blockIsSolid(new Location(parent.getX()-1, parent.getY()), true) &&
				   (blockIsSolid(new Location(parent.getX()-1, parent.getY()+1), true) ||
				   map.isBlockType(new Location(parent.getX()-1, parent.getY()+1), BlockData.LADDER)))
					choices.add(new Action(Action.WALK_LEFT, map)); //WALK_LEFT
				//if block right isn't solid and block down-right is solid or a ladder
				if(!blockIsSolid(new Location(parent.getX()+1, parent.getY()), true) &&
				   (blockIsSolid(new Location(parent.getX()+1, parent.getY()+1), true) ||
				   map.isBlockType(new Location(parent.getX()+1, parent.getY()+1), BlockData.LADDER)))
					choices.add(new Action(Action.WALK_RIGHT, map)); //WALK_RIGHT
				//if list of choices is empty (ie, at top or bottom of ladder); allows switching direction
				if(choices.size() == 0){
					//if block above is a ladder
					if(map.isBlockType(new Location(parent.getX(), parent.getY()-1), BlockData.LADDER))
						choices.add(new Action(Action.LADDER_UP, map)); //LADDER_UP
					//if block below is a ladder
					if(map.isBlockType(new Location(parent.getX(), parent.getY()+1), BlockData.LADDER))
						choices.add(new Action(Action.LADDER_DOWN, map)); //LADDER_DOWN
				}
			//if current block isn't a ladder
			}else{
				//if block below isn't solid
				if(!blockIsSolid(new Location(parent.getX(), parent.getY()+1), true)){
					choices.add(new Action(Action.FALL, map)); //FALL
				//if block below is solid
				}else{
					//if previous action wasn't gritting
					if(!previousEquals(Action.GRIT) &&
					   parent.getInventory().contains(Item.BEET, false)) //TODO TEMP make it check for grit item instead
						choices.add(new Action(Action.GRIT, map)); //GRIT
					//if current block is a beet sprout
					if(map.isBlockType(new Location(parent.getX(), parent.getY()), BlockData.SPROUT))
						choices.add(new Action(Action.PLUCK_BEET, map)); //PLUCK_BEET
					//if block below is minable
					if(map.isMinable(new Location(parent.getX(), parent.getY()+1)))
						choices.add(new Action(Action.DRILL_DOWN, map)); //DRILL_DOWN
					//LEFT
					//if block left is solid
					if(blockIsSolid(new Location(parent.getX()-1, parent.getY()), true)){
						//if block to the left is minable
						if(map.isMinable(new Location(parent.getX()-1, parent.getY())))
							choices.add(new Action(Action.MINE_LEFT, map)); //MINE_LEFT
						//if block up-left and block up aren't solid
						if(!blockIsSolid(new Location(parent.getX()-1, parent.getY()-1), true) &&
						   !blockIsSolid(new Location(parent.getX(), parent.getY()-1), true))
							choices.add(new Action(Action.CLIMB_UP_LEFT, map)); //CLIMB_UP_LEFT
					//if block left isn't solid
					}else{
						//if block down-left is solid or a ladder
						if(blockIsSolid(new Location(parent.getX()-1, parent.getY()+1), true) ||
						   map.isBlockType(new Location(parent.getX()-1, parent.getY()+1), BlockData.LADDER))
							choices.add(new Action(Action.WALK_LEFT, map)); //WALK_LEFT
						//if block down-left isn't solid
						else
							choices.add(new Action(Action.CLIMB_DOWN_LEFT, map)); //CLIMB_DOWN_LEFT
					}
					//RIGHT
					//if block right is solid
					if(blockIsSolid(new Location(parent.getX()+1, parent.getY()), true)){
						//if block to the right is minable
						if(map.isMinable(new Location(parent.getX()+1, parent.getY())))
							choices.add(new Action(Action.MINE_RIGHT, map)); //MINE_RIGHT
						//if block up-right and block up aren't solid
						if(!blockIsSolid(new Location(parent.getX()+1, parent.getY()-1), true) &&
						   !blockIsSolid(new Location(parent.getX(), parent.getY()-1), true))
							choices.add(new Action(Action.CLIMB_UP_RIGHT, map)); //CLIMB_UP_RIGHT
					//if block right isn't solid
					}else{
						//if block down-right is solid or a ladder
						if(blockIsSolid(new Location(parent.getX()+1, parent.getY()+1), true) ||
						   map.isBlockType(new Location(parent.getX()+1, parent.getY()+1), BlockData.LADDER))
							choices.add(new Action(Action.WALK_RIGHT, map)); //WALK_RIGHT
						//if block down-right isn't solid
						else
							choices.add(new Action(Action.CLIMB_DOWN_RIGHT, map)); //CLIMB_DOWN_RIGHT
					}
				}
			}
		}
		
		//if list contains previous action, add it again
		if(previous != null && choicesContain(previous)){
			choices.add(new Action(previous.getType(), map));
			choices.add(new Action(previous.getType(), map));
		}
		
		//if choice list is empty, kill the parent entity
		if(choices.size() == 0){
			return new Action(Action.DIE_GENERIC, map);
		}
		
		//returns random action on the list
		Action result = choices.get(Util.randomIntInclusive(0, choices.size()-1));
		result.addEntityHandler(parent.getEntityHandler()); //add ents to action
		return result;
	}
	
	public boolean blockIsSolid(Location loc, boolean exceptionDefault){
		try{
			return map.getBlock(loc).isSolid();
		}catch(Exception e){
			return exceptionDefault;
		}
	}
	
	public boolean choicesContain(Action a){
		for(Action aa : choices){
			if(aa.getType() == a.getType())
				return true;
		}
		return false;
	}
	
	public boolean previousEquals(int type){
		try{
			if(previous.equals(type))
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}
}
