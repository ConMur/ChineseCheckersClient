package board;

/**
 * Represents a move on a chinese checkers board
 * 
 * @author Connor Murphy
 *
 */
public class Move {
	private int oldRow, oldCol, newRow, newCol;

	/**
	 * Creates a move
	 * 
	 * @param oldRow
	 *            the old row of the piece before moving it
	 * @param oldCol
	 *            the old column of the piece before moving it
	 * @param newRow
	 *            the new row of the piece after moving it
	 * @param newCol
	 *            the new column of the piece after moving it
	 */
	public Move(int oldRow, int oldCol, int newRow, int newCol) {
		this.oldRow = oldRow;
		this.oldCol = oldCol;
		this.newRow = newRow;
		this.newCol = newCol;
	}

	public int getOldRow() {
		return oldRow;
	}

	public int getOldCol() {
		return oldCol;
	}

	public int getNewRow() {
		return newRow;
	}

	public int getNewCol() {
		return newCol;
	}

	public String toString() {
		return oldCol + " " + oldRow + " " + newCol + " " + newRow;
	}
}
