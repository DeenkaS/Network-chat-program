import java.io.*;
import java.net.*;
import java.util.*;

public class Client{
    Socket socket;
    BufferedReader read;
    BufferedWriter write;
    String name;
    //Client constructor
    public Client(Socket socket, String name){
        try{
            this.socket = socket;
            this.read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.name = name;
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void clientSendMessage(){
        try{
            write.write(name); 
            write.newLine();
            write.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                write.write(name + ": " + message);
                write.newLine();
                write.flush();
            }
            scanner.close();
        }
        catch(Exception e){
            System.out.println("Error occured: " + e);
            try{
                socket.close();
            }
            catch(Exception E){
                System.out.println("Could not close socket. Error: " + E);
            }
        }
    }

    public void clientReceiveMessage(){
        //Creates new runnable object inside a new thread.
        //instead of implementing runnable this seemed like an intersting
        //way of implementing a thread xD
        new Thread(new Runnable(){
            @Override
            public void run(){
                String messageToDisplay;

                    while(socket.isConnected()){
                        try{
                            messageToDisplay = read.readLine();
                            System.out.println(messageToDisplay);
                        }
                        catch(Exception e){
                            //System.out.println(e);
                            try{
                                System.out.println("SERVER DISCONNECTED");
                                socket.close();
                                break;
                            }
                            catch(Exception E){
                                System.out.println("Could not close socket. Error: " + E);
                                break;
                            }
                        }
                    }
            }
        }).start(); //thread starts running
    }

    public static void main(String[] args){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter your name before joining the chat: ");
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost", 8080);
            Client client = new Client(socket, name);
            client.clientReceiveMessage();
            client.clientSendMessage();
            scanner.close();
        }
        catch(Exception e){
            System.out.println("Error occured: " + e);
        }
    }

}