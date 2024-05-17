package CLIENT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {
    private MySocket mySocket;
    private Scanner teclado = new Scanner(System.in);
    private String nick;

    private Client(String nick) {
        this.nick = nick;
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(args[2]);
        client.mySocket = new MySocket(args[0], Integer.parseInt(args[1]));
        client.mySocket.sendMessage(client.nick);
        new Thread() { // thread d'enviar missatges
            public void run() {
                try {
                    while (true) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        String inputMsg = client.teclado.nextLine();
                        if (inputMsg != null) {
                            client.mySocket.sendMessage(inputMsg);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error sending message");
                }
            }
        }.start();

        Thread t = new Thread(new Runnable() { // thread de rebre missatges
            public void run() {
                String msg = "";
                while (msg != null) { // la connexió acaba al rebre "exit"
                    try {
                        msg = client.mySocket.receiveMessage();
                        System.out.println("Server: " + msg);
                    } catch (Exception e) {
                        System.out.println("Error receiving message: " + e.getMessage());
                    }
                }
            }
        });
        t.start();

    }
}
