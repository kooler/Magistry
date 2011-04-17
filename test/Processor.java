package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.print.attribute.standard.Finishings;
import javax.swing.JPanel;

import data.*;

public class Processor {
	ArrayList<City> cities;
	JPanel canvas;
	final int CITY_RECT_WIDTH = 50;
	final int CITY_RECT_HEIGHT = 50;
	City startCity = null;
	City finCity = null;
	private boolean selectionStarted = false;
	ArrayList<Route> routes;
	ArrayList<City> checkedCities;
	
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
		for (City ct : cities) {
			selectCity(ct, Color.BLUE);
		}
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
		for (City ct : cities) {
			selectCity(c, Color.BLUE);
		}
		selectCity(c, Color.RED);
	}
	
	public void selectCity(City c, Color col) {
		Graphics2D g2d = (Graphics2D)canvas.getGraphics();
		g2d.setColor(col);
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
	
	private void madePairingOfTwoBestParents() {
		//Get two bets parents
		int bestLevel = 0;
		Route parent1 = null;
		for (Route r : routes) {
			if (r.getLength() > bestLevel) {
				parent1 = r;
			}
		}
		Route parent2 = null;
		bestLevel = 0;
		for (Route r : routes) {
			if (r.getLength() > bestLevel && !r.equals(parent1)) {
				parent2 = r;
			}
		}
		
		if (parent1 != null && parent2 != null) {
			System.err.println("Two parents Found!");
			System.err.println("Parent1: " + parent1);
			System.err.println("Parent2: " + parent2);
			//Generate children
			Route ch1 = Routes.crossover(parent1, parent2);
			Route ch2 = Routes.crossover(parent1, parent2);
			System.err.println("Child1: " + ch1);
			System.err.println("Child2: " + ch2);
			//Replace parent with children
			parent1 = ch1;
			parent2 = ch2;
		} else {
			System.err.println("Can't find two parents");
		}
	}
	
	private void startProcess() {
		generatePopulation();
		//madePairingOfTwoBestParents();
		
		//printDivider();
		//printRoutes();
	}
	
	public void generatePopulation() {
		routes = new ArrayList<Route>();
		Route r = new Route();
		
		checkedCities = new ArrayList<City>();
		r.addCity(startCity);
		checkedCities.add(startCity);
		checkNeightbours(startCity, finCity, r);
		
		printRoutes();
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
		}
	}
}
