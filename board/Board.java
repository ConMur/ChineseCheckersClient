package board;

import java.awt.Color;
import java.awt.Graphics;

public class Board
{
	private BoardSpot[][] board;
	private boolean[][] invalidSpots;

	private final int SIZE = 17;

	/**
	 * Creates a Chinese checkers board of the given size
	 * @param size the size of the board
	 */
	public Board()
	{
		board = new BoardSpot[SIZE][SIZE];
		invalidSpots = new boolean[SIZE][SIZE];
		setBounds();
		
		resetBoard();
	}

	/**
	 * Draws the board in its current state
	 */
	public void drawBoard(Graphics g)
	{
		for (int r = 0; r < board.length; ++r)
		{
			String line = "";
			String space = " ";

			// Add spaces before the line
			for (int i = 0; i < board.length - r; ++i)
			{
				line += space;
			}

			for (int c = 0; c < board[0].length; ++c)
			{
				Color color = board[r][c].getColor();
				if (color == null)
				{
					line += "*";
				}
				else if (color == Color.RED)
				{
					line += "r";
				}
				else if (color == Color.ORANGE)
				{
					line += "o";
				}
				else if (color == Color.YELLOW)
				{
					line += "y";
				}
				else if (color == Color.GREEN)
				{
					line += "g";
				}
				else if (color == Color.BLUE)
				{
					line += "b";
				}
				else if (color == Color.MAGENTA)
				{
					line += "v";
				}
			}
			System.out.println(line);
		}
	}

	/**
	 * Sets the specified place in the board to the given color.
	 * @param row the row of the board to set
	 * @param col the column of the board to set
	 * @param color the color of the place. Use null if there is no color in the
	 *            spot
	 */
	public void set(int row, int col, Color color)
	{
		board[row][col] = new BoardSpot(color);
	}

	public void setBounds()
	{
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
			for (int j = 0; j <= 4; j++)
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

	private void resetBoard()
	{
		for (int r = 0; r < board.length; ++r)
		{
			for (int c = 0; c < board[0].length; ++c)
			{
				board[r][c] = new BoardSpot();
			}
		}
	}

}
