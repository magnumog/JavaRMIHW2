package system;

import java.rmi.Remote;
import java.rmi.RemoteException;

import api.Result;
import api.Task;

public interface Computer extends Remote {
	public <T> Result<T> execute(Task<T> task) throws RemoteException;
	
	public void exit() throws RemoteException;
}
