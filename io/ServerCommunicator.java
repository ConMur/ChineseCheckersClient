package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import board.Board;
import board.BoardSpot;

/**
 * The way to communicate with the server. It listens to incoming commands and
 * responds to them as needed
 *
 * @author Connor Murphy
 */
public class ServerCommunicator
{
	private Socket client;
	private boolean running;

	private BufferedReader in;
	private BufferedWriter out;

	private PriorityQueue<Message> inQueue;
	private PriorityQueue<Message> outQueue;

	/**
	 * Sets up the streams needed to communicate with the server
	 *
	 * @param ip the ip of the server
	 * @param port the port the server is on
	 */
	public ServerCommunicator(String ip, int port)
	{
		try
		{
			client = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					client.getOutputStream()));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		inQueue = new PriorityQueue<>();
		outQueue = new PriorityQueue<>();

		start();
	}

	/**
	 * Starts a thread that listens to the server for commands
	 */
	public void start()
	{
		// Ensure there are not multiple listeners
		if (running)
			return;

		Thread t = new Thread(new ListeningThread());
		t.setName("Server Listening Thread");
		t.start();
		running = true;
	}

	/**
	 * Stops listening to the server for commands
	 */
	public void stop()
	{
		running = false;
	}

	/**
	 * Closes all streams in use
	 */
	public void close()
	{
		try
		{
			client.close();
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void update()
	{
		// Get the highest priority message from the server
		Message message = null;
		if (!inQueue.isEmpty())
		{
			message = inQueue.remove();
		}

		// Ensure there is a message to process
		if (message != null)
		{
			ServerCommand cmd = message.getCommand();
			StringTokenizer tokenizer = new StringTokenizer(
					message.getMessage());
			
			//TODO: remove when not testing
			System.out.println(cmd + ": " + message.getMessage());

			try
			{
				if (cmd == ServerCommand.MOVE)
				{
					int oldRow = Integer.parseInt(tokenizer.nextToken());
					int oldCol = Integer.parseInt(tokenizer.nextToken());
					int newRow = Integer.parseInt(tokenizer.nextToken());
					int newCol = Integer.parseInt(tokenizer.nextToken());

					BoardSpot spot = Board.getSpot(oldRow, oldCol);
					Board.set(oldRow, oldCol, null);
					Board.set(newRow, newCol, spot.getColor());

				}
				else if (cmd == ServerCommand.NEW_GAME)
				{
					int colour = Integer.parseInt(tokenizer.nextToken());
					Board.resetBoard();
					// AI.reset();
					// AI.setColor(colour);
					// TODO: set the colour
				}
				else if (cmd == ServerCommand.PLACE_PIECE)
				{
					int colour = Integer.parseInt(tokenizer.nextToken());
					int row = Integer.parseInt(tokenizer.nextToken());
					int col = Integer.parseInt(tokenizer.nextToken());

					Board.set(row, col, colour);
				}
				else if (cmd == ServerCommand.YOUR_TURN)
				{
					// TODO: start AI looking
					/*Move move = AI.getMove()
					 * String message = "1 " + move.getOldRow() + " " + move.getOldCol() + " " + move.getNewRow() + " " + move.getNewCol();
					 * outQueue.add(new Message(message,ServerCommand.MOVE, Prioirty.Normal);
					 */
				}
				else if (cmd == ServerCommand.INVALID_MOVE)
				{
					System.err.println("Invalid move made");
				}
				else if (cmd == ServerCommand.TIMEOUT)
				{
					System.err.println("Timeout");
				}
				else if (cmd == ServerCommand.WIN)
				{
					int colorCode = Integer.parseInt(tokenizer.nextToken());
					String color = "";
					if (colorCode == 1)
					{
						color = "Red";
					}
					else if (colorCode == 2)
					{
						color = "Orange";
					}
					else if (colorCode == 3)
					{
						color = "Yellow";
					}
					else if (colorCode == 4)
					{
						color = "Green";
					}
					else if (colorCode == 5)
					{
						color = "Blue";
					}
					else if (colorCode == 6)
					{
						color = "Purple";
					}
					System.out.println(color + " won!");
				}
				else
				{
					System.err.println("This line should never be reached");
				}
			}
			catch (NoSuchElementException e)
			{
				System.err
						.println("Attempted to read more tokens then there was");
				e.printStackTrace();
			}
		}

		// Send the highest priority message to the server
		message = null;
		if (!inQueue.isEmpty())
		{
			message = inQueue.remove();
		}

		// Ensure there is a message to process
		if (message != null)
		{
			try
			{
				out.write(message.getMessage() + "/n");
				out.flush();
			}
			catch (IOException ioe)
			{
				System.err.println("Error sending a message to the server");
				ioe.printStackTrace();
			}
		}

	}

	/**
	 * A thread that listens to the server commands and processes them as
	 * necessary
	 *
	 * @author Connor Murphy
	 */
	class ListeningThread implements Runnable
	{

		@Override
		/**
		 * Listens for server commands and adds them to the queue
		 */
		public void run()
		{
			while (running)
			{
				String line = null;
				StringTokenizer tokenizer = null;
				try
				{
					line = in.readLine();
					System.out.println("READ LINE");
					tokenizer = new StringTokenizer(line);
				}
				catch (NumberFormatException | IOException e)
				{
					if (running)
					{
						e.printStackTrace();
						return;
					}
					// else it is a thread trying to do one last loop
				}

				int cmdNo = Integer.parseInt(tokenizer.nextToken());

				// Change the numerical command into the corresponding enum
				// value
				ServerCommand cmd;
				try
				{
					cmd = ServerCommand.values()[cmdNo - 1];
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					throw new RuntimeException("The command number, " + cmdNo
							+ " is invalid");
				}

				// Make the message everything but the server command
				String message = line.substring(2);

				if (cmd == ServerCommand.INVALID_MOVE
						|| cmd == ServerCommand.TIMEOUT)
				{
					inQueue.add(new Message(message, cmd, Priority.HIGH));
				}
				else
				{
					inQueue.add(new Message(message, cmd, Priority.NORMAL));
				}
			}
		}
	}
}
