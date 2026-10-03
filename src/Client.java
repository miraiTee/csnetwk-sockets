
/*  
    CSNETWK S03
    *Mirai & Ram
    (Java Server)
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/*
    Client opens and searches for the server, if the server is not found.
    It waits for a while until it deems reachable. THe client then sends a message
    and await for a reply from the server. after which it terminates itself.
*/

public class Client {
    private Socket socket;

    private String serverName;
    private int serverNum;

    private String clientName;
    private int clientNum;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket, String clientName, int clientNum) throws IOException {
        this.socket = socket;
        this.clientName = clientName;
        this.clientNum = clientNum;

        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage() throws IOException {
        String messageToSend = clientName + ":" + clientNum;

        bufferedWriter.write(messageToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        System.out.println("Sent message to server...");
    }

    public boolean listenMessage() throws IOException {
        String messageFromServer = bufferedReader.readLine();

        if (messageFromServer == null) {
            System.out.println("Server closed the connection.");
            return false;
        }
            
        System.out.println("Received response from server...");

        String[] parts = messageFromServer.split(":", 2);

        if (parts.length != 2) {
            throw new IOException("Invalid server message format.");
        }

        serverName = parts[0];

        try {
            serverNum = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IOException("Server sent an invalid integer.", e);
        }

        int sum = clientNum + serverNum;

        System.out.println("\n--- SUMMARY ---");
        System.out.println("Client name: " + clientName);
        System.out.println("Server name: " + serverName);
        System.out.println("Client number: " + clientNum);
        System.out.println("Server number: " + serverNum);
        System.out.println("Sum: " + sum);
        System.out.println("--------------\n");

        return true;
    }

    public void closeClient() {
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

        System.out.println("> Closing client socket... goodbye!");
    }

    private static Socket connectToServer() throws InterruptedException {
        final int maxAttempts = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("Trying to connect to server... Attempt " + attempt + "/" + maxAttempts);
                Socket socket = new Socket("localhost", 6767);
                System.out.println("Connected to server...");
                return socket;
            } catch (IOException e) {
                System.out.println("Connection attempt failed.");

                if (attempt == maxAttempts) {
                    System.out.println("Could not connect after " + maxAttempts + " attempts. Exiting.");
                    return null;
                }

                System.out.println("Retrying in 30 seconds...");
                Thread.sleep(30_000);
            }
        }

        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /* Input Name */
        boolean loop = true;
        String clientName = null;

        while (loop) {
            System.out.print("Enter client name (letters only): ");
            String input = scanner.nextLine().trim();

            if (input.matches("[A-Za-z]+")) {
                clientName = input;
                loop = false;
            }

            if (loop){
                System.out.println("Invalid name. Use letters only.\n");
            }
        }

        /* Input Number */
        int clientNum = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Enter an integer (1~100) | Out of Range Int will end server |: ");

            if (scanner.hasNextInt()) {
                clientNum = scanner.nextInt();
                validInput = true;
            } else {
                System.out.println("Please enter a valid integer.");
                scanner.next();
            }
        }

        Client client = null;

        try {
            Socket socket = connectToServer();

            if (socket == null) {
                System.out.println("Client terminated.");
                return;
            }

            client = new Client(socket, clientName, clientNum);

            client.sendMessage();
            client.listenMessage();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Client connection attempt was interrupted.");
        } finally {
            if (client != null) {
                client.closeClient();
            }

            scanner.close();
        }
    }
}
