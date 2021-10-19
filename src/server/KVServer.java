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

        if (args.length > 1) {
            serverLogger.error("Incorrect argument");
            return;
        }
        int portNum = 1234;

        if (args.length == 1) {
            try {
                portNum = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                serverLogger.error("Incorrect argument");
            }
        }

        try {
            KVServant kvServant = new KVServant();
            KVInterface kvStub = (KVInterface) UnicastRemoteObject.exportObject(kvServant, portNum);
            Registry registry = LocateRegistry.createRegistry(portNum);
            registry.rebind("KV", kvStub);
            serverLogger.debug("KVServer is listening...");

        } catch (Exception e) {
            serverLogger.error(e.getMessage());
        }
    }
}
