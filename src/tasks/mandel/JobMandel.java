package tasks.mandel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import api.Job;
import api.JobRunner;
import api.Result;
import api.Space;
import api.Task;

public class JobMandel implements Job<Integer[][]> {
	static double lower_x = -0.7510975859375;
	static double lower_y = 0.1315680625;
	static double edge_length = 0.01611;
	static int n_pixles = 1024;
	static int iteration_limit = 512;
	static int block_size = 256;

	private List<Task> taskList;
	private Integer[][] counts;
	
	public JobMandel() {
	}
	
	@Override
	public List<Task> decompose() throws RemoteException {
		taskList = new LinkedList<>();
		int numbOfBlocks = n_pixles/block_size;
		double edgeLength = edge_length/numbOfBlocks;
		for(int i=0;i<numbOfBlocks;i++) {
			for(int j=0;j<numbOfBlocks;j++) {
				double lowerLeftX = lower_x + edgeLength * i;
				double lowerLeftY = lower_y + edgeLength * j;
				Task task = new TaskMandel(lowerLeftX, lowerLeftY, edgeLength, block_size,iteration_limit,i,j);
				taskList.add(task);
			}
		}
		return taskList;
	}

	@Override
	public void compose(Space space) throws RemoteException {
		counts = new Integer[n_pixles][n_pixles];
		for(Task task : taskList) {
			Result<ResultMandel> result = (Result<ResultMandel>) space.take();
			ResultMandel resultValue = result.getTaskReturnValue();
			
			Integer[][] blockCounts = resultValue.counts();
			int blockRow = resultValue.blokRow();
			int blockCol = resultValue.blockCol();
			for(int row=0; row<block_size;row++){
				System.arraycopy(blockCounts[row],0, counts[blockRow*block_size+row], blockCol*block_size, block_size);
			}
		}
		
	}

	@Override
	public Integer[][] value() {
		return counts;
	}

	@Override
	public JLabel viewResult(Integer[][] returnValue) {
		Image image = new BufferedImage(n_pixles, n_pixles, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		for(int i=0; i<counts.length;i++){
			for(int j=0;j<counts.length;j++) {
				graphics.setColor(getColor(counts[i][j]));
				graphics.fillRect(i, n_pixles-j, 1, 1);
			}
		}
		ImageIcon imageIcon = new ImageIcon(image);
		return new JLabel(imageIcon);
	}

	private Color getColor(int integer) {
		return integer == iteration_limit ? Color.BLACK : Color.WHITE;
	}
	
	public static void main(String[] args) throws Exception {
		JobMandel job = new JobMandel();
		JobRunner jobrunner = new JobRunner(job, "mandel set visulizer","");
		jobrunner.run();
	}
}
