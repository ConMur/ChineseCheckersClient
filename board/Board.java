package board;

import java.awt.Color;

public class Board
{
	private BoardSpot[][] board;
	
	/**
	 * Creates a Chinese checkers board of the given size
	 * @param size the size of the board
	 */
	public Board(int size)
	{
		board = new BoardSpot[size][size];
		
		resetBoard();
	}
	
	/**
	 * Draws the board in its current state
	 */
	public void drawBoard()
	{
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board[0].length; ++c)
			{
				String line = "";
				
				String space = " ";
				
				//Add spaces before the line
				for(int i = 0; i < board.length - r; ++i)
				{
					line += space;
				}
				
				Color color = board[r][c].getColor();
				if(color == null)
				{
					line += "*";
				}
				else if(color == Color.RED)
				{
					line += "r";
				}
				else if(color == Color.ORANGE)
				{
					line += "o";
				}
				else if(color == Color.YELLOW)
				{
					line += "y";
				}
				else if(color == Color.GREEN)
				{
					line += "g";
				}
				else if(color == Color.BLUE)
				{
					line += "b";
				}
				else if(color == Color.MAGENTA)
				{
					line += "v";
				}
				System.out.println(line);
			}
		}
	}
	
	/**
	 * Sets the specified place in the board to the given color. 
	 * @param row the row of the board to set
	 * @param col the column of the board to set
	 * @param color the color of the place. Use null if there is no color in the spot
	 */
	public void set(int row, int col, Color color)
	{
		board[row][col] = new BoardSpot(color);
	}
	
	private void resetBoard()
	{
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board[0].length; ++c)
			{
				board[r][c] = new BoardSpot();
			}
		}
	}
	
	
}
