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
		for (int i = 0; i < r.getCities().size(); i++) {
			for (int j = 1; j < r.getCities().size(); j++) {
				if (j > (i + 1)) {
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

		for(Segment s : firstCitySegments) {
			if (finCitySegments.contains(s)) {
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
					!seg.getRoute().equals(s.getRoute())) {
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
			System.err.println(s);
		}
		//Replace segment in route1 with another one from commonSegments
		if (commonSegments.size() > 1) {
			Random r = new Random();
			int segmentNumber = Math.round((commonSegments.size() - 1) * r.nextFloat()); 
			Segment replaceIt = commonSegments.get(segmentNumber);
			Segment replacement = getSegmentFromAnotherRoute(replaceIt, commonSegments);
			//Replace segment
			child = new Route(commonSegments.get(segmentNumber).getRoute());
			child.replaceSegment(replaceIt, replacement);
		}
		return child;
	}
	
}
