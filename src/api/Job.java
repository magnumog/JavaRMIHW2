package api;

import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JLabel;

public interface Job<T> {
	
	List<Task> decompose() throws RemoteException;
	
	void compose(Space space) throws RemoteException;
	
	T value();
	
	abstract JLabel viewResult(final T returnValue);
}
