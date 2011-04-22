package data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import test.Processor;

public class Connection {
	private City city1 = null;
	private City city2 = null;
	private ArrayList<Point> points = new ArrayList<Point>();
	JPanel canvas;
	
	public Connection(City c1, City c2, JPanel canv) {
		city1 = c1;
		city2 = c2;
		canvas = canv;
	}
	
	public void draw() {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.BLACK);
		
		int steps = 0;
		float dx = city2.getPoint().x - city1.getPoint().x;
		float dy = city2.getPoint().y - city1.getPoint().y;
		float deltaX = 1;
		float deltaY = 1;
		steps = (int) Math.round(Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2)));
		
		deltaY = dy/steps;
		deltaX = dx/steps;
		
		System.out.println("Drawing line from " + city1 + " to " + city2 + ": deltaY=" + deltaY + ", deltaX=" + deltaX + ", steps=" + steps);
		
		float x = city1.getPoint().x + Processor.CITY_RECT_WIDTH/2, y = city1.getPoint().y + Processor.CITY_RECT_HEIGHT/2;
		int px, py = 0;
		for (int i = 0; i < steps; i++) {			
			x += deltaX;
			y += deltaY;
			g2d.drawRect(Math.round(x), Math.round(y), 0, 0);
			points.add(new Point(Math.round(x), Math.round(y)));
		}
	}
	
	public City getStartCity() {
		return city1;
	}
	
	public City getFinCity() {
		return city2;
	}
	
	public boolean hasPointInRegion(Point point, int radius) {
		boolean has = false;
		for(Point p : points) {
			if ((p.x > (point.x - radius) && p.x < (point.x + radius) && p.y > (point.y - radius) && p.y < (point.y + radius))) {
				has = true;
				break;
			}
		}
		return has;
	}
}
