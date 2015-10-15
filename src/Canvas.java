import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseListener, KeyListener{
	private int pWidth, pHeight;
	private Map map;
	private EntityHandler ents;
	private Game parent;
	
	private String tempKeyStr = ""; //TODO temp
	private int tempPlaceID = 0; //TODO temp
	
	//TODO make pickaxe/jackhammer to mine with. more digging actions. axe with tree. pluck beets.
	
	public Canvas(int pWidth, int pHeight, Map map, EntityHandler ents, Game parent){
		this.pWidth = pWidth;
		this.pHeight = pHeight;
		this.map = map;
		this.ents = ents;
		this.parent = parent;
		this.setBackground(new Color(115, 205, 249));
		this.addMouseListener(this);
		this.addKeyListener(this);
	}
	
	public boolean isFocusable(){
		return true;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(pWidth, pHeight);
	}
	
	public Dimension getMinimumSize(){
		return new Dimension(pWidth, pHeight);
	}
	
	public Dimension getMaximumSize(){
		return new Dimension(pWidth, pHeight);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.paint(g);
		ents.paint(g);
		map.paintEdgesAndProperties(g);
		g.drawString(tempKeyStr, 4, 32); //TODO TEMP
		
		paintGUI(g);
	}
	
	public void paintGUI(Graphics g){
		MiniMan selected = ents.getSelectedMiniMan();
		g.translate(0, pHeight-64);
		g.drawImage(ImageHandler.getBottomGuiImage(), 0, 0, null);
		g.setFont(new Font("Rockwell", Font.BOLD, 15));
		if(selected == null){
			g.drawString("None Selected", 64, 32);
		}else{
			g.drawImage(ImageHandler.getSelectionImage(),
					selected.getXOnScreen(), selected.getYOnScreen()-pHeight+64, null);
			g.drawImage(ImageHandler.getMiniManImage(selected), 16, 16, null);
			g.drawString("#"+ents.getSelectionIndex()+" "+selected.getFullName(), 64, 32);
			g.translate(256, 0);
			selected.getInventory().paint(g);
		}
	}

	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		try{
			map.setBlock(e.getX()/32, e.getY()/32, tempPlaceID, null);
		}catch(Exception ee){}
	}
	public void mouseReleased(MouseEvent e){}
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() >= 48 && e.getKeyCode() <= 57){
			//TODO TEMP block placement type
			tempKeyStr = e.getKeyChar()+" "+e.getKeyCode()+" "+BlockData.getName(e.getKeyCode()-48);
			//
			tempPlaceID = e.getKeyCode()-48;
		}
		if(tempPlaceID == BlockData.WATER) tempPlaceID = BlockData.AIR;
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			ents.addMiniMan();
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			ents.changeSelection(-1);
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			ents.changeSelection(1);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			ents.changeSelection(0);
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}
