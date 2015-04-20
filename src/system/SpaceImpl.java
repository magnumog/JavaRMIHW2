package system;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import api.Result;
import api.Space;
import api.Task;

public class SpaceImpl extends UnicastRemoteObject implements Space {

	protected SpaceImpl() throws RemoteException {
		super();
	}

	@Override
	public void putAll(List<Task> taskList) throws RemoteException {
		
	}

	@Override
	public Result take() throws RemoteException {
		return null;
	}

	@Override
	public void register(Computer computer) throws RemoteException {
		
	}

}
