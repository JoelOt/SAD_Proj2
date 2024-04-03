package SERVER;

import java.net.*;
import java.io.*;

public class Server {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream dataInput=null;
    private DataOutputStream dataOutput=null; 

    public Server(){
    
    }
    public void iniciarConnexio(){
        try {
            try{
                socket = serverSocket.accept(); // acceptem la connexió del client
                System.out.println("Client " + socket.getInetAddress().getHostName() + " connected...");  //client connectat
            }catch(Exception e){
                System.out.println("Error al conectar: "+e.getMessage());
            }
            dataInput = new DataInputStream(socket.getInputStream());  //dades d'entrada
            dataOutput = new DataOutputStream(socket.getOutputStream()); //dades de sortida
            dataOutput.flush();  //netejem el buffer

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String rebreDades(){  //rebem dades del client i les mostrem per pantalla
        try{
            return (String)dataInput.readUTF();    //llegim el missatge del client
        }catch(IOException e){
            System.out.println("Error al rebre dades: "+e.getMessage());
                return null;
            }
    }

    public void enviar(String message){  //ara per ara envia un echo
        try{
            dataOutput.writeUTF(message);  //enviem el missatge al client
            dataOutput.flush();   //netejem el buffer
            System.out.println("Enviat echo : " + message);
        }catch(IOException e){
            System.out.println("Error al enviar dades: "+e.getMessage());
        }
    }
    public void tancarConnexio(){
        try { 
            System.out.println("Server : Connexió tancada");
            dataInput.close();
            dataOutput.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executarConnexio(){
        Thread t = new Thread(new Runnable(){
            public void run(){
                iniciarConnexio();
                String msg = "";
                while(!msg.equals("exit")){  //la connexió acaba al rebre "exit"
                    msg = rebreDades();
                    System.out.println("Rebut de " + socket.getInetAddress().getHostName() + ": " + msg);
                    enviar(msg);  //enviem echo
                }
                tancarConnexio();
            }
        });t.start();
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        try{
        server.serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Server is running...");
        }catch(Exception e){
            System.out.println("Error al crear el socket: "+e.getMessage());
        }

        server.executarConnexio();  //rebre dades del client
    }
}


//passar args desde terminal: MyServerSoccket ss = new MyServerSoccket(Integer.parseInt(args[0]));
