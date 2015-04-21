package tasks.tsp;

import java.util.List;

public class JobTSP implements Job<List<Integer>> {
	double[][] cities = TaskTSP.CITIES;
	List<Task> tourlist;
	List<Integer> tour;
	
	public JobTSP() {
	}
	
	@Override
	public List<Task> decompose() throws RemoteException {
		tourlist = new LinkedList<>();
		List<Integer> integerList = new LinkedList<>();
		for(int i=1;i<TaskTSP.CITIES.length;i++) {
			integerlist.add(i);
		}
		for(int i=0;i<integerList.size();i++) {
			List<Integer> partialList = new LinkedList<>(integerList);
			partialList.remove(i);
			Task taks = new TaskTSP(i+1,partialList);
			tourlist.add(task);
		}
		return tourlist;
	}
	
	@Override
	public void compose(Space space) throws RemoteException {
		tour = new LinkedList<>();
		double shortestTourDist = Double.MAX_VALUE;
		for(Task task : taskList) {
			Result<List<Integer>> result = space.take();
			double tourDist = TaskTSP.tourDistance(result.getTaskReturnValue() );
			if(tourDist<shortestTourDist) {
				tour=result.getTaskReturnValue();
				shortestTourDist=tourDist;
			}
		}
	}
	
	public List<Integer> value() {
		return tour;
	}
	
	@Override
    public JLabel viewResult( List<Integer> cityList ) 
    {
        Logger.getLogger( this.getClass().getCanonicalName() ).log( Level.INFO, "Tour: {0}", cityList.toString() );
        Integer[] tour = cityList.toArray( new Integer[0] );

        // display the graph graphically, as it were
        // get minX, maxX, minY, maxY, assuming they 0.0 <= mins
        double minX = CITIES[0][0], maxX = CITIES[0][0];
        double minY = CITIES[0][1], maxY = CITIES[0][1];
        for ( double[] cities : CITIES ) 
        {
            if ( cities[0] < minX ) 
                minX = cities[0];
            if ( cities[0] > maxX ) 
                maxX = cities[0];
            if ( cities[1] < minY ) 
                minY = cities[1];
            if ( cities[1] > maxY ) 
                maxY = cities[1];
        }

        // scale points to fit in unit square
        final double side = Math.max( maxX - minX, maxY - minY );
        double[][] scaledCities = new double[CITIES.length][2];
        for ( int i = 0; i < CITIES.length; i++ )
        {
            scaledCities[i][0] = ( CITIES[i][0] - minX ) / side;
            scaledCities[i][1] = ( CITIES[i][1] - minY ) / side;
        }

        final Image image = new BufferedImage( NUM_PIXALS, NUM_PIXALS, BufferedImage.TYPE_INT_ARGB );
        final Graphics graphics = image.getGraphics();

        final int margin = 10;
        final int field = NUM_PIXALS - 2*margin;
        // draw edges
        graphics.setColor( Color.BLUE );
        int x1, y1, x2, y2;
        int city1 = tour[0], city2;
        x1 = margin + (int) ( scaledCities[city1][0]*field );
        y1 = margin + (int) ( scaledCities[city1][1]*field );
        for ( int i = 1; i < CITIES.length; i++ )
        {
            city2 = tour[i];
            x2 = margin + (int) ( scaledCities[city2][0]*field );
            y2 = margin + (int) ( scaledCities[city2][1]*field );
            graphics.drawLine( x1, y1, x2, y2 );
            x1 = x2;
            y1 = y2;
        }
        city2 = tour[0];
        x2 = margin + (int) ( scaledCities[city2][0]*field );
        y2 = margin + (int) ( scaledCities[city2][1]*field );
        graphics.drawLine( x1, y1, x2, y2 );

        // draw vertices
        final int VERTEX_DIAMETER = 6;
        graphics.setColor( Color.RED );
        for ( int i = 0; i < CITIES.length; i++ )
        {
            int x = margin + (int) ( scaledCities[i][0]*field );
            int y = margin + (int) ( scaledCities[i][1]*field );
            graphics.fillOval( x - VERTEX_DIAMETER/2,
                               y - VERTEX_DIAMETER/2,
                              VERTEX_DIAMETER, VERTEX_DIAMETER);
        }
        final ImageIcon imageIcon = new ImageIcon( image );
        return new JLabel( imageIcon );
    }
	
	public static void main(String[] args) throws Exception {
		JobTSP job = new JobTSP();
		JobRunner jobRunner = new JobRunner(job,"Euclidian TSP","");
		jobrunner.run();
	}
}
