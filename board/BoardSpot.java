package board;

import java.awt.Color;

public class BoardSpot
{
	private Color color;

	public BoardSpot(Color color)
	{
		this.color = color;
	}
	
	public BoardSpot(int color)
	{
		setColor(color);
	}

	public BoardSpot()
	{
		this(null);
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}

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

	public Color getColor()
	{
		return color;
	}
}
