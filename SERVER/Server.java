package SERVER;

import java.net.*;
import java.io.*;

public class Server {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream ois=null;
    private DataOutputStream oos=null; 

    public Server(){
        try {
            serverSocket = new ServerSocket(5050);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startServer(){

        //aixecar connexió:
        try {
            System.out.println("Server is running...");
            try{
                socket = serverSocket.accept(); // acceptem la connexió del client
                System.out.println("Client" + socket.getInetAddress().getHostName() + "connected...");  //client PC-1 connectat
            }catch(Exception e){
                System.out.println("Error al conectar: "+e.getMessage());
            }

            //fluxe de dades:
            ois = new DataInputStream(socket.getInputStream());  //dades d'entrada
            oos = new DataOutputStream(socket.getOutputStream()); //dades de sortida
            oos.flush();  //netejem el buffer

            //rebre dades:
            String message = "";    //missatge rebut que es va completant
            while(!message.equals("exit")){  //la connexió acaba al rebre "exit"
                message = (String)ois.readUTF();    //llegim el missatge del client
                System.out.println(message);

                //enviar dades:
                oos.writeUTF(message);  //enviem el missatge al client
                oos.flush();   //netejem el buffer
            }

            //tancar connexió:
            System.out.println("Server: Connexió tancada");
            ois.close();
            oos.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executarConnexio(){
        Thread t = new Thread(new Runnable(){
            public void run(){
                startServer();
            }
        });t.start();
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.executarConnexio();
    }
}