package client;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;
import utility.PasswordHasher;




public class Client {

    final String ADDRESS = "localhost";
    final int PORTNR = 3000;

    private String address;
    private int portnr;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;

    public Client(InputStream inputStream){
        this.address = ADDRESS;
        this.portnr = PORTNR;
        this.scanner = new Scanner(inputStream);
    }

    public void sendToServer(String string){
        if(string != null){
            writer.println(string);
        }
    }

    public String readFromServer(){
        String response = "";
        try {
            if (reader.ready()) {
                response = reader.readLine();
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        finally {
            return response;
        }
    }

    public String getPassword(){
        System.out.println("Skriv inn passord: \n");
        return scanner.nextLine();
    }

    public String getAddress(){
        return address;
    }

    public int getPortnr(){
        return portnr;
    }

    public boolean getConnection(){
        try {
            Socket socket = new Socket(address, portnr);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isr);
            writer = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Client client = new Client(System.in);
        String salt = "4bb31cab089fbe5751920e896fd0ddff";
        client.getConnection();
        String response = "";
        String password = "";
        PasswordHasher hasher = new PasswordHasher();

        while(!password.equals("exit")){
            password = client.getPassword();
            client.sendToServer(hasher.SHA1hash(password, 2048, salt));
            while(response.equals("")){
                response = client.readFromServer();
            }
            System.out.println(response);
            response = "";
        }
    }
}
