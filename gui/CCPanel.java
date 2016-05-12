package gui;

import io.ServerCommunicator;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import board.Board;

public class CCPanel extends JPanel
{
	private Board board;
	private boolean running;
	private ServerCommunicator communicator;
	
	private final String IP = "127.0.0.1";
	private final int PORT = 5000;
	
	private Graphics g;
	
	public CCPanel()
	{
		super();
		setPreferredSize(new Dimension(600, 400));
		board = new Board();
		running = true;
		communicator = new ServerCommunicator(IP, PORT);
		communicator.start();
		g = this.getGraphics();
	}
	
	public void go()
	{
		while(running)
		{
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
	
	public void stop()
	{
		running = false;
	}
	
	public void restart()
	{
		running = true;
	}
}
