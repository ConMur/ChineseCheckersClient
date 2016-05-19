package gui;

import io.ServerCommunicator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import board.Board;

/**
 * The panel to display the events of the Chinese Checkers game going on
 *
 * @author Connor Murphy
 */
public class CCPanel extends JPanel
{
	private boolean running;
	private ServerCommunicator communicator;

	private String ip = "10.242.171.255";
	private int port = 5050;

	/**
	 * Sets up this panel and the necessary classes to play this Chinese
	 * Checkers game
	 */
	public CCPanel()
	{
		super();

		// Get Ip and port
		String ipString = null; // JOptionPane.showInputDialog("Enter the ip");
		String portString = null; // JOptionPane.showInputDialog("Enter the port");
		if (ipString != null)
		{
			ip = ipString;
		}

		if (portString != null)
		{
			port = Integer.parseInt(portString);
		}

		setPreferredSize(new Dimension(600, 400));
		Board.init();

		
		
		running = true;
		// TODO: uncomment when testing with server
		communicator = new ServerCommunicator(ip, port);
		communicator.start();
	}

	/**
	 * Starts the drawing of the panel
	 */
	public void go()
	{
		// Create and start a thread that refreshes the gui every so often
		Thread drawThread = new Thread(new DrawThread());
		drawThread.setName("Draw Thread");
		drawThread.start();

		// Process server incoming and out going messages
		while (running)
		{
			// TODO: uncomment when testing
			communicator.update();
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

	@Override
	public void paintComponent(Graphics g)
	{
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Board.drawBoard(g);

	}

	/**
	 * Draws the board to the screen
	 * @author Connor Murphy
	 *
	 */
	class DrawThread implements Runnable
	{
		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					Thread.sleep(15);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				repaint();
			}
		}
	}
}
