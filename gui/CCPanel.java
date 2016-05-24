package gui;

import io.ServerCommunicator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import board.Board;
import board.Move;

/**
 * The panel to display the events of the Chinese Checkers game going on. This
 * is also the client side of the Chinese Checkers networking system.
 * @author Connor M. (main)
 */
@SuppressWarnings("serial")
public class CCPanel extends JPanel
{
	// CCPanel fields.
	private boolean running;
	private ServerCommunicator communicator;

	private String ip = "10.242.171.106";
	private int port = 421;
	
	//Stats
	private static Move lastMove;
	private static Move ourLastMove;
	private static int numTimeouts;
	private static int numInvalidMoves;
	/**
	 * Sets up this panel and the necessary classes to play this Chinese
	 * Checkers game
	 */
	@SuppressWarnings("unused")
	public CCPanel()
	{
		super();

		// Get IP and port
		String ipString = null; // JOptionPane.showInputDialog("Enter the ip");
		String portString = null; // JOptionPane.showInputDialog("Enter the port");
		if (ipString != null)
			ip = ipString;

		if (portString != null)
			port = Integer.parseInt(portString);

		// Initiate board.
		setPreferredSize(new Dimension(600, 400));
		Board.init();
		running = true;
		communicator = new ServerCommunicator(ip, port);
		communicator.start();
		
		lastMove = null;
		ourLastMove = null;
		numTimeouts = 0;
		numInvalidMoves = 0;
	}

	/* NETWORK METHODS */

	/**
	 * Starts the drawing of the panel.
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
	 * Stops drawing the panel and closes any server-client connections.
	 */
	public boolean stop()
	{
		try
		{
			/* CONNECTION CLOSED */
			running = false;
			communicator.stop();
			communicator.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Failed to close connection!");
			e.printStackTrace();
			return false;
		}
	}

	/* GRAPHICS METHODS */

	@Override
	public void paintComponent(Graphics g)
	{
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Board.drawBoard(g);

		// TODO: draw: move made by us, last move made by games, number of
		// invalid moves, number of timeouts
		g.setFont(new Font("serif", Font.BOLD, 14));
		g.setColor(Color.WHITE);
		g.drawString("Move made: " + lastMove, 0, 10);
		g.drawString("Last Move: " + ourLastMove, 0, 24);
		g.drawString("Invalid moves: " + numInvalidMoves, 0, 38);
		g.drawString("Number Timeouts: " + numTimeouts, 0, 52);
	}
	
	public static void setLastMove(Move lastMove)
	{
		CCPanel.lastMove = lastMove;
	}

	public static void setOurLastMove(Move ourLastMove)
	{
		CCPanel.ourLastMove = ourLastMove;
	}

	public static void addTimeout()
	{
		numTimeouts++;
	}

	public static void addNumInvalidMoves()
	{
		numInvalidMoves++;
	}



	/**
	 * Thread class. Draws the board to the screen
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
				// Repaint every time.
				repaint();
			}
		}
	}
}
