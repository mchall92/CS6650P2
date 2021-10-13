package server;

import Util.KVInterface;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KVServant implements KVInterface {

    KeyValue keyValue;
    ExecutorService executorService;
    ServerLogger serverLogger = new ServerLogger("KVServant");
    Timestamp timestamp;
    public KVServant() {
        keyValue = new KeyValue();
        executorService = Executors.newFixedThreadPool(20);
    }

    @Override
    public String PUT(String key, String value) throws RemoteException {
        timestamp = new Timestamp(System.currentTimeMillis());
        serverLogger.debug("Received request- PUT " + key + " with Value: " + value + "   " + timestamp);
        String client = null;
        try {
            client = RemoteServer.getClientHost();
        }
        catch (ServerNotActiveException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
        }

        String finalClient = client;
        Future future = executorService.submit(() -> {
            timestamp = new Timestamp(System.currentTimeMillis());
            if (keyValue.put(key, value) ) {
                serverLogger.debug("PUT request SUCCESS. PUT (Key / Value) : (" +
                                           key + " / " + value + ") from Client: " + finalClient +"   " + timestamp);
                return "PUT request SUCCESS. PUT (Key / Value) : (" + key + " / " + value + ")";
            } else {
                serverLogger.debug("PUT request FAILED for (Key / Value) : (" +
                                           key + " / " + value + ") from Client: " + finalClient + "   " + timestamp);
                return "PUT request FAILED for (Key / Value) : (" + key + " / " + value + ")";
            }
        });
        try {
            return (String) future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
            return e.getMessage() + "   " + timestamp;
        }
    }

    @Override
    public String GET(String key) throws RemoteException {
        timestamp = new Timestamp(System.currentTimeMillis());
        serverLogger.debug("Received request- GET key: " + key + "   " + timestamp);
        String client = "";
        try {
            client = RemoteServer.getClientHost();
        }
        catch (ServerNotActiveException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
        }
        String finalClient = client;
        Future future = executorService.submit(() -> {
            timestamp = new Timestamp(System.currentTimeMillis());
            if (keyValue.containsKey(key)) {
                String value = keyValue.get(key);
                serverLogger.debug("GET request SUCCESS. GET (Key / Value) -> (" +
                                           key + " : " + value + ") for Client: " + finalClient + "   " + timestamp);
                return "GET request SUCCESS. GET (Key / Value) -> (" +
                        key + " : " + value + ")";
            } else {
                serverLogger.debug("GET request FAILED for Key " +
                                           key + " for Client: " + finalClient + "   " + timestamp);
                return "GET request FAILED for Key : " + key;
            }

        });
        try {
            return (String) future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
            return e.getMessage() + "   " + timestamp;
        }
    }

    @Override
    public String DELETE(String key) throws RemoteException {
        timestamp = new Timestamp(System.currentTimeMillis());
        serverLogger.debug("Received request- DELETE key: " + key + "   " + timestamp);
        String client = "";
        try {
            client = RemoteServer.getClientHost();
        }
        catch (ServerNotActiveException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
        }

        String finalClient = client;
        Future future = executorService.submit(() -> {
            timestamp = new Timestamp(System.currentTimeMillis());
            if (keyValue.containsKey(key)) {
                keyValue.delete(key);
                serverLogger.debug("DELETE request SUCCESS. DELETE Key: " +
                                           key + " for Client: " + finalClient + "   " + timestamp);
                return "DELETE request SUCCESS. DELETE key: " + key;
            } else {
                serverLogger.debug("DELETE request FAILED for Key " +
                                           key + " for Client: " + finalClient + "   " + timestamp);
                return "DELETE request FAILED for Key : " + key;
            }

        });
        try {
            return (String) future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            serverLogger.error(e.getMessage() + "   " + timestamp);
            return e.getMessage() + "   " + timestamp;
        }
    }
}
