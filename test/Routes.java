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
	public static Route crossover(Route route1, Route route2) {
		Route child = null;
		ArrayList<Segment> commonSegments = getCommonSegments(route1, route2);
		for (Segment s : commonSegments) {
			System.out.println("Found common segment:" + s);
		}
		//Replace segment in route1 with another one from commonSegments
		if (commonSegments.size() > 0) {
			Random r = new Random();
			int segmentNumber = Math.round((commonSegments.size() - 1) * r.nextFloat()); 
			Segment replaceIt = commonSegments.get(segmentNumber);
			//System.out.println("Random segment: " + replaceIt);
			Route searchInRoute;
			if (replaceIt.getRoute().equals(route1)) {
				searchInRoute = route2;
			} else {
				searchInRoute = route1;
			}
			Segment replacement = getSegmentFromAnotherRoute(replaceIt, getRouteSegments(searchInRoute));
			System.out.println("Replacing in " + replaceIt.getRoute() + " [" + replaceIt + "] to [" + replacement + "]");
			//System.out.println("Replacement segment: " + replacement);
			//Replace segment
			child = new Route(replaceIt.getRoute());
			child.replaceSegment(replaceIt, replacement);
		} else {
			System.err.println("No similar segments found");
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
