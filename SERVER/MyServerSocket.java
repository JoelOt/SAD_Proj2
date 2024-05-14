package SERVER;

import java.io.*;
import java.net.*;

public class MyServerSocket extends ServerSocket {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public MyServerSocket(int port) throws IOException{
        super(port);
        accept();
        executarConnexio();
        try{
            
        }catch(Exception e){
            System.out.println("Error creating server socket");
        }
    }

    @Override
    public Socket accept() throws IOException {
        socket = super.accept();
        System.out.println("Client " + socket.getInetAddress().getHostName() + " connected...");  //client connectat
        InputStream input = socket.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(input));
        OutputStream output = socket.getOutputStream();
        printWriter = new PrintWriter(output, true);
        return socket;
    }

    public String receiveMessage() throws IOException {
        try {
            String msg = bufferedReader.readLine();
            return msg;
        } catch (IOException e) {
           System.out.println("Error receiving message");
           return null;
        }
    }

    public void sendMessage(String message) {
        try {
            printWriter.println(message);
            System.out.println("Server: " + message);
        } catch (Exception e) {
            System.out.println("Error sending message");
        }
    }

    public void close() throws IOException {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
            }
        } finally {
            if (printWriter != null) {
                printWriter.close();
                printWriter = null;
            }
            super.close();
        }
    }

    public void executarConnexio(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                String msg = "";
                while (msg != null) {
                    try{    
                    msg = receiveMessage();
                    System.out.println("Rebut de " + socket.getInetAddress().getHostName() + ": " + msg);
                    sendMessage(msg);
                    } catch (Exception e) {
                        System.out.println("Error receiving message");
                    }
                }
            }
        });
        t.start();
    }
}
