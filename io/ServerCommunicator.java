package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import ai.AI;
import board.Board;
import board.BoardSpot;
import board.Move;

/**
 * The way to communicate with the server. It listens to incoming commands and
 * responds to them as needed
 *
 * @author Connor Murphy
 */
public class ServerCommunicator {
	private Socket client;
	private boolean running;

	private BufferedReader in;
	private PrintWriter out;

	private PriorityQueue<Message> inQueue;
	private PriorityQueue<Message> outQueue;

	/**
	 * Sets up the streams needed to communicate with the server
	 *
	 * @param ip
	 *            the ip of the server
	 * @param port
	 *            the port the server is on
	 */
	public ServerCommunicator(String ip, int port) {
		try {
			client = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		inQueue = new PriorityQueue<>();
		outQueue = new PriorityQueue<>();

		start();
	}

	/**
	 * Starts a thread that listens to the server for commands
	 */
	public void start() {
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
	public void stop() {
		running = false;
	}

	/**
	 * Closes all streams in use
	 */
	public void close() {
		try {
			client.close();
			in.close();
			out.close();
		}catch(NullPointerException e){
		//do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		// Get the highest priority message from the server
		Message message = null;

		synchronized (inQueue) {
			if (!inQueue.isEmpty()) {
				message = inQueue.remove();
			}
		}

		// Ensure there is a message to process
		if (message != null) {
			ServerCommand cmd = message.getCommand();
			StringTokenizer tokenizer = new StringTokenizer(
					message.getMessage());

			// TODO: remove when not testing
			System.out.println(cmd + ": " + message.getMessage());

			try {
				if (cmd == ServerCommand.MOVE) {
					int oldRow = Integer.parseInt(tokenizer.nextToken());
					int oldCol = Integer.parseInt(tokenizer.nextToken());
					int newRow = Integer.parseInt(tokenizer.nextToken());
					int newCol = Integer.parseInt(tokenizer.nextToken());

					BoardSpot spot = Board.getSpot(oldRow, oldCol);
					Board.set(oldRow, oldCol, null);
					Board.set(newRow, newCol, spot.getColor());

					// TODO
					if (spot.getColor() == AI.getColor()) {
						Move move = new Move(oldRow, oldCol, newRow, newCol);
						AI.movePiece(move);
					}

				} else if (cmd == ServerCommand.NEW_GAME) {
					int colour = Integer.parseInt(tokenizer.nextToken());
					Board.resetBoard();
					AI.start(colour);
					// TODO: set the colour
				} else if (cmd == ServerCommand.PLACE_PIECE) {
					int colour = Integer.parseInt(tokenizer.nextToken());
					int row = Integer.parseInt(tokenizer.nextToken());
					int col = Integer.parseInt(tokenizer.nextToken());

					Board.set(row, col, colour);
				} else if (cmd == ServerCommand.YOUR_TURN) {
					// TODO: start AI looking
					System.out.println("started move search");
					Move move = AI.getMove();
					// AI.movePiece(move);
					if (move == null)
						System.out.println("asdfsa");
					System.out.println("end move search");
					String msg = "1 " + move.getOldRow() + " "
							+ move.getOldCol() + " " + move.getNewRow() + " "
							+ move.getNewCol();

					// TODO: remove when not testing
					// Board.set(move.getOldRow(), move.getOldCol(), null);
					// Board.set(move.getNewRow(), move.getNewCol(),
					// Color.BLUE);

					outQueue.add(new Message(msg, ServerCommand.MOVE,
							Priority.NORMAL));

				} else if (cmd == ServerCommand.INVALID_MOVE) {
					System.err.println("Invalid move made");
				} else if (cmd == ServerCommand.TIMEOUT) {
					System.err.println("Timeout");
				} else if (cmd == ServerCommand.WIN) {
					int colorCode = Integer.parseInt(tokenizer.nextToken());
					String color = "";
					if (colorCode == 1) {
						color = "Red";
					} else if (colorCode == 2) {
						color = "Orange";
					} else if (colorCode == 3) {
						color = "Yellow";
					} else if (colorCode == 4) {
						color = "Green";
					} else if (colorCode == 5) {
						color = "Blue";
					} else if (colorCode == 6) {
						color = "Purple";
					}
					System.out.println(color + " won!");
				} else {
					System.err.println("This line should never be reached");
				}
			} catch (NoSuchElementException e) {
				System.err
						.println("Attempted to read more tokens then there was");
				e.printStackTrace();
			}
		}

		// Send the highest priority message to the server
		message = null;
		synchronized (outQueue) {
			if (!outQueue.isEmpty()) {
				message = outQueue.remove();
			}
		}

		// Ensure there is a message to process
		if (message != null) {
			out.println(message.getMessage());
			out.flush();
		}

	}

	/**
	 * A thread that listens to the server commands and processes them as
	 * necessary
	 *
	 * @author Connor Murphy
	 */
	class ListeningThread implements Runnable {

		@Override
		/**
		 * Listens for server commands and adds them to the queue
		 */
		public void run() {
			while (running) {
				String line = null;
				StringTokenizer tokenizer = null;
				try {
					line = in.readLine();
					System.out.println("READ LINE: " + line);
					tokenizer = new StringTokenizer(line);
				} catch (NumberFormatException | IOException e) {
					if (running) {
						e.printStackTrace();
						return;
					}
					// else it is a thread trying to do one last loop
				}

				int cmdNo = Integer.parseInt(tokenizer.nextToken());

				// Change the numerical command into the corresponding enum
				// value
				ServerCommand cmd = null;
				try {
					cmd = ServerCommand.values()[cmdNo - 1];
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Nonexistant server command...");
				}

				// Make the message everything but the server command
				String message = "";
				while (tokenizer.hasMoreTokens()) {
					message += tokenizer.nextToken() + " ";
				}

				if (cmd == ServerCommand.INVALID_MOVE
						|| cmd == ServerCommand.TIMEOUT) {
					synchronized (inQueue) {
						inQueue.add(new Message(message, cmd, Priority.HIGH));

					}
				} else {
					synchronized (inQueue) {
						inQueue.add(new Message(message, cmd, Priority.NORMAL));

					}
				}
			}
		}
	}
}
