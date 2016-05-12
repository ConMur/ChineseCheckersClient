package main;

import gui.CCPanel;

import javax.swing.JFrame;

public class Main
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Chinese Checkers AI");
		CCPanel panel = new CCPanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
