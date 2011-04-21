package test;

import java.util.ArrayList;
import java.util.Random;

import data.City;
import data.Segment;

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
		output += " :: " + getLength();
		return output;
	}
	
	public void replaceSegment(Segment replaceIt, Segment replaceTo) {
		if (replaceIt == null || replaceTo == null) {
			//System.out.println("Error in replacement, not enough data");
			return;
		}
		ArrayList<City> newCities = new ArrayList<City>();
		boolean replacementStarted = false;
		for(City c : getCities()) {
			//If start of segment - start skipping
			if (c.equals(replaceIt.getStartCity())) {
				replacementStarted = true;
			}
			//Add city
			if (!replacementStarted) {
				newCities.add(c);
			}
			//If end of segment - stop skipping and replace new segment
			if (c.equals(replaceIt.getFinCity())) {
				replacementStarted = false;
				for(City c1 : replaceTo.getCities()) {
					newCities.add(c1);
				}
			}
		}
		if (newCities.size() > 0) {
			cities = newCities;
		}
	}
	
	public int getLength() {
		int length = 0;
		for (int i = 0; i < (cities.size() - 1); i++) {
			length += Math.round(Math.sqrt(Math.pow(cities.get(i).getPoint().x - cities.get(i + 1).getPoint().x,2) + 
					Math.pow(cities.get(i).getPoint().y - cities.get(i + 1).getPoint().y,2)));
		}
		return length;
	}
}
