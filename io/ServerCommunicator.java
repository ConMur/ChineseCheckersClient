package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class ServerCommunicator
{
	private Socket client;
	private boolean running;

	private BufferedReader in;
	private BufferedWriter out;

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
		start();
	}

	/**
	 * Reads in server commands and processes them as needed
	 */
	private void processServerCommands()
	{
		int cmdNo = -1;
		StringTokenizer tokenizer = null;

		try
		{
			String line = in.readLine();
			tokenizer = new StringTokenizer(line);
		}
		catch (NumberFormatException | IOException e)
		{
			e.printStackTrace();
			return;
		}

		cmdNo = Integer.parseInt(tokenizer.nextToken());

		// Change the numerical command into the corresponding enum value
		ServerCommand cmd = ServerCommand.values()[cmdNo];

		if (cmd == ServerCommand.MOVE)
		{
			int oldRow = Integer.parseInt(tokenizer.nextToken());
			int oldCol = Integer.parseInt(tokenizer.nextToken());
			int newRow = Integer.parseInt(tokenizer.nextToken());
			int newCol = Integer.parseInt(tokenizer.nextToken());

			// TODO: Update board
		}
		else if (cmd == ServerCommand.NEW_GAME)
		{
			int colour = Integer.parseInt(tokenizer.nextToken());

			// TODO: set the colour
		}
		else if (cmd == ServerCommand.PLACE_PIECE)
		{
			// Read in the whole board
			while (tokenizer.hasMoreTokens())
			{
				if (!tokenizer.nextToken().equals(String.valueOf(3)))
				{
					//The command is not to place a piece so exit
					break;
				}
				int row = Integer.parseInt(tokenizer.nextToken());
				int col = Integer.parseInt(tokenizer.nextToken());
				
				// TODO: update board
			}

			
		}
		else if (cmd == ServerCommand.YOUR_TURN)
		{
			//TODO: start AI looking
		}
		else if (cmd == ServerCommand.INVALID_MOVE)
		{
			System.err.println("Invalid move made");
		}
		else if (cmd == ServerCommand.WIN)
		{
			//TODO: win
		}
		else
		{
			System.err.println("Unknown server command: " + cmdNo);
		}
	}

	/**
	 * Starts listening to the server for commands
	 */
	public void start()
	{
		// Ensure there are not multiple listeners
		if (running)
			return;

		Thread t = new Thread(new ListeningThread());
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

	class ListeningThread implements Runnable
	{

		@Override
		public void run()
		{
			while (running)
			{
				processServerCommands();
			}
		}

	}
}