package board;

import java.awt.Color;

/**A Class which represents a playing piece in Chinese Checkers.
 * Each piece is associated with a player, and will affect
 * the underlying board matrix.
 */
public class BoardSpot
{
	private Color color;
	/**Constructor with color assignment.
	 * @param color The color of the piece, representing a player.
	 */
	public BoardSpot(Color color)
	{
		this.color = color;
	}
	
	/**Constructor with color assignment.
	 * @param color The color of the piece, representing a player.
	 */
	public BoardSpot(int color)
	{
		setColor(color);
	}
	
	/**Constructor with null assignment.
	 */
	public BoardSpot()
	{
		this(null);
	}
	
	/**Changes the color of the piece.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**Sets piece to different color, assigning to a different
	 * player.
	 * @param colorCode The color representing player number.
	 */
	public void setColor(int colorCode)
	{
		if (colorCode == 1)
		{
			color = Color.RED;
		}
		else if (colorCode == 2)
		{
			color = Color.ORANGE;
		}
		else if (colorCode == 3)
		{
			color = Color.YELLOW;
		}
		else if (colorCode == 4)
		{
			color = Color.GREEN;
		}
		else if (colorCode == 5)
		{
			color = Color.BLUE;
		}
		else if (colorCode == 6)
		{
			color = Color.MAGENTA;
		}
		else
		{
			throw new IllegalArgumentException("The given colorCode of "
					+ colorCode + " is invalid");
		}
	}

	/**@return The color of the given piece.
	 */
	public Color getColor()
	{
		return color;
	}
}
