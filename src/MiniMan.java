import java.awt.Graphics;
import java.util.ArrayList;

public class MiniMan extends Entity{
	private Action action;
	private Soul soul;
	private Map map;
	private int x, y;
	private int id;
	private boolean killMe;
	private String firstname, surname;
	private Inventory inv;
	private EntityHandler ents;
	
	public MiniMan(Map map, EntityHandler ents){
		this.map = map;
		this.ents = ents;
		x = Util.randomIntInclusive(0, map.getWidth()-1);
		y = 0;
		id = Util.randomIntInclusive(0, ImageHandler.getNumMiniManImgs()-1);
		killMe = false;
		soul = new RandomSoul(this, map);
		firstname = Util.generateFirstname();
		surname = Util.generateSurname();
		this.inv = new Inventory();
	}
	
	public void update(){
		checkItemCollisions();
		if(action == null){
			initAction();
			action.update();
		}else{
			if(!action.isValid()){
				initAction();
				action.update();
			}else{
				action.update();
			}
		}
	}
	
	public void checkItemCollisions(){
		Item colliding = ents.getCollidingItem(new Location(x, y));
		if(colliding != null)
			inv.add(colliding.take());
	}

	public void paint(Graphics g){
		if(action != null)
			g.drawImage(ImageHandler.getMiniManImage(id, action.getType(), action.getFrame()),
					getXOnScreen(), getYOnScreen(), null);
	}
	
	public void initAction(){
		action = soul.getAction();
		action.addCriteria(this);
		action.setParent(this);
		//inv.add(Util.randomIntInclusive(0, 0)); //TODO TEMP giving item every turn
	}
	
	public Action getAction(){
		return action;
	}
	
	public Inventory getInventory(){
		return inv;
	}
	
	public EntityHandler getEntityHandler(){
		return ents;
	}
	
	public int getID(){
		return id;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public int getXOnScreen(){
		return x*32 + getXDisplacement();
	}
	
	public int getYOnScreen(){
		return y*32 + getYDisplacement();
	}
	
	public void changeX(int dx){
		x += dx;
	}
	
	public void changeY(int dy){
		y += dy;
	}
	
	public int getXDisplacement(){
		if(action == null) return 0;
		return action.getXDisplacement();
	}
	
	public int getYDisplacement(){
		if(action == null) return 0;
		return action.getYDisplacement();
	}
	
	public Location getLocation(){
		return new Location(x, y);
	}
	
	public String getFullName(){
		return firstname+" "+surname;
	}
	
	public void kill(){
		killMe = true;
	}

	public boolean shouldKill(){
		return killMe;
	}

}
