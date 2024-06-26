package CLIENT;

import java.io.*;
import java.net.*;

public class MySocket extends Socket {
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private InputStream input;
    private OutputStream output;

    public MySocket(String host, int port){
        super();
        try {
            super.connect(new InetSocketAddress(host, port));
            this.input = super.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            this.output = super.getOutputStream();
            printWriter = new PrintWriter(output, true);
        } catch (Exception e) { 
            System.out.println("Error connecting to " + host + ":" + port);
        }
    }

    public MySocket(Socket socket, InputStream input, OutputStream output) {
        super();
        try {
            this.input = input;
            this.output = output;
            super.connect(new InetSocketAddress(socket.getInetAddress(), socket.getLocalPort()));
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            printWriter = new PrintWriter(output, true);
        } catch (Exception e) {
            System.out.println("Error initializing MySocket with Socket");
        }
    }

    public String receiveMessage(){
        try {
            System.out.println("    - Esperant msg...");
            return bufferedReader.readLine();
        } catch (IOException e) {
           System.out.println("Error receiving message : " + e.getMessage());
           return null;
        }
    }

    public void sendMessage(String message){
        try {
            printWriter.println(message);
        } catch (Exception e) {
            System.out.println("Error sending message");
        }
    }

    @Override
    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
            }
            if (printWriter != null) {
                printWriter.close();
                printWriter = null;
            }
            super.close();
            System.out.println("Socket closed");
        } catch(Exception e) {
            System.out.println("error closing Socket");
        }
    }
}
