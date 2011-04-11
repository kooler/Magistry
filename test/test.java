package test;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import forms.addCityFrame;

public class test {
	public static Processor processor;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(500, 300);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("Data");
		menuBar.add(menu);
		JMenuItem addCity = new JMenuItem("Add city");
		addCity.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				new addCityFrame(processor, null);
			}
		});
		menu.add(addCity);
		mainFrame.setJMenuBar(menuBar);
		
		JPanel canvas = new JPanel();
		canvas.setBackground(Color.WHITE);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				new addCityFrame(processor, new Point(e.getPoint()));
			}
		});
		
		mainFrame.add(canvas);
		
		processor = new Processor();
		processor.setCanvas(canvas);
		
		mainFrame.show();
	}

}
