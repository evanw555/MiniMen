public class Location{
	private int x, y;
	
	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean equals(Location other){
		if(other == null)
			return false;
		return this.getX() == other.getX() && this.getY() == other.getY();
	}
}
