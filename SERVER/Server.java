package SERVER;

import java.util.concurrent.ConcurrentHashMap;

import CLIENT.MySocket;

public class Server {
    private MyServerSocket myServerSocket;
    private ConcurrentHashMap<String, MySocket> clientsMap;

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        try {
            System.out.println("Server is running...");
            server.clientsMap = new ConcurrentHashMap<String, MySocket>();
            server.myServerSocket = new MyServerSocket(Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MySocket mySocket = null;
        String nick = "a";
        while (true) {
            System.out.println("Esperant Client...");
            mySocket = server.myServerSocket.accept();  //acceptem un client nou (es bloquejant)
            //nick = mySocket.receiveMessage();
            System.out.println("Nick: " + nick);
            if (server.clientsMap.containsKey(nick) || nick == null) {
                System.out.println("Nickname ya existeix: " + nick);
                mySocket.close();
                continue;
            }
            server.clientsMap.put(nick, mySocket);
            System.out.println("Client " + nick + " posat al mapa");
            Thread clientThread = new Thread(server.new ClientThread(mySocket, nick)); // aquest thread gestiona aquest client en concret
            clientThread.start();
            Thread.sleep(1000);
        }
    }
    class ClientThread implements Runnable {
        private MySocket clientSocket;
        private String clientNick;

        public ClientThread(MySocket clientSocket, String nick) {
            this.clientSocket = clientSocket;
            this.clientNick = nick;
        }

        @Override
        public void run() {
            try {
                clientSocket.sendMessage("Benvingut al Servidor!");
                String msg = "";
                while (msg != null) {
                    msg = clientSocket.receiveMessage();
                    System.out.println("Client " + clientNick + " : " + msg);
                    clientSocket.sendMessage(msg);
                }
                clientSocket.close();
            } catch (Exception e) {
                System.out.println("Error receiving message");
            }
        }

    }
}
