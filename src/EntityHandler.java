import java.awt.Graphics;
import java.util.ArrayList;

public class EntityHandler {
	private ArrayList<Entity> ents, purgatory;
	private Map map;
	private int selectionIndex = -1;
	private MiniMan selectedMM = null;
	
	public EntityHandler(Map map){
		ents = new ArrayList<Entity>();
		purgatory = new ArrayList<Entity>();
		this.map = map;
	}
	
	public void update(){
		synchronized(ents){
			if(!purgatory.isEmpty()){
				for(Entity e : purgatory){ //for synch/concurrence reasons
					ents.add(e);
					if(e instanceof MiniMan) //if miniman added
						changeSelection(0); //clear selection index
				}
				purgatory.clear();
			}
			for(Entity e : ents)
				e.update();
		}
		if(selectedMM != null && selectedMM.shouldKill())
			changeSelection(0); //clears selection index if selected miniman dies
	}
	
	public void paint(Graphics g){
		synchronized(ents){
			for(Entity e : ents)
				e.paint(g);
		}
	}
	
	public void cleanUp(){
		synchronized(ents){
			for(int i = 0; i < ents.size(); i++){
				if(ents.get(i).shouldKill()){
					ents.remove(i);
					i--;
				}
			}
		}
	}
	
	public void addMiniMan(){
		purgatory.add(new MiniMan(map, this));
	}
	
	public void addItem(Location loc, int type){
		purgatory.add(new Item(loc.getX(), loc.getY(), type, this));
	}
	
	public Item getCollidingItem(Location loc){
		for(Entity e : ents) //returns first instance of colliding item
			if(e instanceof Item && e.getLocation().equals(loc))
				return (Item)e;
		return null; //if no items colliding with location
	}
	
	public void changeSelection(int d){
		//get number of minimen in entity list
		int count = 0;
		for(Entity e : ents)
			if(e instanceof MiniMan)
				count++;
		//if there are none or if argument is 0, make it unselected
		if(count == 0 || d == 0){
			selectionIndex = -1;
			selectedMM = null;
			return;
		}
		//change selection index
		selectionIndex += d;
		//correct being under the proper index bounds; make it wrap
		if(selectionIndex < 0)
			selectionIndex = count-1;
		//correct being over; make it wrap
		if(selectionIndex >= count)
			selectionIndex = 0;
		//make the miniman of the selected index the selected miniman
		int indexCount = 0;
		for(Entity e : ents){
			if(e instanceof MiniMan){
				if(selectionIndex == indexCount){
					selectedMM = (MiniMan)e;
					break;
				}
				else
					indexCount++;
			}
		}
	}
	
	public MiniMan getSelectedMiniMan(){
		return selectedMM;
	}
	
	public int getSelectionIndex(){
		return selectionIndex;
	}
}