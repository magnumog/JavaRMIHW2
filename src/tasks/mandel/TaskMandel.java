package tasks.mandel;

import api.Task;

public class TaskMandel implements Task<ResultValueMandelbrotSet> {
	double lowerX;
	double lowerY;
	double edgeLength;
	int numPixles;
	int iterationLimit;
	int blockRow;
	int blockCol;
	
	public TaskMandel(double lowerX,double lowerY,double edgeLength, int numPixles, int iterationLimit, int blockRow, int blockCol) {
		this.lowerX = lowerX;
		this.lowerY = lowerY;
		this.edgeLength = edgeLength;
		this.numPixles = numPixles;
		this.iterationLimit = iterationLimit;
		this.blockRow = blockRow;
		this.blockCol = blockCol;
	}

	@Override
	public ResultValueMandelbrotSet call() {
		Integer[][] counts = new Integer[numPixles][numPixles];
		double delta = edgeLength/numPixles;
		for(int i=0;i<numPixles;i++) {
			for(int j=0;j<numPixles;j++) {
				counts[i][j] = getIterationCount(i,j,delta);
			}
		}
		return new ResultValueMandelbrotset(counts,blockRow,blockCol);
	}

	private int getIterationCount(int i, int j, double delta) {
		double x0 = lowerX+i*delta;
		double y0 = lowerY+j*delta;
		int iteration = 0;
		for(double x =x0,y=y0; x*x+y*y<=4.0 && iteration<iterationLimit;iteration++) {
			double xtemp = x*x-y*y+x0;
			y=2*x*y+y0;
			x=xtemp;
		}
		return iteration;
	}
	
	

}
