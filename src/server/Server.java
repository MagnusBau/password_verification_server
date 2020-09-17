package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

import utility.PasswordHasher;


public class Server {

    final static int portnr = 3000;
    private PrintWriter writer = null;
    private BufferedReader reader = null;


    public Server(){
        try{
            ServerSocket serverSocket = new ServerSocket(portnr);
            Socket clientSocket = serverSocket.accept();
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public String readFromClient(){
        String line = "";
        try {
            if (reader.ready()) {
                line = reader.readLine();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return line;
    }

    public boolean writeToClient(String message){
        try {
            writer.println(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        // passordet er passord123
        boolean run = true;
        String query = "";
        Server server = new Server();
        String salt = "7f4f3cce4a23b2040d98f648c35eda30";
        String hash = "2f680f0c32a8e994a3a3472167472336";
        String exit = "4176b96d192e037ea50179ed0c2bb068";

        PasswordHasher hasher = new PasswordHasher();

        while (run){
            query = server.readFromClient();
            if(!query.equals("")){
                System.out.println(query);
                if(query.equals(exit)){
                    run = false;
                }
                if(hasher.SHA1hash(query, 2048, salt).equals(hash)){
                    System.out.println("passord ok");
                    server.writeToClient("Passord ok");
                }else{
                    System.out.println("Passord ikke ok");
                    server.writeToClient("passord ikke ok");
                }
            }
        }
    }
}
