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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void StartClient(){

        //fluxe de dades:
        try{
            ois = new DataInputStream(socket.getInputStream());  //dades d'entrada
            oos = new DataOutputStream(socket.getOutputStream()); //dades de sortida
        }catch(IOException e){
            System.out.println("Error al crear els fluxes de dades: "+e.getMessage());
        }
        
        //rebre dades:
        try{
            String message = "";    //missatge rebut que es va completant
            while(!message.equals("exit")){  //la connexió acaba al rebre "exit"
                message = (String)ois.readUTF();    //llegim el missatge del client
                System.out.println(message);
            }
            //tancar connexió:
            System.out.println("Client: Connexió tancada");
            ois.close();
            oos.close();
            socket.close();
        }catch(IOException e){
            System.out.println("Error al enviar dades: "+e.getMessage());
        }
    }

    public void enviar(String message){
        try{
            //enviar dades:
            oos.writeUTF(message);  //enviem el missatge al client
            oos.flush();   //netejem el buffer
        }catch(IOException e){
            System.out.println("Error al enviar dades: "+e.getMessage());
        }
    }

    public void readMsg(){
        while (true) {
            enviar(teclado.nextLine());
        }
    }

    public void executarConnexio(){
        Thread t = new Thread(new Runnable(){
            public void run(){
                StartClient();
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.executarConnexio();
        
        client.readMsg();
    }

}
