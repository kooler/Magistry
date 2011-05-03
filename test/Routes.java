package test;

import java.util.ArrayList;
import java.util.Random;

import data.City;
import data.Segment;

public class Routes {
	/**
	 * Get all segments route has
	 */
	private static ArrayList<Segment> getRouteSegments(Route r) {
		ArrayList<Segment> segments = new ArrayList<Segment>();
		for (int i = 0; i < (r.getCities().size() - 1); i++) {
			for (int j = 1; j < r.getCities().size(); j++) {
				if (j > i) {
					ArrayList<City> cities = new ArrayList<City>();
					for (int k = i; k <= j; k++) {
						cities.add(r.getCities().get(k));
					}
					segments.add(new Segment(r, cities));
				}
				
			}
		}
		return segments;
	}
	/**
	 * Get all common segments both routes has
	 */
	private static ArrayList<Segment> getCommonSegments(Route route1, Route route2) {
		ArrayList<Segment> commonSegments = new ArrayList<Segment>();
		ArrayList<Segment> firstCitySegments = getRouteSegments(route1);
		ArrayList<Segment> finCitySegments = getRouteSegments(route2);

		//System.err.println("City1 segments: " + firstCitySegments);
		//System.err.println("City2 segments: " + finCitySegments);
		
		for(Segment s : firstCitySegments) {
			if (hasSegment(finCitySegments, s.getStartCity(), s.getFinCity())) {
				commonSegments.add(s);
			}
		}
		
		return commonSegments;
	}
	/**
	 * Get all common cities both routes have
	 */
	private static ArrayList<City> getCommonCities(Route route1, Route route2) {
		ArrayList<City> commonCities = new ArrayList<City>();
		
		for (City c : route1.getCities()) {
			if (route2.getCities().contains(c)) {
				commonCities.add(c);
			}
		}
		
		return commonCities;
	}
	/**
	 * Get the segment with the same start/fin city but from another route
	 */
	private static Segment getSegmentFromAnotherRoute(Segment s, ArrayList<Segment> segments) {
		Segment sameSeg = null;
		
		for (Segment seg: segments) {
			if (seg.getStartCity().equals(s.getStartCity()) &&
					seg.getFinCity().equals(s.getFinCity()) &&
					seg.getCities().size() != s.getCities().size()) {
				sameSeg = seg;
				break;
			}
		}
		
		return sameSeg;
	}
	/**
	 * Make crossover between two routes
	 */
	public static Route crossover(Route route1, Route route2, Processor p) {
		Route child = null;
		ArrayList<City> commonCities = getCommonCities(route1, route2);
		//Replace segment in route1 with another one from commonSegments
		if (commonCities.size() > 0) {
			Random r = new Random();
			int segmentNumber = Math.round((commonCities.size() - 1) * r.nextFloat()); 
			City crossCity = commonCities.get(segmentNumber);
			child = new Route(p);
			for (int i = 0; i < route1.getCities().size(); i++) {
				City c = route1.getCities().get(i);
				if (c.equals(crossCity)) {
					break;
				}
				child.addCity(c);
			}
			boolean start = false;
			for (int i = 0; i < route2.getCities().size(); i++) {
				City c = route2.getCities().get(i);
				if (c.equals(crossCity)) {
					start = true;
				}
				if (start) {
					child.addCity(c);
				}
			}
		} else {
			//System.err.println("No similar segments found");
		}
		return child;
	}
	
	public static boolean hasSegment(ArrayList<Segment> segments, City startCity, City finCity) {
		boolean has = false;
		
		for (Segment s : segments) {
			if (s.getStartCity().equals(startCity) && s.getFinCity().equals(finCity)) {
				has = true;
				break;
			}
		}
		
		return has;
	}
		
}
