import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

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
    
    private final List<ServerThread> serverThreads = new CopyOnWriteArrayList<>();

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
                try {
                    ServerThread serverThread = new ServerThread(socket, serverName, this);
                    serverThreads.add(serverThread);
                    serverThread.start();
                } catch (IOException e) {
                    socket.close();
                    System.err.println("Could not create client thread: "+ e.getMessage());
                }
            }
        }
        catch (IOException e){        
            if (!serverSocket.isClosed()) {
                e.printStackTrace();
            }
        }

        System.out.println("--SERVER TERMINATED--");
    }

    public void removeThread(ServerThread thread) {
        serverThreads.remove(thread);
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

        closeServerSocket();
        for (ServerThread thread : serverThreads) {
            thread.shutdown();
        }
        serverThreads.clear();

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

    public ServerSocket getServerSocket(){
        return this.serverSocket;
    }

}