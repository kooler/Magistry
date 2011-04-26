package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import data.City;
import data.Connection;
import forms.editConnectionFrame;
import forms.addCityFrame;

public class test {
	public static Processor processor;
	static JComboBox criteria;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame mainFrame = new JFrame();
		mainFrame.setSize(500, 300);
		
		final JFileChooser fileChooser = new JFileChooser();
		
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
				Connection con = processor.getConnection(e.getPoint());
				if (c == null && con == null) {
					new addCityFrame(processor, e.getPoint());
				} else if (con != null) {
					System.out.println("Selected connection: " + con);
					new editConnectionFrame(processor, con);
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
				processor.startSelection(criteria.getSelectedItem().toString());
			}
		});
		JButton reset = new JButton("Redraw/Reset selection");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				processor.redraw();
			}
		});
		JPanel buttons = new JPanel();
		buttons.setLayout(new BorderLayout());
		buttons.add(start, BorderLayout.EAST);
		buttons.add(reset, BorderLayout.WEST);
		JPanel opensave = new JPanel();
		buttons.add(opensave, BorderLayout.CENTER);
		JButton open = new JButton("Open");
		JButton save = new JButton("Save");
		opensave.setLayout(new BorderLayout());
		opensave.add(open, BorderLayout.EAST);
		opensave.add(save, BorderLayout.WEST);
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				int returnVal = fileChooser.showSaveDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            FileOutputStream fout;
					try {
						System.out.println("Saving to file: " + file.getCanonicalPath());
						fout = new FileOutputStream(file.getCanonicalPath());
						ObjectOutputStream oos = new ObjectOutputStream(fout);
			            oos.writeObject(processor.getCities());
			            oos.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
			}
		});
		open.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				int returnVal = fileChooser.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            FileInputStream fout;
					try {
						System.out.println("Opening: " + file.getCanonicalPath());
						fout = new FileInputStream(file.getCanonicalPath());
						ObjectInputStream oos = new ObjectInputStream(fout);
						processor.setCities((ArrayList<City>) oos.readObject());
			            oos.close();
			            processor.redraw();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
			}
		});
		mainFrame.add(buttons, BorderLayout.SOUTH);
		
		//Criteria
		JPanel criteriaPanel = new JPanel();
		criteriaPanel.setLayout(new GridLayout(1,2));
		criteria = new JComboBox();
		criteria.addItem("Length");
		criteria.addItem("Time");
		criteria.addItem("Cost");
		criteriaPanel.add(new JLabel("Criteria:"));
		criteriaPanel.add(criteria);
		opensave.add(criteriaPanel, BorderLayout.CENTER);
		
		processor = new Processor();
		processor.setCanvas(canvas);
		
		mainFrame.show();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
