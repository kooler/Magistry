package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import sun.reflect.generics.tree.BottomSignature;

import data.City;

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
		mainFrame.setLayout(new BorderLayout());
		JPanel canvas = new JPanel();
		canvas.setBackground(Color.WHITE);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				City c = processor.getCity(e.getPoint());
				if (c == null) {
					new addCityFrame(processor, e.getPoint());
				} else {
					if (!processor.selectionStarted()) {
						if (c.isSelected()) {
							processor.deselectAllCities();
						} else {
							City selected = processor.getSelectedCity();
							if (selected != null) {
								processor.connectCities(c, selected);
								processor.deselectAllCities();
							} else {
								processor.selectCity(c);
							}
						}
					} else {
						processor.addToSelection(c);
					}
				}
			}
		});
		
		mainFrame.add(canvas, BorderLayout.CENTER);
		JButton start = new JButton("Start");
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				processor.startSelection();
			}
		});
		JButton reset = new JButton("Redraw/Reset selection");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				Point p1 = new Point(50,50);
				Point p2 = new Point(150,50);
				Point p3 = new Point(250,50);
				Point p4 = new Point(350,50);
				Point p5 = new Point(100,150);
				Point p6 = new Point(200,150);
				Point p7 = new Point(300,150);
				Point p8 = new Point(50,250);
				Point p9 = new Point(150,250);
				Point p10 = new Point(250,250);
				processor.addCity("1", p1);
				processor.addCity("2", p2);
				processor.addCity("3", p3);
				processor.addCity("4", p4);
				processor.addCity("5", p5);
				processor.addCity("6", p6);
				processor.addCity("7", p7);
				processor.addCity("8", p8);
				processor.addCity("9", p9);
				processor.addCity("10", p10);
				City c1 = processor.getCity(p1);
				City c2 = processor.getCity(p2);
				City c3 = processor.getCity(p3);
				City c4 = processor.getCity(p4);
				City c5 = processor.getCity(p5);
				City c6 = processor.getCity(p6);
				City c7 = processor.getCity(p7);
				City c8 = processor.getCity(p8);
				City c9 = processor.getCity(p9);
				City c10 = processor.getCity(p10);
				processor.connectCities(c1, c2);
				processor.connectCities(c1, c5);
				processor.connectCities(c2, c3);
				processor.connectCities(c2, c5);
				processor.connectCities(c2, c6);
				//processor.connectCities(c2, c7);
				processor.connectCities(c3, c5);
				processor.connectCities(c3, c6);
				processor.connectCities(c3, c7);
				processor.connectCities(c3, c4);
				processor.connectCities(c4, c5);
				processor.connectCities(c4, c6);
				processor.connectCities(c4, c7);
				processor.connectCities(c5, c6);
				processor.connectCities(c6, c7);
				processor.connectCities(c7, c8);
				processor.connectCities(c7, c9);
				processor.connectCities(c7, c10);
				processor.connectCities(c8, c9);
				processor.connectCities(c8, c10);
				processor.connectCities(c9, c10);
				processor.connectCities(c1, c7);
				//processor.connectCities(c2, c7);
				processor.connectCities(c3, c7);
				processor.connectCities(c4, c7);
				//processor.connectCities(c5, c7);
				processor.connectCities(c6, c7);
				//processor.connectCities(c1, c8);
				processor.connectCities(c2, c8);
				processor.connectCities(c3, c8);
				processor.connectCities(c4, c8);
				//processor.connectCities(c5, c8);
				processor.connectCities(c6, c8);
				//processor.connectCities(c1, c9);
				//processor.connectCities(c2, c9);
				//processor.connectCities(c3, c9);
				processor.connectCities(c4, c9);
				//processor.connectCities(c5, c9);
				processor.connectCities(c6, c9);
				//processor.connectCities(c1, c10);
				//processor.connectCities(c2, c10);
				//processor.connectCities(c3, c10);
				processor.connectCities(c4, c10);
				//processor.connectCities(c5, c10);
				processor.connectCities(c6, c10);
				
			}
		});
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		buttons.add(start, BorderLayout.EAST);
		buttons.add(reset, BorderLayout.WEST);
		mainFrame.add(buttons, BorderLayout.SOUTH);
		
		processor = new Processor();
		processor.setCanvas(canvas);
		
		mainFrame.show();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
	}

}
