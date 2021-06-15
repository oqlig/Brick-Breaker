
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	private boolean play = false;
	private int score = 0;
	private String nickname = "";
	
	private int totalBricks = 48;
	
	private final Timer timer;
	private int delay=8;
	
	private int playerX = 310;
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;

	private UserScores connector;
	private MapGenerator map;

	
	public Gameplay()
	{
		Scanner sc=new Scanner(System.in);
		System.out.print("Enter Your nickname: ");
		nickname = sc.nextLine();
		map = new MapGenerator(4, 12);
		connector = new UserScores();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{    		
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// bricks
		map.draw((Graphics2D) g);
		
		// borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// score
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS",Font.BOLD, 25));
		g.drawString(""+score, 590,30);
		
		// the paddle
		g.setColor(Color.cyan);
		g.fillRect(playerX, 550, 100, 8);
		
		// the ball
		g.setColor(Color.green);
		g.fillOval(ballposX, ballposY, 20, 20);

		// won the game
		if(totalBricks <= 0)
		{
			connector.addPlayerScore(nickname,score);

			String players = connector.bestScores();
			System.out.println(players);
			String[] playersSplit = players.split(", ");

			play = false;
			ballXdir = 0;
			ballYdir = 0;
			int y = 300;
			int place = 0;

			g.setColor(Color.PINK);
			g.setFont(new Font("Comic Sans MS",Font.BOLD, 30));
			g.drawString("Top three scores:", 190,260);

			for (int i=0; i<3; i++){
				place = i + 1;
				g.setColor(Color.PINK);
				g.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
				g.drawString(place + ". " + playersSplit[i], 230,y);
				y += 30;
			}

             g.setColor(Color.PINK);
             g.setFont(new Font("Comic Sans MS",Font.BOLD, 30));
             g.drawString("Congratulations! You Won!", 260,450);
             
             g.setColor(Color.PINK);
             g.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
             g.drawString("Press (Enter) to Restart", 230,500);
		}
		
		// lose the game
		if(ballposY > 570)
        {
			connector.addPlayerScore(nickname,score);

        	String players = connector.bestScores();
			System.out.println(players);
			String[] playersSplit = players.split(", ");

			 play = false;
             ballXdir = 0;
     		 ballYdir = 0;
     		 int y = 300;
     		 int place = 0;

			g.setColor(Color.PINK);
			g.setFont(new Font("Comic Sans MS",Font.BOLD, 30));
			g.drawString("Top three scores:", 190,260);

     		 for (int i=0; i<3; i++){
     		 	place = i + 1;
				 g.setColor(Color.PINK);
				 g.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
				 g.drawString(place + ". " + playersSplit[i], 230,y);
				 y += 30;
			 }

             g.setColor(Color.PINK);
             g.setFont(new Font("Comic Sans MS",Font.BOLD, 30));
             g.drawString("Game over, Score: "+score, 190,450);
             
             g.setColor(Color.PINK);
             g.setFont(new Font("Comic Sans MS",Font.BOLD, 20));
             g.drawString("Press (Enter) to Restart", 230,500);
        }
		
		g.dispose();
	}

	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{        
			if(playerX >= 600)
			{
				playerX = 600;
			}
			else
			{
				moveRight();
			}
        }
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{          
			if(playerX < 10)
			{
				playerX = 10;
			}
			else
			{
				moveLeft();
			}
        }		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if(!play)
			{
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				map = new MapGenerator(4, 12);
				
				repaint();
			}
        }		
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void moveRight()
	{
		play = true;
		playerX+=20;	
	}
	
	public void moveLeft()
	{
		play = true;
		playerX-=20;	 	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		timer.start();
		if(play)
		{			
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = -2;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
			{
				ballYdir = -ballYdir;
			}
			
			// check map (bricks) collision with the ball
			A: for(int i = 0; i<map.map.length; i++)
			{
				for(int j =0; j<map.map[0].length; j++)
				{				
					if(map.map[i][j] > 0)
					{
						//scores++;
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{					
							map.setBrickValue(0, i, j);
							score+=5;	
							totalBricks--;
							
							// when ball hit right or left of brick
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// when ball hits top or bottom of brick
							else
							{
								ballYdir = -ballYdir;				
							}
							
							break A;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}		
			
			repaint();		
		}
	}
}
