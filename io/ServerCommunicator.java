package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import board.Board;

/**
 * The way to communicate with the server. It listens to incoming commands and
 * responds to them as needed
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
		running = true;

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
			in.close();
			out.close();
			client.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void update()
	{
		//Get the highest priority message from the server
		Message message = inQueue.poll();

		//Ensure there is a message to process
		if(message != null) {
			ServerCommand cmd = message.getCommand();
			StringTokenizer tokenizer = new StringTokenizer(message.getMessage());

			if (cmd == ServerCommand.MOVE) {
				int oldRow = Integer.parseInt(tokenizer.nextToken());
				int oldCol = Integer.parseInt(tokenizer.nextToken());
				int newRow = Integer.parseInt(tokenizer.nextToken());
				int newCol = Integer.parseInt(tokenizer.nextToken());

				// TODO: Update board
			} else if (cmd == ServerCommand.NEW_GAME) {
				int colour = Integer.parseInt(tokenizer.nextToken());

				// TODO: set the colour
			} else if (cmd == ServerCommand.PLACE_PIECE) {
				int colour = Integer.parseInt(tokenizer.nextToken());
				int row = Integer.parseInt(tokenizer.nextToken());
				int col = Integer.parseInt(tokenizer.nextToken());

				Board.set(row, col, colour);
			} else if (cmd == ServerCommand.YOUR_TURN) {
				// TODO: start AI looking
			} else if (cmd == ServerCommand.INVALID_MOVE) {
				System.err.println("Invalid move made");
			} else if (cmd == ServerCommand.TIMEOUT) {
				System.err.println("Timeout");
			} else if (cmd == ServerCommand.WIN) {
				// TODO: win
			} else {
				System.err.println("This line should never be reached");
			}
		}

		//Send the highest priority message to the server
		Message toSend = outQueue.poll();

		//Ensure there is a message to process
		if(toSend != null)
		{
			try {
				out.write(toSend.getMessage());
				out.flush();
			}
			catch(IOException ioe)
			{
				System.err.println("Error sending a message to the server");
				ioe.printStackTrace();
			}
		}

	}

	/**
	 * A thread that listens to the server commands and processes them as
	 * necessary
	 * @author Connor Murphy
	 *
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
				String line;
				StringTokenizer tokenizer;
				try
				{
					line = in.readLine();
					tokenizer = new StringTokenizer(line);
				}
				catch (NumberFormatException | IOException e)
				{
					e.printStackTrace();
					return;
				}

				int cmdNo = Integer.parseInt(tokenizer.nextToken());

				// Change the numerical command into the corresponding enum
				// value
				ServerCommand cmd;
				try {
					 cmd = ServerCommand.values()[cmdNo];
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					throw new RuntimeException("The command number, " + cmdNo + " is invalid");
				}

				if(cmd == ServerCommand.INVALID_MOVE || cmd == ServerCommand.TIMEOUT) {
					inQueue.add(new Message(line, cmd, Priority.HIGH));
				}
				else
				{
					inQueue.add(new Message(line, cmd, Priority.NORMAL));
				}
			}
		}

	}
}
