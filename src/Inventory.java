import java.awt.Graphics;
import java.util.ArrayList;

public class Inventory {
	private final int INV_CAPACITY = 16;
	private ArrayList<ItemStack> inv;
	
	public Inventory(){
		inv = new ArrayList<ItemStack>();
	}
	
	public void add(int itemType){
		if(contains(itemType, true)){
			inv.get(getIndexOfItemStack(itemType, true)).add(1);
		}else if(!this.isFull()){
			inv.add(new ItemStack(itemType));
		}
	}
	
	public void remove(int itemType){
		if(contains(itemType, false)){
			ItemStack current = inv.get(getIndexOfItemStack(itemType, false));
			current.add(-1);
			if(current.shouldKill()) //if removed the last item, then remove this stack from inventory
				inv.remove(getIndexOfItemStack(itemType, false));
		}
	}
	
	public void remove(int itemType, int quantity){
		for(int i = 0; i < quantity; i++)
			remove(itemType);
	}
	
	public void paint(Graphics g){
		for(int i = 0; i < inv.size(); i++){
			inv.get(i).paint((i%8)*32, (i/8)*32, g);
		}
	}
	
	public boolean canAdd(int itemType){
		//can add if inventory isn't full or if there is an open stack of the same type
		return !isFull() || contains(itemType, true);
	}
	
	public boolean contains(int itemType, boolean mustBeOpen){
		for(ItemStack i : inv)
			if(i != null && i.equals(itemType)){
				if(mustBeOpen && !i.isFull()) //if the item stack must have an open slot
					return true;
				else if(!mustBeOpen) //if not, then just check for its presence
					return true;
			}
		return false;
	}
	
	public boolean isFull(){
		return inv.size() == INV_CAPACITY;
	}
	
	public int getIndexOfItemStack(int itemType, boolean mustBeOpen){
		for(int i = 0; i < inv.size(); i++)
			if(inv.get(i) != null && inv.get(i).equals(itemType)){
				if(mustBeOpen && !inv.get(i).isFull()) //if stack must be open, then give open index
					return i;
				else if(!mustBeOpen) //if not, then just give first index regardless
					return i;
			}
		return -1;
	}
}
