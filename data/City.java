package data;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class City implements Serializable {
	private String name;
	private Point point;
	private boolean isSelected;
	ArrayList<City> connections;
	
	public City(String name, Point p) {
		this.name = name;
		point = p;
		connections = new ArrayList<City>();
	}
	
	public Point getPoint() {
		return point;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void select() {
		isSelected = true;
	}
	
	public void deselect() {
		isSelected = false;
	}
	
	public void connectTo(City city) {
		if (!connections.contains(city)) {
			connections.add(city);
		}
	}
	
	public ArrayList<City> getConnections() {
		return connections;
	}
	
	public String toString() {
		return name + "(" + point.x + ";" + point.y + ")";
	}
	
	public String getName() {
		return name;
	}
}
