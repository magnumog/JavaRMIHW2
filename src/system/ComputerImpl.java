package system;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import api.Result;
import api.Space;
import api.Task;

public class ComputerImpl extends UnicastRemoteObject implements Computer {

	int numberOfTasks = 0;
	public ComputerImpl() throws RemoteException {
		super();
	}

	@Override
	public <T> Result<T> execute(Task<T> task) throws RemoteException {
		numberOfTasks++;
		long startTime = System.currentTimeMillis();
		T value = task.call();
		long runTime = System.currentTimeMillis();
		return new Result<>(value, runTime);
	}

	@Override
	public void exit() throws RemoteException {
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new SecurityManager());
		
		String domain = "localhost";
		String url = "rmi://" + domain + ":" + Space.PORT +"/"+ Space.SERVICE_NAME;
		Space space = (Space)Naming.lookup(url);
		
		space.register(new ComputerImpl());
	}
	

}
