package main;

import gui.CCPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.JFrame;

public class Main
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Chinese Checkers AI");
		CCPanel panel = new CCPanel();
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				panel.stop();
				frame.dispose();
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
				for(Thread t : threadArray)
				{
					System.out.println(t.getName());

				}
			}
		});
		
		frame.setVisible(true);
		panel.go();

		System.out.println("-----------------");
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		for(Thread t : threadArray)
		{
			System.out.println(t.getName());

		}
	}
}
