import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

/*

server thread
- gen server name
- wait for client connection
...
- once connected:
- take in init client message
- print client name and server name
- input 1~100 as server int
- display client int, server int, sum(c,s)

IF client int is OOR (n<1 OR n>100)
- terminate and release all sockets
- shut down server
ELSE
- continue receiving?

concurrent server (+5 pts)
- relegate each client connecting to a thread 
*/

public class Server {
    static final int PORT = 6767;
    private ServerSocket serverSocket;
    private String serverName;

    public Server(ServerSocket serverSocket, String serverName){
        this.serverSocket = serverSocket;
        this.serverName = serverName;
    }

    public void startServer(){
        System.out.println("--START SERVER--");
        System.out.println("Server name: " + serverName);
        
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, serverName, this);

                serverThread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("--SERVER TERMINATED--");
    }

    public void closeServerSocket(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /* TODO */
    public void closeServer(){
        System.out.println("--CLOSING SERVER--");

        for (ServerThread thread : new ArrayList<>(ServerThread.serverThreads)) {
            thread.shutdown();
        }
        ServerThread.serverThreads.clear();
        closeServerSocket();

        System.out.println("--SERVER CLOSED--");
    }

    public static void main(String[] args){

        Scanner s = new Scanner(System.in);

        System.out.println("--INIT--");
        System.out.println("Enter a server name:");

        String serverName = s.nextLine(); 

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket, serverName);
            server.startServer();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        s.close();
    }

}