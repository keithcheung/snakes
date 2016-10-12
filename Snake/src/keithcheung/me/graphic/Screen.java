package keithcheung.me.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import keithcheung.me.entities.Apple;
import keithcheung.me.entities.BodyPart;

public class Screen extends JPanel implements Runnable {

	public static final int WIDTH = 800, HEIGHT = 800;
	private Thread thread;
	private boolean running = false;
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	
	private Apple apple;
	private ArrayList<Apple> apples;
	
	private Random r;
	
	private int xCoor = 10, yCoor = 10;
	private int size = 5;
	
	private boolean right = true, left = false, up = false, down = false;
	
	private int ticks = 0;
	
	private Key key;
	private int score = 0;
	private int tickLimit = 300000;
	public Screen() {
		setFocusable(true);
		key = new Key();
		addKeyListener(key);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		r = new Random();
		
		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();
		
		start();
	}
	
	public void tick() {
		if (snake.size() == 0) {
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
		}
		
		if (apples.size() == 0) {
			int xCoor = r.nextInt(75);
			int yCoor = r.nextInt(75);
			
			apple = new Apple(xCoor,yCoor, 10);
			apples.add(apple);
		}
		
		for (int i = 0; i < apples.size(); i ++) {
			if(xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				size = size + 5; // Changes size of snake for every apple eaten
				score = score + 10;
				apples.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < snake.size(); i ++) {
			if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
				if(i != snake.size() - 1) {
					stop();
				}
			}
		}
		
		if(xCoor < 0 || xCoor > 79 || yCoor < 0 || yCoor > 75) {
			stop();
		}
		
		ticks ++;
		
		if (ticks > tickLimit) { // Change for different speed of snake
			if(right) xCoor++;
			if(left) xCoor--;
			if(up) yCoor--;
			if(down) yCoor++;
			
			ticks = 0;
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
		
			if (snake.size() > size) {
				snake.remove(0);
			}
			if (score > 200) {
				tickLimit = 150000;
			}
			else if (score > 100) {
				tickLimit = 200000; // to help with this lag
			}
			
		}
		
		
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(new Color(10,50,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		for (int i = 0; i < WIDTH /10; i++) {
			g.drawLine(i * 10,  0, i * 10,  HEIGHT - 30);
		}
		
		for (int i = 0; i < (HEIGHT - 20) /10; i++) {
			g.drawLine(0, i * 10, WIDTH, i * 10);
		}
		
		for(int i = 0; i < snake.size(); i ++) {
			snake.get(i).draw(g);
		}
		for (int i = 0; i < apples.size(); i ++) {
			apples.get(i).draw(g);
		}
		g.setColor(new Color(0,0,0));
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Score: " + score, 50, HEIGHT - 10); // tracking score
	}
	
	public void start () {
			running = true;
			thread = new Thread(this, "Game Loop");
			thread.start();
	}
	
	public void stop () {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(running) {
			tick();
			repaint();
		}
	}
	
	
	private class Key implements KeyListener {

		
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_RIGHT && !left) {
				up = false;
				down = false;
				right = true;
				
			}
			
			if (key == KeyEvent.VK_LEFT && !right) {
				up = false;
				down = false;
				left = true;
				
			}
			
			if (key == KeyEvent.VK_UP && !down) {
				left = false;
				right = false;
				up = true;
			}
			
			if (key == KeyEvent.VK_DOWN && !up) {
				left = false;
				right = false;
				down = true;
			}
			if (key == KeyEvent.VK_R) {
				start();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
