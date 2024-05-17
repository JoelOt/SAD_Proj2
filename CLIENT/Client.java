package CLIENT;

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
        client.mySocket.sendMessage(client.nick); //enviem el nick al servidor
        new Thread() { // thread d'enviar missatges
            public void run() {
                    while (true) {
                        String inputMsg = client.teclado.nextLine();
                        if (inputMsg != null) {
                            client.mySocket.sendMessage(inputMsg);
                        }
                    }
            }
        }.start();
        Thread t = new Thread(new Runnable() { // thread de rebre missatges
            public void run() {
                String msg = "";
                while (msg != null) { // la connexió acaba al rebre "exit"
                    msg = client.mySocket.receiveMessage();
                    System.out.println("Server: " + msg);
                }
            }
        });
        t.start();

    }
}
