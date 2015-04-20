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
		// TODO Auto-generated method stub
		return null;
	}

}
