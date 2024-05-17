package SERVER;

import java.io.*;
import java.net.*;
import CLIENT.MySocket;

public class MyServerSocket extends ServerSocket {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public MyServerSocket(int port) throws IOException {
        super(port);
        try {

        } catch (Exception e) {
            System.out.println("Error creating server socket");
        }
    }
    @Override
    public Socket accept() {
        try {
            socket = super.accept();
            MySocket mySocket = new MySocket(socket, socket.getInputStream(), socket.getOutputStream());
            
            System.out.println("Client " + " connected..." + socket.getInetAddress()); // client connectat
            return mySocket;
        } catch (Exception e) {
            System.out.println("Error accepting client connection");
        }
        System.out.println("Client not accepted");
        return null;
    }

    public String receiveMessage() throws IOException {
        try {
            String msg = bufferedReader.readLine();
            return msg;
        } catch (IOException e) {
            System.out.println("Serv: Error receiving message" + e.getMessage());
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
        } catch (Exception e) {
            System.out.println("Error closing server socket");
        }
    }

}
