package server;

import Util.KVInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class KVServer {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        ServerLogger serverLogger = new ServerLogger("KVServer");
        try {
            KVServant kvServant = new KVServant();
            KVInterface kvStub = (KVInterface) UnicastRemoteObject.exportObject(kvServant, 1);
            Registry registry = LocateRegistry.createRegistry(1);
            registry.rebind("KV", kvStub);
            serverLogger.debug("KVServer is listening...");

        } catch (Exception e) {
            serverLogger.error(e.getMessage());
        }
    }
}
