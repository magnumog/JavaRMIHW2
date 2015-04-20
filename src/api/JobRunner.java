package api;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import system.ComputerImpl;
import system.SpaceImpl;

public class JobRunner<T> extends JFrame {
	final private Job<T> job;
	final private Space space;
	final private long startTime=System.currentTimeMillis();
	
	public JobRunner(Job job, String title, String domainName) throws RemoteException,NotBoundException,MalformedURLException {
		System.setSecurityManager(new SecurityManager());
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.job=job;
		if(domainName.isEmpty()) {
			space = new SpaceImpl();
			for(int i=0; i<Runtime.getRuntime().availableProcessors();i++) {
				space.register(new ComputerImpl());
			}
		} else {
			final String url = "rmi://" + domainName + Space.PORT + "/" + Space.SERVICE_NAME;
			space = (Space) Naming.lookup(url);
		}
	}
	
	public void run() throws RemoteException {
		try {
			space.putAll(job.decompose());
		} catch(RemoteException e) {
			throw e;
		}
		
		try {
			job.compose(space);
		} catch(RemoteException e) {
			throw e;
		}
		
		view(job.viewResult(job.value()));
		Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, "Job run time: {0} ms.",(System.currentTimeMillis()-startTime)/100000);
	}
	
	private void view(final JLabel label) {
		final Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(new JScrollPane(label),BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

}
