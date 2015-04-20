package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import api.Result;
import api.Space;
import api.Task;

public class SpaceImpl extends UnicastRemoteObject implements Space {
	BlockingQueue<Task> taskQ = new LinkedBlockingQueue<>();
	BlockingQueue<Result> resultQ = new LinkedBlockingQueue<>();
	Map<Computer,ComputerProxy> computerProxies = new HashMap<>();
	int computerIds = 0;

	public SpaceImpl() throws RemoteException {
		super();
		Logger.getLogger(SpaceImpl.class.getName()).log(Level.INFO,"Space started");
	}

	@Override
	synchronized public void putAll(List<Task> taskList) {
		for(Task task : taskList) {
			taskQ.add(task);
		}
		
	}

	@Override
	synchronized public Result take() {
		try {
			return resultQ.take();
		} catch (InterruptedException e) {
			Logger.getLogger(SpaceImpl.class.getName()).log(Level.INFO,null,e);
		}
		assert false;
		return null;
	}

	@Override
	synchronized public void register(Computer computer) throws RemoteException {
		final ComputerProxy computerProxy = new ComputerProxy(computer);
		computerProxies.put(computer,computerProxy);
		computerProxy.start();		
	}

	@Override
	public void exit() throws RemoteException {
		computerProxies.values().forEach( proxy -> proxy.exit);
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new SecurityManager());
		LocateRegistry.createRegistry(Space.PORT).rebind(Space.SERVICE_NAME, new SpaceImpl());
	}
	
	private class ComputerProxy extends Thread implements Computer {
		private Computer computer;
		private int computerId = computerIds++;
		
		ComputerProxy(Computer computer) {
			this.computer = computer;
		}

		@Override
		public Result execute(Task task) throws RemoteException {
			return computer.execute(task);
		}

		@Override
		public void exit() throws RemoteException {
			try{
				computer.exit();
			} catch(RemoteException e) {
			}
			
		}
		
		public void run() {
			while(true) {
				Task task = null;
				try {
					task = taskQ.take();
					resultQ.add(execute(task));
				} catch(RemoteException e) {
					taskQ.add(task);
					computerProxies.remove(computer);
					break;
				} catch(InterruptedException e) {
					
				}
			}
		}
		
	}

}
