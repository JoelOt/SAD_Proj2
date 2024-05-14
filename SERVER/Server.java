package SERVER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private MyServerSocket myServerSocket;


    public static void main(String[] args) {
        Server server = new Server();
        try {
            System.out.println("Server is running...");
            server.myServerSocket = new MyServerSocket(Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
