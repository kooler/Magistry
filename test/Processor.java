package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.print.attribute.standard.Finishings;
import javax.swing.JPanel;

import data.*;

public class Processor implements Serializable {
	ArrayList<City> cities;
	JPanel canvas;
	public final static int CITY_RECT_WIDTH = 50;
	public final static int CITY_RECT_HEIGHT = 50;
	City startCity = null;
	City finCity = null;
	private boolean selectionStarted = false;
	ArrayList<Route> routes;
	ArrayList<City> checkedCities;
	ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public Processor() {
		cities = new ArrayList<City>();
		canvas = new JPanel();
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public void setCities(ArrayList<City> c) {
		cities = c;
	}
	
	public void addCity(String name, Point p) {
		City c = new City(name, p);
		cities.add(c);
		selectCity(c);
	}
	
	public void setCanvas(JPanel p) {
		canvas = p;
	}
	
	public City getCity(Point p) {
		City result = null;
		for(City c : cities) {
			if (c.getPoint().x <= p.x && c.getPoint().y <= p.y && 
					(c.getPoint().x + CITY_RECT_WIDTH) >= p.x && (c.getPoint().y + CITY_RECT_HEIGHT) >= p.y) {
				result = c;
				break;
			}
		}
		return result;
	}
	
	public void selectCity(City c) {
		for (City ct : cities) {
			selectCity(c, Color.BLUE);
		}
		selectCity(c, Color.RED);
	}
	
	public void selectCity(City c, Color col) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(col);
		g2d.drawOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		g2d.drawString(c.getName(), c.getPoint().x + 10, c.getPoint().y + 30);
		c.select();
	}
	
	public void deselectCity(City c) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.drawOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		g2d.drawString(c.getName(), c.getPoint().x + 10, c.getPoint().y + 30);
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
	
	private void drawConnection(City city1, City city2) {
		//Draw line
		boolean drawed = false;
		for (Connection c : connections) {
			if (city1.equals(c.getStartCity()) && city2.equals(c.getFinCity())) {
				drawed = true;
				c.draw();
			}
		}
		if (!drawed) {
			Connection c = new Connection(city1, city2, canvas);
			c.draw();
			connections.add(c);
		}
	}
	
	public void connectCities(City city1, City city2) {
		city1.connectTo(city2);
		city2.connectTo(city1);
		drawConnection(city1, city2);
	}
	
	public void startSelection() {
		selectionStarted = true;
		startCity = null;
		finCity = null;
		deselectAllCities();
	}
	
	public boolean selectionStarted() {
		return selectionStarted;
	}
	
	public void addToSelection(City c) {
		if (startCity == null) {
			startCity = c;
		} else {
			finCity = c;
			selectionStarted = false;
			selectCity(finCity, Color.BLUE);
			startProcess();
		}
		selectCity(startCity, Color.BLUE);
	}
	
	public Route getBestCreature() {
		int bestLevel = 99999999;
		Route parent1 = null;
		for (Route r : routes) {
			if (r.getLength() < bestLevel) {
				parent1 = r;
				bestLevel = r.getLength();
			}
		}
		return parent1;
	}
	
	private void madePairingOfTwoBestParents() {
		//Get two bets parents
		int bestLevel = 99999999;
		Route parent1 = null;
		for (Route r : routes) {
			if (r.getLength() < bestLevel) {
				parent1 = r;
				bestLevel = r.getLength();
			}
		}
		Route parent2 = null;
		bestLevel = 99999999;
		for (Route r : routes) {
			if (r.getLength() < bestLevel && !r.equals(parent1)) {
				parent2 = r;
				bestLevel = r.getLength();
			}
		}
		
		if (parent1 != null && parent2 != null) {
			//System.out.println("Two parents Found!");
			//System.out.println("Parent1: " + parent1);
			//System.out.println("Parent2: " + parent2);
			//Generate children
			Route ch1 = Routes.crossover(parent1, parent2);
			Route ch2 = Routes.crossover(parent1, parent2);
			//System.out.println("Child1: " + ch1);
			//System.out.println("Child2: " + ch2);
			//Replace parent with children
			parent1 = ch1;
			parent2 = ch2;
		} else {
			//System.err.println("Can't find two parents");
		}
	}
	
	private void startProcess() {
		routes = new ArrayList<Route>();
		int sizeOfPopulation = 100;
		for (int i = 0; i < sizeOfPopulation; i++) {
			generatePopulation();
		}
		printAllRoutes();
		System.out.println("Current: " + getBestCreature());
		int numberOfParings = 1000;
		for (int i = 0; i < numberOfParings; i++) {
			madePairingOfTwoBestParents();
		}
		System.out.println("Parring: " + getBestCreature());
		printAllRoutes();
//		printDivider();
		//printRoutes();
	}
	
	public void generatePopulation() {
		Route r = new Route();
		
		checkedCities = new ArrayList<City>();
		r.addCity(startCity);
		checkedCities.add(startCity);
		checkNeightbours(startCity, finCity, r);
		
		//printRoutes();
	}
	
	public void printDivider() {
		System.out.println("---------------------------------------------------");
	}
	
	public void printRoutes() {
		for(Route r : routes) {
			System.out.println(r);
		}
	}
	
	private void checkNeightbours(City c, City lookFor, Route route) {
		//Get random neighbour
		boolean processed = false;
		int MAX_ITERATIONS = 1000;
		int iteration = 0;
		Random r = new Random();
		do {
			int neighNum = Math.round(r.nextFloat() * (c.getConnections().size() - 1));
			City neigh = c.getConnections().get(neighNum);
			if (!route.containsCity(neigh)) {
				processed = true;
				if (neigh.equals(lookFor)) {
					route.addCity(neigh);
					//System.err.println(route);
					routes.add(route);
					break;
				} else {
					Route newRoute = new Route(route);
					newRoute.addCity(neigh);
					checkNeightbours(neigh, lookFor, newRoute);
				}
			}
			iteration++;
		} while (!processed && iteration < MAX_ITERATIONS);
		/*
		for(City city : c.getConnections()) {
			if (route.containsCity(city)) {
				continue;
			}
			if (city.equals(lookFor)) {
				route.addCity(city);
				routes.add(route);
				break;
			} else {
				Route newRoute = new Route(route);
				newRoute.addCity(city);
				checkNeightbours(city, lookFor, newRoute);
			}
		}*/
	}
	
	public void redraw() {
		deselectAllCities();
		//Redraw connections
		for (City c : cities) {
			for (City c1 : c.getConnections()) {
				drawConnection(c, c1);
			}
		}
	}
	
	public void printAllRoutes() {
		System.err.println("=========================================================");
		for (Route r : routes) {
			System.err.println(r);
		}
		System.err.println("=========================================================");
	}
	
	public Connection getConnection(Point p) {
		Connection con = null;
		for(Connection c : connections) {
			if (c.hasPointInRegion(p, 10)) {
				con = c;
				break;
			}
		}
		return con;
	}
}
