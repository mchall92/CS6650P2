package client;

import Util.KVInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class KVClient {
    ClientLogger clientLogger = new ClientLogger("Client");

    public static void main(String[] args) throws RemoteException {
        ClientLogger clientLogger = new ClientLogger("KVClient");
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1);
            KVInterface kvstub = (KVInterface) registry.lookup("KV");
            kvstub.PUT("A", "1");
            clientLogger.debug(kvstub.GET("A"));

        } catch(Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
