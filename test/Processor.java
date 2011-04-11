package test;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.*;

public class Processor {
	ArrayList<City> cities;
	JPanel canvas;
	
	public Processor() {
		cities = new ArrayList<City>();
		canvas = new JPanel();
	}
	
	public void addCity(String name, Point p) {
		cities.add(new City(name));
		canvas.getGraphics().drawRect(p.x, p.y, 50, 30);
		canvas.getGraphics().drawString(name, p.x + 10, p.y + 20);
	}
	
	public void setCanvas(JPanel p) {
		canvas = p;
	}
}
