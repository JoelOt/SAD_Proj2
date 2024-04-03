package CLIENT;

import java.net.*;
import java.util.Scanner;
import java.io.*;


public class Client {
    private Socket socket;
    private DataInputStream ois=null;
    private DataOutputStream oos=null; 
    private Scanner teclado=new Scanner(System.in);


    public Client(){
        try {
            socket = new Socket("localhost", 5050);
            ois = new DataInputStream(socket.getInputStream());  //dades d'entrada
            oos = new DataOutputStream(socket.getOutputStream()); //dades de sortida
            oos.flush();  //netejem el buffer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String rebreDades(){  //rebem dades del server i les mostrem per pantalla
        try{
            return (String)ois.readUTF();    //llegim el missatge del client
        }catch(IOException e){
            System.out.println("Error al rebre dades: "+e.getMessage());
            return null;
        }
    }

    public void enviar(String message){
        try{
            //enviar dades:
            oos.writeUTF(message);  //enviem el missatge al client
            oos.flush();   //netejem el buffer
            System.out.println("Client: " + message);
        }catch(IOException e){
            System.out.println("Error al enviar dades: "+e.getMessage());
        }
    }


    public void tancarConnexio(){ 
        try{
            System.out.println("Client: Connexió tancada");
            ois.close();
            oos.close();
            socket.close();
        }catch(IOException e){
            System.out.println("Error al enviar dades: "+e.getMessage());
        }
    }


    public String readMsg(){  //llegim el que escriu el client i ho enviem al server. modificar per utilitzar bufferedReader
        return teclado.nextLine();
    }

    public void executarConnexio(){  //executem la connexió amb el server
        Thread t = new Thread(new Runnable(){            
            public void run(){
                String msg = "";
                while(!msg.equals("exit")){  //la connexió acaba al rebre "exit"
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
        client.executarConnexio();  //rebre dades del server
        
        while(true){  //enviar dades al server
            String inputMsg = client.readMsg();
            client.enviar(inputMsg);
        }
    }
}


/*
 creo el socket amb la ip del servidor i el port 5050
 creo els objectes per llegir i escriure dades (dataStream)
 creo 
 */