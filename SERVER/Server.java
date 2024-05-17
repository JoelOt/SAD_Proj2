package SERVER;

import java.util.concurrent.ConcurrentHashMap;

import CLIENT.MySocket;

public class Server {
    private MyServerSocket myServerSocket;
    private ConcurrentHashMap<String, MySocket> clientsMap;
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        try { //(inicialitzacio) nose com treure que no llançi la excepcio
            System.out.println("Server is running...");
            server.clientsMap = new ConcurrentHashMap<String, MySocket>();
            server.myServerSocket = new MyServerSocket(Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {  //bucle infinit per anar rebent clients
            MySocket mySocket = server.myServerSocket.accept(); // acceptem un client nou
            String nick = mySocket.receiveMessage(); // rebem el nick del client
            if(server.clientsMap.containsKey(nick)){  //si ja existeix el nick tanquem la connexió
                System.out.println("Nick " + nick + " ja registrat");
                mySocket.close();
                Thread.sleep(2000);  //pauseta flow csma
            }else{
                server.clientsMap.put(nick, mySocket);
                System.out.println("    - Client '" + nick + "' posat al mapa");
                Thread clientThread = new Thread(server.new ClientThread(mySocket, nick)); // aquest thread gestiona
                clientThread.start();
            }
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
            clientSocket.sendMessage("Benvingut al Servidor!");
            String msg = "";
            while (msg != null) {
                msg = clientSocket.receiveMessage();
                System.out.println("Client " + clientNick + " : " + msg);
                clientSocket.sendMessage(msg);
            }
            clientSocket.close();
        }

    }
}
