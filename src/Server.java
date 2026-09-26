import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*

server
- gen server name
- wait for client connection
...
- once connected:
- take in init client message
- print client name  and server name
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
    public static void main(String[] args){

    Socket socket = null;
    InputStreamReader yn_reader = null;
    OutputStreamWriter op_writer = null;

    BufferedReader buff_reader = null;
    BufferedWriter buff_writer = null;

    ServerSocket server_socket = null;
    String server_name = null;

    

        while (true) {
            try {
                server_socket = new ServerSocket(1234);
                socket = server_socket.accept();

                yn_reader = new InputStreamReader(socket.getInputStream());
                op_writer = new OutputStreamWriter(socket.getOutputStream());

                buff_reader = new BufferedReader(yn_reader);
                buff_writer = new BufferedWriter(op_writer);

                Scanner s = new Scanner(System.in); 

                while (true){
                    String client_msg = buff_reader.readLine();
                    // parse client_msg
                    //print client_msg

                    System.out.println("Insert an integer (0 ~ 100): ");
                    int input = s.nextInt();

                    //use server_msg and print here
                    

                    buff_writer.write("");
                    buff_writer.newLine(); 
                    buff_writer.flush();
                    
                    //check client_n value from client_msg
                    if (client_n > "100" || client_n < 1)
                        break;
                }

                socket.close();
                yn_reader.close();
                op_writer.close();
                buff_reader.close();
                buff_writer.close();
                server_socket.close();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}