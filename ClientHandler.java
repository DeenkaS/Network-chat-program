import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    
    public static ArrayList <ClientHandler> clientArray = new ArrayList<>();
    private Socket socket;
    private BufferedReader BR;
    private BufferedWriter BW;
    public String clientName;

    public ClientHandler (Socket socket){
        try{
        this.socket = socket;
        this.BW = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.BR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientName = BR.readLine();
        clientArray.add(this);
        ServerMessage("Server: " + clientName + " has joined the chat");
        } catch (IOException e){
            CloseEverything(socket,BR,BW);
        }
    }

    @Override
    public void run(){
        String messageHold;

        while(socket.isConnected()){
            try{
                messageHold = BR.readLine();
                ServerMessage(messageHold);
            } catch (IOException e){
                CloseEverything(socket,BR,BW);
                break;
            }
        }

    }
    //Sends message to all clients but the sender.
    public void ServerMessage(String sentMessage){
        for(ClientHandler clientThread : clientArray){
            try{
                if(clientThread.socket.getPort()!=(socket.getPort())){
                    clientThread.BW.write(sentMessage);
                    clientThread.BW.newLine();
                    clientThread.BW.flush();
                }
            } catch(IOException e){
                CloseEverything(socket,BR,BW);
            }
        }
    }
    //Removes clientThread from clientArrayList upon request
    public void KillClient(){
        for(ClientHandler clientThread : clientArray){
            if(!this.equals(clientThread)){
                clientThread = null;
                ServerMessage("Server: " + this.clientName + " has been slain");
            }
        }
    }
    //Kills clientThread and closes it's socket and IOs
    public void CloseEverything(Socket socket, BufferedReader br, BufferedWriter bw){
        KillClient();
        try {
            socket.close();
            br.close();
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
