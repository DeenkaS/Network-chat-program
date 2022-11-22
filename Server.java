import java.net.*;
import java.io.*;
public class Server {
    Socket socket;
    ServerSocket server;

    //server constructor
    public Server(ServerSocket server){
        this.server = server;
    }
    //open server
    public void ServerStart() throws IOException{
        System.out.println("Server up & Running\nWaiting for Clients...");
        //Keep server open until server.close().
            while(!server.isClosed()){
                socket = server.accept();   //Accept incoming requests
                ClientHandler handler = new ClientHandler(socket); //Allocate new clienthandler to client
                System.out.println(handler.clientName + " has connected");
                Thread t = new Thread(handler); //run handler on new thread
                t.start();
         }
    }

    public static void main(String[] args){
        try{
            ServerSocket server = new ServerSocket(8080);
            Server chatServer = new Server(server);
            chatServer.ServerStart();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
