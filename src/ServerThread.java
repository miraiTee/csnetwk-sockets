import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/* 
server thread
- gen server name
- wait for client connection
...
- once connected:
- take in init client message
- print client name  and server name
- input 1~100 as server int
- display client int, server int, sum(c,s)
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

                String[] arr = messageFromClient.split(": ");
                this.clientName = arr[0];
                this.clientNum = Integer.parseInt(arr[1]);

                Scanner s = s.nextInt();

                int sum = clientNum + serverNum;

                String messageFromServer = "Client: " + clientName +
                    ", client number: " + clientNum +
                    ", server number: " + serverNum +
                    ", sum: " + sum;

                bufferedWriter.write(messageFromServer);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e){
            }
        }
    }

    /* TO DO CLOSE THE THREAD */
}
