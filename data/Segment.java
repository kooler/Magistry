package data;

import java.util.ArrayList;

import test.Route;

public class Segment {
	private City startCity;
	private City finCity;
	private Route route;
	private ArrayList<City> cities;
	
	public Segment(Route r, ArrayList<City> c) {
		cities = c;
		startCity = c.get(0);
		finCity = c.get(c.size() - 1);
		route = r;
	}
	
	public City getStartCity() {
		return startCity;
	}
	
	public City getFinCity() {
		return finCity;
	}
	
	public Route getRoute() {
		return route;
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public String toString() {
		String output = "";
		for (City c : cities) {
			output += c + "; ";
		}
		return output;
	}
}
