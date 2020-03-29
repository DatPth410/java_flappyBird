package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	public static final Color VERY_LIGHT_BLUE = new Color(51,204,255);
	public static FlappyBird flappyBird;
	public final int WIDTH = 600, HEIGHT = 700;
	public Renderer renderer;
	public Rectangle bird;
	public Rectangle ground;
	public Rectangle grass;
	public ArrayList<Rectangle> columns;
	public Random rand;
	public boolean gameOver, started;
	public int ticks, yMotion, score;
	
	public FlappyBird() {
		JFrame jframe = new JFrame();
		renderer = new Renderer();
		Timer timer = new Timer(20, this);
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 30, 20, 20);
		ground = new Rectangle(0, HEIGHT-100, WIDTH, 100);
		grass = new Rectangle(0, HEIGHT - 110, WIDTH, 10);
		columns = new ArrayList<Rectangle>();
		rand = new Random();
		
		
		jframe.setTitle("Flappy Bird");
		jframe.add(renderer);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addKeyListener(this);
		jframe.addMouseListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	public void addColumn(boolean start) {
		int space = 250;
		int width =  100;
		int height = 50 + rand.nextInt(250);
		
		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 200,HEIGHT - height - 110, width, height));
			//xem lai
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 200, 0 , width,HEIGHT - height - space - 110));
		}else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 400,HEIGHT - height - 110, width, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x, 0 , width,HEIGHT - height - space - 110));
		}
	}

	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void jump() {
		if (gameOver) {
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 30, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			addColumn(true);
			addColumn(true);
			gameOver = false;
		}
		
		if (!started) {
			started = true;
		}else if(!gameOver) {
			if (yMotion > 0) {
				yMotion = 0;
			}
			
			yMotion -= 15;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int speed = 10;
		ticks++;
		if (started) {
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				if (!gameOver) {
					column.x -= speed;
				}
				
			}
			
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
//				System.out.println(column.x+ " " + column.y + "/");
				if (column.x + column.width < 0) {
//					System.out.print(column.x+" " + column.y + "/");
					columns.remove(column);
//					System.out.print(column.x + column.y + "deleted +");
					if (column.y == 0) {
//						System.out.println("Added");
						addColumn(false);
					}
				}
			}
			
			bird.y += yMotion;
			
			for (Rectangle rectangle : columns) {
				if (rectangle.y==0 && bird.x + bird.width/2 > rectangle.x + rectangle.width/2 -10 && bird.x + bird.width/2 < rectangle.x + rectangle.width / 2 + 10) {
					score++;
				}
				
				if (rectangle.intersects(bird)) {
					gameOver = true;
					
					bird.x = rectangle.x - bird.width;
				}
			}
			
			if (bird.y + bird.height > HEIGHT - 110 || bird.y < 0) {
				gameOver = true;
			}
			if (bird.y + yMotion >= HEIGHT -110) {
				bird.y = HEIGHT - 110 - bird.height;
			}
		}
		renderer.repaint();
	}
	
	public void repaint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(VERY_LIGHT_BLUE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);
		g.fillRect(ground.x, ground.y, ground.width, ground.height);
		
		g.setColor(Color.green);
		g.fillRect(grass.x, grass.y, grass.width, grass.height);
		
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for (Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 75));
		
		if (!started) {
			g.drawString("Click to start!", 50, HEIGHT / 2 - 50);
		}
		if (gameOver) {
			g.drawString("Game Over!", 75, HEIGHT / 2 - 50);
			g.drawString("Score: "+String.valueOf(score), WIDTH /2 - 150 , HEIGHT / 2 + 100);
		}
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH /2 - 25, 100);
		}
	}

	public static void main(String[] Args) {
		flappyBird = new FlappyBird();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		jump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}
	
}
