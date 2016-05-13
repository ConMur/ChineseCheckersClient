package gui;

import io.ServerCommunicator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import board.Board;

/**
 * The panel to display the events of the Chinese Checkers game going on
 * @author Connor Murphy
 *
 */
public class CCPanel extends JPanel
{
	private Board board;
	private boolean running;
	private ServerCommunicator communicator;
	
	private final String IP = "127.0.0.1";
	private final int PORT = 5000;
	
	private Graphics g;
	
	/**
	 * Sets up this panel and the nessecary classes to play this Chinese Checkers game
	 */
	public CCPanel()
	{
		super();
		setPreferredSize(new Dimension(600, 400));
		board = new Board();
		running = true;
		//TODO: uncomment when testing with server
		//communicator = new ServerCommunicator(IP, PORT);
		//communicator.start();
	}
	
	/**
	 * Starts the drawing of the panel
	 */
	public void go()
	{
		g = this.getGraphics();
		
		while(running)
		{
			//Draw background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			board.drawBoard(g);
			try
			{
				Thread.sleep(15);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stops drawing the panel and closes any server-client connections 
	 */
	public void stop()
	{
		running = false;
		communicator.stop();
		communicator.close();
	}
}
