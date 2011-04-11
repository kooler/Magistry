package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import data.*;

public class Processor {
	ArrayList<City> cities;
	JPanel canvas;
	final int CITY_RECT_WIDTH = 50;
	final int CITY_RECT_HEIGHT = 50;
	
	public Processor() {
		cities = new ArrayList<City>();
		canvas = new JPanel();
	}
	
	public void addCity(String name, Point p) {
		cities.add(new City(name, p));
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.drawOval(p.x, p.y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		g2d.drawString(name, p.x + 10, p.y + 30);
	}
	
	public void setCanvas(JPanel p) {
		canvas = p;
	}
	
	public City getCity(Point p) {
		City result = null;
		for(City c : cities) {
			if (c.getPoint().x < p.x && c.getPoint().y < p.y && 
					(c.getPoint().x + CITY_RECT_WIDTH) > p.x && (c.getPoint().y + CITY_RECT_HEIGHT) > p.y) {
				result = c;
				break;
			}
		}
		return result;
	}
	
	public void selectCity(City c) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.RED);
		g2d.drawOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);		
		c.select();
	}
	
	public void deselectCity(City c) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.drawOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		c.deselect();
	}
	
	public void deselectAllCities() {
		for(City c : cities) {
			deselectCity(c);
		}
	}
	
	public City getSelectedCity() {
		City result = null;
		for(City c : cities) {
			if (c.isSelected()) {
				result = c;
				break;
			}
		}
		return result;
	}
	
	public void connectCities(City city1, City city2) {
		city1.connectTo(city2);
		city2.connectTo(city1);
		//Draw line
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.BLACK);
		int x1 = 0, x2 = 0;
		if (city1.getPoint().x < city2.getPoint().x) {
			x1 = city1.getPoint().x + CITY_RECT_WIDTH;
			x2 = city2.getPoint().x;
		} else {
			x1 = city1.getPoint().x;
			x2 = city2.getPoint().x + CITY_RECT_WIDTH;
		}
		g2d.drawLine(x1, city1.getPoint().y + CITY_RECT_HEIGHT/2, x2, city2.getPoint().y + CITY_RECT_HEIGHT/2);
	}
}
