package CLIENT;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private Socket socket;
    private DataInputStream dataInput = null;
    private DataOutputStream dataOutput = null;
    private Scanner teclado = new Scanner(System.in);

    public Client() {

    }

    public void iniciarConnexio() {
        try {
            dataInput = new DataInputStream(socket.getInputStream()); // dades d'entrada
            dataOutput = new DataOutputStream(socket.getOutputStream()); // dades de sortida
            dataOutput.flush(); // netejem el buffer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String rebreDades() { // rebem dades del server i les mostrem per pantalla
        try {
            return (String) dataInput.readUTF(); // llegim el missatge del client
        } catch (IOException e) {
            System.out.println("Error al rebre dades: " + e.getMessage());
            return null;
        }
    }

    public void enviar(String message) {
        try {
            // enviar dades:
            dataOutput.writeUTF(message); // enviem el missatge al client
            dataOutput.flush(); // netejem el buffer
            System.out.println("Client: " + message);
        } catch (IOException e) {
            System.out.println("Error al enviar dades: " + e.getMessage());
        }
    }

    public void tancarConnexio() {
        try {
            System.out.println("Client: Connexió tancada");
            dataInput.close();
            dataOutput.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error al enviar dades: " + e.getMessage());
        }
    }

    public String readMsg() { // llegim el que escriu el client i ho enviem al server. modificar per utilitzar
                              // bufferedReader
        return teclado.nextLine();
    }

    public void executarConnexio() { // executem la connexió amb el server
        Thread t = new Thread(new Runnable() {
            public void run() {
                String msg = "";
                while (!msg.equals("exit")) { // la connexió acaba al rebre "exit"
                    msg = rebreDades();
                    System.out.println("Server: " + msg);
                }
                tancarConnexio();
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.socket = new Socket(args[0], Integer.parseInt(args[1]));
            System.out.println("Client is running...");
        } catch (Exception e) {
            System.out.println("Error al crear el socket: " + e.getMessage());
        }
        client.iniciarConnexio();
        client.executarConnexio(); // rebre dades del server

        new Thread() {
            public void run() {
                while (true) { // enviar dades al server
                    String inputMsg = client.readMsg();
                    client.enviar(inputMsg);
                }
            }
        }.start();
    }
}