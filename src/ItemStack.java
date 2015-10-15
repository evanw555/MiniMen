import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ItemStack {
	private final int STACK_CAPACITY = 8;
	private int type, quantity;
	private boolean killMe;
	
	public ItemStack(int type){
		this.type = type;
		this.quantity = 1;
		this.killMe = false;
	}
	
	public void add(int d){
		quantity += d;
		if(quantity < 1) //if nothing in stack, then kill it
			killMe = true;
		else if(quantity > STACK_CAPACITY) //if too many, then cut it down
			quantity = STACK_CAPACITY;
	}
	
	public void paint(int xPix, int yPix, Graphics g){
		g.drawImage(ImageHandler.getItemImage(type), xPix, yPix, null);
		//paint the quantity of this stack with shadowing, but not if this contains only 1 item
		if(quantity > 1){
			g.setColor(Color.BLACK);
			g.drawString(quantity+"", xPix+24+1, yPix+32+1);
			g.setColor(Color.WHITE);
			g.drawString(quantity+"", xPix+24, yPix+32);
		}
		}
	
	public boolean isFull(){
		return quantity == STACK_CAPACITY;
	}
	
	public boolean shouldKill(){
		return killMe;
	}
	
	public boolean equals(int otherType){
		return this.type == otherType;
	}
}
