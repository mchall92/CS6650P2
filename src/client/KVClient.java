package client;

import Util.KVInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KVClient {

    private static ExecutorService executorService;
    private static ClientLogger clientLogger = new ClientLogger("Client");
    private static KVInterface kvstub;

    private static String[] put1, put2, put3, put4, put5, put6, put7;
    private static String[] get1, get2, get3, get4, get5;
    private static String[] del1, del2, del3, del4, del5;

    public static void main(String[] args) throws Exception {
        ClientLogger clientLogger = new ClientLogger("KVClient");
        executorService = Executors.newFixedThreadPool(20);
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1);
            kvstub = (KVInterface) registry.lookup("KV");

            runHardCodedCommand();

            runCustomCommands();

        } catch(Exception e){
            clientLogger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void execute(String[] request) {
        if (request[0].equalsIgnoreCase("put")) {
            executorService.execute(() -> {
                clientLogger.debug(sendPutRequest(request[1], request[2]));
            });
        } else if (request[0].equalsIgnoreCase("get")) {
            executorService.execute(() -> {
                clientLogger.debug(sendGetRequest(request[1]));
            });
        } else if (request[0].equalsIgnoreCase("delete")) {
            executorService.execute(() -> {
                clientLogger.debug(sendDeleteRequest(request[1]));
            });
        } else {
            clientLogger.error("Incorrect request command and should not reach here!");
        }
    }

    private static String sendPutRequest(String key, String value) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        clientLogger.debug("Sent request- PUT " + key + " with Value: " + value + "   " + timestamp);
        try {
            timestamp = new Timestamp(System.currentTimeMillis());
            return kvstub.PUT(key, value) + "   " + timestamp;
        } catch (RemoteException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            return e.getMessage() + "   " + timestamp;
        }
    }

    private static String sendGetRequest(String key) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        clientLogger.debug("Sent request- GET key: " + key + "   " + timestamp);
        try {
            timestamp = new Timestamp(System.currentTimeMillis());
            return kvstub.GET(key) + "   " + timestamp;
        } catch (RemoteException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            return e.getMessage() + "   " + timestamp;
        }
    }

    private static String sendDeleteRequest(String key) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        clientLogger.debug("Sent request- DELETE key: " + key + "   " + timestamp);
        try {
            timestamp = new Timestamp(System.currentTimeMillis());
            return kvstub.DELETE(key) + "   " + timestamp;
        } catch (RemoteException e) {
            timestamp = new Timestamp(System.currentTimeMillis());
            return e.getMessage() + "   " + timestamp;
        }
    }


    /**
     * Hard-coded operations.
     */
    private static void runHardCodedCommand() {
        put1 = new String[]{"put", "A", "1"};
        put2 = new String[]{"put", "B", "2"};
        put3 = new String[]{"put", "C", "3"};
        put4 = new String[]{"put", "D", "4"};
        put5 = new String[]{"put", "A", "5"};

        get1 = new String[]{"get", "A"};
        get2 = new String[]{"get", "B"};
        get3 = new String[]{"get", "C"};
        get4 = new String[]{"get", "D"};
        get5 = new String[]{"get", "A"};

        del1 = new String[]{"delete", "A"};
        del2 = new String[]{"delete", "B"};
        del3 = new String[]{"delete", "C"};
        del4 = new String[]{"delete", "D"};

        put6 = new String[]{"put", "A", "1"};

        del5 = new String[]{"delete", "A"};

        put7 = new String[]{"put", "B", "3"};

        execute(put1);
        execute(put2);
        execute(put3);
        execute(put4);
        execute(put5);

        execute(get1);
        execute(get2);
        execute(get3);
        execute(get4);
        execute(get5);

        execute(del1);
        execute(del2);
        execute(del3);
        execute(del4);

        execute(put6);
        execute(del5);
        execute(put7);
    }

    private static void runCustomCommands() {
        while (true) {

            Scanner sc= new Scanner(System.in);
            System.out.print("Enter an operation (PUT/GET/DELETE): ");
            String op = sc.nextLine();

            String[] operation = op.split("\\s+");

            // check if operation from user is correct
            // if so, send request, if not, prompt user to input operation again
            // and output instructions
            if (operation.length >= 2) {
                if (operation[0].equalsIgnoreCase("PUT") && operation.length == 3) {
                    execute(operation);
                } else if (operation[0].equalsIgnoreCase("GET") && operation.length == 2) {
                    execute(operation);
                } else if (operation[0].equalsIgnoreCase("DELETE") && operation.length == 2) {
                    execute(operation);
                } else {
                    errorOp();
                }
            } else if (operation[0].equalsIgnoreCase("CLOSE")) {
                break;
            } else {
                errorOp();
            }
        }
    }

    /**
     * Response to user input error.
     */
    private static void errorOp() {
        String msg = "Operation format incorrect, please follow this format:\n"
                + "PUT KEY VAULE\n"
                + "GET KEY\n"
                + "DELETE KEY\n"
                + "If you would like to exit, please enter: close";
        System.out.println(msg);
    }
}
