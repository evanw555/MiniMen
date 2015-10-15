import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageHandler{
	private static BufferedImage mainBlockImg;
	private static BufferedImage[][] subBlockImgs;
	
	private static BufferedImage mainEdgeImg;
	private static BufferedImage[][] subEdgeImgs;
	
	private static BufferedImage mainWaterImg;
	private static BufferedImage[][] subWaterImgs;
	
	private static int numMiniManImgs;
	private static BufferedImage[] mainMiniManImgs;
	private static BufferedImage[][][] subMiniManImgs;
	
	private static BufferedImage mainItemImg;
	private static BufferedImage[] subItemImgs;
	
	private static BufferedImage bottomGui;
	private static BufferedImage selection;
	
	public static void loadImages(){
		loadBlockImages();
		loadEdgeImages();
		loadWaterImages();
		loadMiniManImages();
		loadItemImages();
		loadBottomGui();
		loadSelection();
	}
	
	public static void loadBlockImages(){
		mainBlockImg = loadImage("images/blocks/blocks.PNG");
		subBlockImgs = new BufferedImage[mainBlockImg.getHeight()/32][mainBlockImg.getWidth()/32];
		for(int i = 0; i < mainBlockImg.getHeight()/32; i++){
			for(int j = 0; j < mainBlockImg.getWidth()/32; j++){
				subBlockImgs[i][j] = mainBlockImg.getSubimage(j*32, i*32, 32, 32);
			}
		}
	}
	
	public static void loadEdgeImages(){
		mainEdgeImg = loadImage("images/blocks/edges.PNG");
		subEdgeImgs = new BufferedImage[mainEdgeImg.getHeight()/32][mainEdgeImg.getWidth()/32];
		for(int i = 0; i < mainEdgeImg.getHeight()/32; i++){
			for(int j = 0; j < mainEdgeImg.getWidth()/32; j++){
				subEdgeImgs[i][j] = mainEdgeImg.getSubimage(j*32, i*32, 32, 32);
			}
		}
	}
	
	public static void loadWaterImages(){
		mainWaterImg = loadImage("images/blocks/water.PNG");
		subWaterImgs = new BufferedImage[mainWaterImg.getHeight()/32][mainWaterImg.getWidth()/32];
		for(int i = 0; i < mainWaterImg.getHeight()/32; i++){
			for(int j = 0; j < mainWaterImg.getWidth()/32; j++){
				subWaterImgs[i][j] = mainWaterImg.getSubimage(j*32, i*32, 32, 32);
			}
		}
	}
	
	public static void loadMiniManImages(){
		countMiniManImages();
		mainMiniManImgs = new BufferedImage[numMiniManImgs];
		for(int i = 0; i < numMiniManImgs; i++)
			mainMiniManImgs[i] = loadImage("images/entity/miniman/"+i+".PNG");
		for(int k = 0; k < numMiniManImgs; k++){
			subMiniManImgs = new BufferedImage[numMiniManImgs][mainMiniManImgs[k].getHeight()/32][mainMiniManImgs[k].getWidth()/32];
			for(int i = 0; i < Action.NUM_ACTIONS; i++){ //height
				for(int j = 0; j < Action.FRAMES[i]; j++){ //width
					subMiniManImgs[k][i][j] = mainMiniManImgs[k].getSubimage(j*32, i*32, 32, 32);
				}
			}
		}
	}
	
	public static void countMiniManImages(){
		boolean repeat = true;
		int x = 0;
		do{
			try{
				loadImageNoCatch("images/entity/miniman/"+x+".PNG");
				numMiniManImgs++;
				x++;
			}catch(Exception e){
				repeat = false;
			}
		}while(repeat);
	}
	
	public static void loadItemImages(){
		mainItemImg = loadImage("images/entity/item/items.PNG");
		subItemImgs = new BufferedImage[mainItemImg.getHeight()/32];
		for(int i = 0; i < mainItemImg.getHeight()/32; i++){
			subItemImgs[i] = mainItemImg.getSubimage(0, i*32, 32, 32);
		}
	}
	
	public static void loadBottomGui(){
		bottomGui = loadImage("images/gui/bottom_gui.PNG");
	}
	
	public static void loadSelection(){
		selection = loadImage("images/gui/selection.PNG");
	}
	
	public static BufferedImage getBlockImage(int type, int id){
		return subBlockImgs[type][id];
	}
	
	public static BufferedImage getEdgeImage(int code, int id){
		return subEdgeImgs[code][id];
	}
	
	public static BufferedImage getWaterImage(int code, int id){
		return subWaterImgs[code][0];
	}
	
	public static BufferedImage getMiniManImage(int id, int action, int frame){
		return subMiniManImgs[id][action][frame];
	}
	
	public static BufferedImage getMiniManImage(MiniMan m){
		return subMiniManImgs[m.getID()][m.getAction().getType()][m.getAction().getFrame()];
	}
	
	public static int getNumMiniManImgs(){
		return numMiniManImgs;
	}
	
	public static BufferedImage getItemImage(int type){
		return subItemImgs[type];
	}
	
	public static BufferedImage getBottomGuiImage(){
		return bottomGui;
	}
	
	public static BufferedImage getSelectionImage(){
		return selection;
	}
	
	public static BufferedImage loadImage(String location){
		try{
			 return ImageIO.read(new File(location));
		}catch(IOException e){ System.out.println("Failed: "+location); }
		return null;
	}
	
	public static BufferedImage loadImageNoCatch(String location) throws Exception{
		return ImageIO.read(new File(location));
	}
}
