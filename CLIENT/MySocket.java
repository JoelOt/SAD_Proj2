package Client;

import java.io.*;
import java.net.*;

public class MySocket extends Socket {
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public MySocket(String host, int port){
        super();
        try {
            super.connect(new InetSocketAddress(host, port));
            iniciarConnexio();
            executarConnexio();
        } catch (Exception e) {
            System.out.println("Error connecting to " + host + ":" + port);
        }
    }


    private void iniciarConnexio() throws Exception {
        try {
            InputStream input = super.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = super.getOutputStream();
            printWriter = new PrintWriter(output, true);
            System.out.println("Client is running...");
        } catch (Exception e) {
            System.out.println("Error creating input/output streams");
        }
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

    public void sendMessage(String message) throws Exception {
        try {
            printWriter.println(message);
            System.out.println("Client: " + message);
        } catch (Exception e) {
            System.out.println("Error sending message");
        }
    }

    @Override
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

    public void executarConnexio() { // executem la connexió amb el server

        Thread t = new Thread(new Runnable() {
            public void run() {
                String msg = "";
                while (msg != null) { // la connexió acaba al rebre "exit"
                    try{    
                    msg = receiveMessage();
                    System.out.println("Server: " + msg);
                    } catch (Exception e) {
                        System.out.println("Error receiving message");
                    }
                }
            }
        });
        t.start();
    }
}
