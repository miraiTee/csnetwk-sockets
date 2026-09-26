public class ServerMessage {
    String client;
    int client_n;
    
    String server;
    int server_n;

    int sum;

    public ServerMessage(String client, int client_n, String server, int server_n)
    {
        this.client = client;
        this.client_n = client_n;
        this.server = server;
        this.server_n = server_n;
        this.sum = client_n + server_n;
    }

    @Override
    public String toString(){
        return "Client:" + client + " " + "CN:" + client_n + " " +
        "Server:" + server + " " + "SN:" + server_n + " " + 
        "Sum:" + sum;
    }
}
