import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private Server server;
    private boolean threadClosed = false;

    private String serverName;
    private int serverNum;

    private String clientName;
    private int clientNum;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ServerThread(Socket socket, String serverName, Server server) throws IOException {

        this.socket = socket;
        this.serverName = serverName;
        this.server = server;

        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public boolean listenMessage() throws IOException {
        String messageFromClient = bufferedReader.readLine();

        if (messageFromClient == null) {
            System.out.println("\nClient closed the connection.");
            return false;
        }

        System.out.println("\n-- RECEIVING --");
        System.out.println(messageFromClient + "\n");

        String[] parts = messageFromClient.split(":", 2);

        if (parts.length != 2) {
            throw new IOException("Invalid client message format.");
        }

        clientName = parts[0];
        try {
            clientNum = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IOException("Client number is not an integer.", e);
        }

        serverNum = (int) (Math.random() * 100) + 1;

        int sum = clientNum + serverNum;

        System.out.println("\n-- SUMMARY --");
        System.out.println("Client: " + clientName + " has chosen " + clientNum);
        System.out.println("Server: " + serverName + " generated " + serverNum);
        System.out.println("Total: " + sum);
        System.out.println("-------------\n");

        return true;
    }

    public void sendMessage() throws IOException {
        String messageToSend = serverName + ":" + serverNum;

        bufferedWriter.write(messageToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @Override
    public void run() {
        try {
            boolean receivedMessage = listenMessage();

            if (!receivedMessage) {
                return;
            }

            sendMessage();

            if (clientNum < 1 || clientNum > 100) {
                server.closeServer();
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
            }

        } finally {
            closeThread();
        }
    }

    public void closeThread() {    
        if (threadClosed) {
            return;
        }

        threadClosed = true;

        if (server != null) {
            server.removeThread(this);
        }

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
