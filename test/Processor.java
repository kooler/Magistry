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
	String criteria = "Length";
	boolean showGeneticProcess = false;
	int visualDelayTime = 1000;
	
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
		p.x -= CITY_RECT_WIDTH/2;
		p.y -= CITY_RECT_HEIGHT/2;
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
	
	public void fillCity(City c, Color col, int delta) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setBackground(col);
		g2d.setColor(col);
		if (delta == 0) {
			g2d.fillOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		} else {
			g2d.drawOval(c.getPoint().x - delta/2, c.getPoint().y - delta/2, CITY_RECT_WIDTH + delta, CITY_RECT_HEIGHT + delta);
		}
		g2d.drawString(c.getName(), c.getPoint().x + 10, c.getPoint().y + 30);
	}
	
	public void selectCityInRoute(City c) {
		selectCity(c, Color.magenta);
	}
	
	public void deselectCity(City c) {
		deselectCity(c, Color.black);
	}
	
	public void deselectCity(City c, Color col) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(col);
		g2d.drawOval(c.getPoint().x, c.getPoint().y, CITY_RECT_WIDTH, CITY_RECT_HEIGHT);
		g2d.drawString(c.getName(), c.getPoint().x + 10, c.getPoint().y + 30);
		c.deselect();
	}
	
	public void clearMap() {
		canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		canvas.setBackground(Color.white);
		for (City c: cities) {
			deselectCity(c, Color.lightGray);
		}
		for (Connection c : connections) {
			c.draw(Color.lightGray);
		}
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
	
	public void startSelection(String criteria) {
		selectionStarted = true;
		startCity = null;
		finCity = null;
		deselectAllCities();
		this.criteria = criteria;
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
			try {
				startProcess();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		selectCity(startCity, Color.BLUE);
	}
	
	public Route getBestCreature() {
		int bestLevel = 9999999;
		Route best = null;
		int param = 0;
		for (Route r : routes) {
			if (criteria == "Length") {
				param = r.getLength();
			} else if (criteria == "Time") {
				param = r.getTime();
			} else if (criteria == "Cost") {
				param = r.getCost();
			}
			if (param < bestLevel) {
				best = r;
				bestLevel = r.getLength();
			}
		}
		return best;
	}
	
	public int getWorthCreature() {
		int worthLevel = 0;
		int best = 0;
		int param = 0;
		for (int i = 0; i < routes.size(); i++) {
			Route r = routes.get(i);
			if (criteria == "Length") {
				param = r.getLength();
			} else if (criteria == "Time") {
				param = r.getTime();
			} else if (criteria == "Cost") {
				param = r.getCost();
			}
			if (param > worthLevel) {
				best = i;
				worthLevel = r.getLength();
			}
		}
		return best;
	}
	
	private void madePairingOfTwoBestParents() throws InterruptedException {
		//Get two random parents
		Random r = new Random();
		Route parent1 = routes.get(Math.round(routes.size()/2 * r.nextFloat()));
		Route parent2 = routes.get(Math.round(routes.size()/2 + routes.size()/2 * r.nextFloat()) - 1);
		
		if (showGeneticProcess) {
			clearMap();
			parent1.draw(6, Color.red);
			Thread.sleep(visualDelayTime);
			parent2.draw(10, Color.blue);
			Thread.sleep(visualDelayTime);
		}
		
		if (parent1 != null && parent2 != null) {
			//System.out.println("Two parents Found!");
			//System.out.println("Parent1: " + parent1);
			//System.out.println("Parent2: " + parent2);
			//Generate children
			Route ch1 = Routes.crossover(parent1, parent2, this);
			//Route ch2 = Routes.crossover(parent1, parent2, this);
			//System.out.println("Child1: " + ch1);
			//System.out.println("Child2: " + ch2);
			//Replace parent with children
			
			routes.set(getWorthCreature(), ch1);
			
			if (showGeneticProcess) {
				ch1.drawAsChild();
				Thread.sleep(visualDelayTime);
				//ch2.drawAsChild();
				Thread.sleep(visualDelayTime);
			}
			
		} else {
			//System.err.println("Can't find two parents");
		}
	}
	
	private void startProcess() throws InterruptedException {
		routes = new ArrayList<Route>();
		int sizeOfPopulation = 50;
		for (int i = 0; i < sizeOfPopulation; i++) {
			generatePopulation();
		}
		printAllRoutes();
		System.out.println("Current: " + getBestCreature());
		int numberOfParings = 1000;
		for (int i = 0; i < numberOfParings; i++) {
			madePairingOfTwoBestParents();
			//Thread.sleep(visualDelayTime);
		}
		System.out.println("After Parrings " + getBestCreature());
		printAllRoutes();
	}
	
	public void generatePopulation() {
		Route r = new Route(this);
		
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
					Route newRoute = new Route(route, this);
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
	
	public Connection getConnection(City c1, City c2) {
		Connection c = null;
		for (Connection con : connections) {
			if ((con.getStartCity().equals(c1) && con.getFinCity().equals(c2))
					|| (con.getStartCity().equals(c2) && con.getFinCity().equals(c1))) {
				c = con;
				break;
			}
		}
		return c;
	}
}
