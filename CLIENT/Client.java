package CLIENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {
    private MySocket mySocket;
    private Scanner teclado = new Scanner(System.in);

    


    public static void main(String[] args) {
        Client client = new Client();
        client.mySocket = new MySocket(args[0], Integer.parseInt(args[1]));  //ya es fa iniciarConnexio

        new Thread() {
            public void run() {
                try{
                while (true) { // enviar dades al server
                    BufferedReader in = new BufferedReader(
                    new InputStreamReader(System.in));
                    //String inputMsg = in.readLine();
                    String inputMsg = client.teclado.nextLine();
                    client.mySocket.sendMessage(inputMsg);
                }
            } catch (Exception e) {
                System.out.println("Error sending message");
            }
        }}.start();

    }
}
