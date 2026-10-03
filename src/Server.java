
/*  
    CSNETWK S03
    *Mirai & Ram
    (Java Server)
*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
    Server class acts as the hub for clients to commune and pass numbers 
    around and sum it up. Cretes a server thread to handle each client separately.
    
    The server only shuts down unless its forcefully terminated or a client sends
    an out of range number (n>100 || n<1).
*/

public class Server {
    static final int PORT = 6767;
    private ServerSocket serverSocket;
    private String serverName;
    
    private final List<ServerThread> serverThreads = Collections.synchronizedList(new ArrayList<>());

    public Server(ServerSocket serverSocket, String serverName) {
        this.serverSocket = serverSocket;
        this.serverName = serverName;
    }

    /* Starts server */
    public void startServer() {
        System.out.println("Server name: " + serverName + "on Port: " + PORT);
        System.out.print("> Waiting for clients... ");
        
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.print("> Client detected. Relegating to a thread ");
                ServerThread serverThread = new ServerThread(socket, serverName, this);
                serverThreads.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {        
            if (!serverSocket.isClosed()) {
                System.out.print("> Server Thread unable to be created. ");
                e.printStackTrace();
            }
        }
    }

    /* Removes a thread from the list */
    public void removeThread(ServerThread thread) {
        serverThreads.remove(thread);
    }

    /* Close Server */
    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ServerThread> threadsToClose;
        synchronized (serverThreads) {
            threadsToClose = new ArrayList<>(serverThreads);
        }

        for (ServerThread thread : threadsToClose) {
            thread.closeThread();
        }

        serverThreads.clear();
        System.out.println("--SERVER TERMINATED--");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("--INIT--");

        boolean loop = true;
        String serverName = null;

        /* Input Name */
        while (loop) {
            System.out.print("Enter server name (letters only): ");
            String input = scanner.nextLine().trim();

            if (input.matches("[A-Za-z]+")) {
                serverName = input;
                loop = false;
            }

            if (loop) {
                System.out.println("\nInvalid name. Use letters only.\\n");
            }
        }

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket, serverName);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}