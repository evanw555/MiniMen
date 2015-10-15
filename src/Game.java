import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game{
	private final int UPDATE_MILLIS = 1000/16;
	private int width, height;
	private JFrame frame;
	private Canvas canvas;
	private Container cont;
	
	private Map map;
	private EntityHandler ents;
	
	public Game(int width, int height){
		this.width = width;
		this.height = height;
		frame = new JFrame("MiniMen");
		
		ImageHandler.loadImages();
		Util.readNames();
		map = new Map(width, height);
		map.generateMap(4, 500);
		ents = new EntityHandler(map);
		
		canvas = new Canvas(width*32, height*32+64, map, ents, this);
		cont = frame.getContentPane();
		cont.add(canvas, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}});
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		frame.toFront();
		frame.setVisible(true);
		canvas.requestFocus();
		
		run();
	}
	
	public void run(){
		while(true){
			this.update();
			canvas.repaint();
			try{ Thread.sleep(UPDATE_MILLIS); }catch(Exception e){}
		}
	}
	
	public void update(){
		map.update();
		map.cleanUp();
		ents.update();
		ents.cleanUp();
	}
	
	public EntityHandler getEntityHandler(){
		return ents;
	}
}