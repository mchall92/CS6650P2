package Util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KVInterface extends Remote {
    void PUT(String key, String value) throws Exception;
    String GET(String key) throws Exception;
    String DELETE(String key) throws Exception;
}
