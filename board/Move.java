package board;

/**
 * Represents a move on a chinese checkers board
 * @author Connor Murphy
 *
 */
public class Move
{
	private int oldRow, oldCol, newRow, newCol;
	
	public Move(int oldRow, int oldCol, int newRow, int newCol)
	{
		this.oldRow = oldRow;
		this.oldCol = oldCol;
		this.newRow = newCol;
		this.newCol = newCol;
	}

	public int getOldRow()
	{
		return oldRow;
	}

	public int getOldCol()
	{
		return oldCol;
	}

	public int getNewRow()
	{
		return newRow;
	}

	public int getNewCol()
	{
		return newCol;
	}
	
	
}
