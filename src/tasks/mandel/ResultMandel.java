package tasks.mandel;

public class ResultMandel {
	Integer[][] counts;
	int blockRow;
	int blockCol;
	
	public ResultMandel(Integer[][] counts, int blockRow, int blockCol) {
		this.counts = counts;
		this.blockRow = blockRow;
		this.blockCol = blockCol;
	}
	
	public Integer[][] counts() {
		return counts;
	}
	
	public int blockCol() {
		return blockCol;
	}
	
	public int blokRow() {
		return blockRow;
	}

}
