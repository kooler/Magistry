package test;

import java.util.ArrayList;

import data.City;

public class Route {
	private ArrayList<City> cities = new ArrayList<City>();
	
	public Route() {
	}
	
	public Route(Route r) {
		for(City c : r.getCities()) {
			addCity(c);
		}
	}
	
	public void addCity(City c) {
		cities.add(c);
	}
	
	public boolean containsCity(City c) {
		return cities.contains(c);
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public String toString() {
		String output = "";
		for(City c : getCities()) {
			output += c + " -> ";
		}
		return output;
	}
	
}
