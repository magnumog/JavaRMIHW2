package tasks.tsp;

public class TaskTSP implements Task<List<Integer>> {
	static final public double[][] CITIES =
	    {
		{ 1, 1 },
		{ 8, 1 },
		{ 8, 8 },
		{ 1, 8 },
		{ 2, 2 },
		{ 7, 2 },
		{ 7, 7 },
		{ 2, 7 },
		{ 3, 3 },
		{ 6, 3 },
		{ 6, 6 },
		{ 3, 6 }
	    };
	double[][] DISTANCES = initalizeDistances();
	Integer One = 1;
	Integer Two = 2;
	int secondCity;
	List<Integer> partialCityList;
	
	public TaskTSP(int secondCity, List<Integer> partialCityList) {
		this.secondCity=secondCity;
		this.partialCityList=partialCityList;
	}
	
	public List<Integer> call() {
		List<Integer> shortestTour = addPrefix(new LinkedList<>(partialCityList));
		double shortestTourDist = tourDistance(shortestTour);
		
		PermutationEnumerator<Integer> enumerator = new PermutationEnumerator<>(partialCityList);
		for(List<Integer> subtour = enumerator.next();subtour!=null; subtour=enumerator.next()) {
			List<Integer> tour = new ArrayList<>();
			addPrefix(tour);
			if(tour.indexOf(One) > tour.indexOf(Two)) {
				continue;
			}
			double tourDistance = tourDist(tour);
			if(tourDistance<shortestTour) {
				shortestTour=tour;
				shortestTourDist=tourDistance;
			}
		}
		return shortestTour;
	}
	
	private List<Integer> addPrefix(List<Integer> partialTour) {
		partialTour.add(0,secondCity);
		partialTour.add(0,0);
		return partialTour;
	}
	
	static public double tourDistance(List<Integer> tour) {
		double cost = DISTANCES[tour.get(tour.size()-1)][tour.get(0)];
		for(int i =0;i<tour.size()-1;i++) {
			cost += DISTANCES[tour.get(i)][tour.get(i+1)];
		}
		return cost;
	}
	
	static private double[][] initalizeDistances() {
		double[][] distances = new double[CITIES.length][CITIES.length];
		for(int i=0;i<CITIES.length;i++) {
			for(int j=0;j<i;j++) {
				distances[i][j] = distances[j][i] = distance(CITIES[i],CITIES[j]);
			}
		}
		return distances;
	}
	
	private static double distance(double[] first,double[] second) {
		double deltaX=first[0]-second[0];
		double deltaY=first[1]-second[1];
		return Math.sqrt(deltaX*deltaX+deltaY*deltaY);
	}

}
