package server;

import Util.KVInterface;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class KVServant implements KVInterface {

    KeyValue keyValue;
    ThreadPool threadPool;
    ServerLogger serverLogger = new ServerLogger("KVServant");
    public KVServant() {

        keyValue = new KeyValue();
        threadPool = new ThreadPool(3, 10);
    }

    @Override
    public void PUT(String key, String value) throws Exception {
        serverLogger.debug("Put " + key + " with Value: " + value);
        threadPool.execute(() -> {

            keyValue.put(key, value);
        });

    }

    @Override
    public String GET(String key) throws Exception {
        AtomicReference<String> msg = new AtomicReference<>();
        serverLogger.debug("Get " + key);
        if (keyValue.containsKey(key)) {
            msg.set(keyValue.get(key));
        } else {
            msg.set("ERROR: key:" + key + " does not exist.");
        }
        return msg.get();
    }

    @Override
    public String DELETE(String key) throws Exception {
        AtomicReference<String> msg = new AtomicReference<>();
        serverLogger.debug("Delete" + key);
        if (keyValue.containsKey(key)) {
            keyValue.delete(key);
            msg.set("Key deleted.");
        } else {
            msg.set("ERROR: key:" + key + " does not exist.");
        }
        return msg.get();
    }
}
