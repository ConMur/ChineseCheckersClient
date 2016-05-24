package board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Represents a Chinese Checkers board
 * 
 * @author Connor Murphy
 *
 */
public final class Board {
	private static BoardSpot[][] board;
	private static boolean[][] invalidSpots;

	private static final int SIZE = 17;
	private static final int SPOT_SIZE = 20;

	/**
	 * Creates a Chinese checkers board
	 */
	private Board() {

	}

	public static void init() {
		board = new BoardSpot[SIZE][SIZE];
		invalidSpots = new boolean[SIZE][SIZE];
		setBounds();

		resetBoard();
	}

	public static boolean isValidSpot(int row, int col) {
		if (row >= 0 && col >= 0 && row < SIZE && col < SIZE
				&& !invalidSpots[row][col]) {
			return true;
		}
		return false;
	}

	public static boolean isTaken(int row, int col) {
		if (board[row][col].getColor() == null)
			return false;
		return true;
	}

	public static BoardSpot getSpot(int row, int col) {
		if (isValidSpot(row, col))
			return board[row][col];
		return null;
	}

	/**
	 * Draws the board to the specified graphics context
	 * 
	 * @param g
	 *            the graphics to draw to
	 */
	public static void drawBoard(Graphics g) {
		// Draw each spot
		for (int r = 0; r < board.length; ++r) {
			// Spacing from the left edge of the screen as this is not a 2D
			// array
			int spacing = (board.length - r) * (SPOT_SIZE - 10);

			for (int c = 0; c < board[0].length; ++c) {
				Color color = board[r][c].getColor();

				if (color == null) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(color);
				}

				// Draw only valid spots
				if (!invalidSpots[r][c]) {
					g.fillOval(c * SPOT_SIZE + spacing, r * SPOT_SIZE,
							SPOT_SIZE, SPOT_SIZE);
					g.setFont(new Font("serif", Font.PLAIN, 6));
					g.setColor(Color.BLACK);
					g.drawString(c + "." + r, c * SPOT_SIZE + spacing + 2, r
							* SPOT_SIZE + 15);
				}

			}
		}
	}

	/**
	 * Draws the board in its current state to the console
	 */
	public static void drawBoard() {
		for (int r = 0; r < board.length; ++r) {
			String line = "";
			String space = " ";

			// Add spaces before the line
			for (int i = 0; i < board.length - r; ++i) {
				line += space;
			}

			for (int c = 0; c < board[0].length; ++c) {
				Color color = board[r][c].getColor();
				if (color == null) {
					line += "*";
				} else if (color == Color.RED) {
					line += "r";
				} else if (color == Color.ORANGE) {
					line += "o";
				} else if (color == Color.YELLOW) {
					line += "y";
				} else if (color == Color.GREEN) {
					line += "g";
				} else if (color == Color.BLUE) {
					line += "b";
				} else if (color == Color.MAGENTA) {
					line += "v";
				} else {
					line += "~";
				}
			}
			System.out.println(line);
		}
	}

	/**
	 * Sets the specified place in the board to the given color.
	 * 
	 * @param row
	 *            the row of the board to set
	 * @param col
	 *            the column of the board to set
	 * @param color
	 *            the color of the place. Use null if there is no color in the
	 *            spot
	 * @throws IllegalArgumentException
	 *             if the row that is set is not a valid row
	 */
	public static void set(int row, int col, Color color) {
		if (invalidSpots[row][col])
			throw new IllegalArgumentException(
					"That is an invalid spot! (row: " + row + " col: " + col
							+ ")");

		board[row][col] = new BoardSpot(color);
	}

	public static void set(int row, int col, int color) {
		if (invalidSpots[row][col])
			throw new IllegalArgumentException(
					"That is an invalid spot! (row: " + row + " col: " + col
							+ ")");

		board[row][col] = new BoardSpot(color);
	}

	/**
	 * Sets the spots in the board that are invalid locations
	 */
	private static void setBounds() {
		// upper left
		for (int i = 0; i <= 3; i++)
			for (int j = 0; j <= 3; j++)
				invalidSpots[i][j] = true;
		// lower right
		for (int i = 13; i <= 16; i++)
			for (int j = 13; j <= 16; j++)
				invalidSpots[i][j] = true;
		// upper right
		for (int i = 8; i <= 16; i++)
			for (int j = 0; j <= 3; j++)
				invalidSpots[i][j] = true;
		for (int i = 13; i <= 16; i++)
			for (int j = 4; j <= 8; j++)
				invalidSpots[i][j] = true;
		// lower left
		for (int i = 0; i <= 3; i++)
			for (int j = 8; j <= 12; j++)
				invalidSpots[i][j] = true;
		for (int i = 0; i <= 8; i++)
			for (int j = 13; j <= 16; j++)
				invalidSpots[i][j] = true;
		// hardcode points
		// under blue
		invalidSpots[0][5] = true;
		invalidSpots[0][6] = true;
		invalidSpots[0][7] = true;
		invalidSpots[1][6] = true;
		invalidSpots[1][7] = true;
		invalidSpots[2][7] = true;

		// left of red
		invalidSpots[9][14] = true;
		invalidSpots[9][15] = true;
		invalidSpots[9][16] = true;
		invalidSpots[10][15] = true;
		invalidSpots[10][16] = true;
		invalidSpots[11][16] = true;

		// right of green
		invalidSpots[5][0] = true;
		invalidSpots[6][0] = true;
		invalidSpots[6][1] = true;
		invalidSpots[7][0] = true;
		invalidSpots[7][1] = true;
		invalidSpots[7][2] = true;

		// above orange
		invalidSpots[14][9] = true;
		invalidSpots[15][9] = true;
		invalidSpots[15][10] = true;
		invalidSpots[16][9] = true;
		invalidSpots[16][10] = true;
		invalidSpots[16][11] = true;

	}

	/**
	 * Resets the board to an empty board
	 */
	public static void resetBoard() {
		for (int r = 0; r < board.length; ++r) {
			for (int c = 0; c < board[0].length; ++c) {
				board[r][c] = new BoardSpot();
			}
		}
		/*
		Board.set(9, 4, Color.MAGENTA);
		for (int i = 0; i < 2; ++i) {
			Board.set(10, 4 + i, Color.MAGENTA);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(11, 4 + i, Color.MAGENTA);
		}
		for (int i = 0; i < 4; ++i) {
			Board.set(12, 4 + i, Color.MAGENTA);
		}

		for (int i = 0; i < 4; ++i) {
			Board.set(4, 0 + i, Color.BLUE);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(5, 1 + i, Color.BLUE);
		}
		for (int i = 0; i < 2; ++i) {
			Board.set(6, 2 + i, Color.BLUE);
		}
		Board.set(7, 3, Color.BLUE);

		Board.set(0, 4, Color.GREEN);
		for (int i = 0; i < 2; ++i) {
			Board.set(1, 4 + i, Color.GREEN);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(2, 4 + i, Color.GREEN);
		}
		for (int i = 0; i < 4; ++i) {
			Board.set(3, 4 + i, Color.GREEN);
		}

		for (int i = 0; i < 4; ++i) {
			Board.set(4, 9 + i, Color.YELLOW);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(5, 10 + i, Color.YELLOW);
		}
		for (int i = 0; i < 2; ++i) {
			Board.set(6, 11 + i, Color.YELLOW);
		}
		Board.set(7, 12, Color.YELLOW);

		Board.set(9, 13, Color.ORANGE);
		for (int i = 0; i < 2; ++i) {
			Board.set(10, 13 + i, Color.ORANGE);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(11, 13 + i, Color.ORANGE);
		}
		for (int i = 0; i < 4; ++i) {
			Board.set(12, 13 + i, Color.ORANGE);
		}

		for (int i = 0; i < 4; ++i) {
			Board.set(13, 9 + i, Color.RED);
		}
		for (int i = 0; i < 3; ++i) {
			Board.set(14, 10 + i, Color.RED);
		}
		for (int i = 0; i < 2; ++i) {
			Board.set(15, 11 + i, Color.RED);
		}
		Board.set(16, 12, Color.RED);*/
	}

}
