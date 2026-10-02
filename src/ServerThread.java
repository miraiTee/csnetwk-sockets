import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/*
server thread
- gen server name
- wait for client connection
...
- once connected:
- take in init client message
- print client name  and server name
- input 1~100 as server int
- display client name client int, server name, server int, sum(c,s)
- send to client 

- clients sends one final it received message 
- and then close the thread?
*/

public class ServerThread extends Thread {
    public  static ArrayList<ServerThread> serverThreads = new ArrayList<>();
    private Socket socket;
    private Server server;

    private String serverName;
    private int serverNum;

    private String clientName;
    private int    clientNum;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ServerThread(Socket socket, String serverName, Server server){
        try {
            this.socket = socket;
            this.serverName = serverName;
            this.server = server;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverThreads.add(this);
        } catch (IOException e){
            closeThread(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override public void run(){
        String messageFromClient;   

        try {
            while (!socket.isClosed()){
                messageFromClient = bufferedReader.readLine();

                System.out.println("");
                System.out.println("--RECEIVING--");
                System.out.println(messageFromClient);
                System.out.println("--RECEIVING--");
                System.out.println("");

                /* preconditions client ONLY SENDS message in the form {Client: <STRING> <INT>} */
                String[] arr = messageFromClient.split(" ");
                this.clientName = arr[1];
                this.clientNum = Integer.parseInt(arr[2]);

                /* Rand 1~100 */
                serverNum = (int)(Math.random() * 100) + 1;
                int sum = clientNum + serverNum;

                System.out.println("");
                System.out.println("-- Summary --");
                System.out.println("Client:" + clientName + "has chosen" + clientNum);
                System.out.println("Server:" + serverName + "generated" + serverNum);
                System.out.println("Total:" + sum);
                System.out.println("-------------");
                System.out.println("");

                System.out.println("");
                System.out.println("--SENDING--");
                String messageFromServer = "Server: "   + serverName + " " + serverNum;
                System.out.println("--SENDING--");
                System.out.println("");

                bufferedWriter.write(messageFromServer);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (clientNum < 1 || clientNum > 100){
                    server.closeServer();
                    break;
                } 
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            closeThread(socket, bufferedReader, bufferedWriter);
        }
    }


    /* TO DO CLOSE THE THREAD */
    public void shutdown() {
        closeThread(socket, bufferedReader, bufferedWriter);
    }

    public void closeThread(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        serverThreads.remove(this);

        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
} 
