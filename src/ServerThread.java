import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;

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
    private Socket socket;

    private String serverName;
    private int serverNum;

    private String clientName;
    private int    clientNum;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ServerThread(Socket socket, String serverName){
        try {
            this.socket = socket;
            this.serverName = serverName;

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e){
        }
    }

    @Override public void run(){
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();

                String[] arr = messageFromClient.split(" ");
                this.clientName = arr[0];
                this.clientNum = Integer.parseInt(arr[1]);

                serverNum = (int)(Math.random() * 100) + 1;
                int sum = clientNum + serverNum;

                System.out.println();
                System.out.println("Client:" + clientName + "has chosen" + clientNum);
                System.out.println("Server:" + serverName + "generated" + serverNum);
                System.out.println("Total:" + sum);
                System.out.println();

                System.out.println();
                System.out.println("--SENDING--");
                String messageFromServer = 
                    "Server: "   + serverName + " " + serverNum;
                System.out.println("--SENDING--");
                System.out.println();

                bufferedWriter.write(messageFromServer);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (clientNum < 1 || clientNum > 100){
                    // terminate all other threads
                    // close server
                } 

            } catch (IOException e){
            }
        }
    }

    /* TO DO CLOSE THE THREAD */
}
