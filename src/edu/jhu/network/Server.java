package edu.jhu.network;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server class
 */
public class Server {
    // method used to initiate the server
    public void start() {
        // create port to establish connection
        int port_number = 20022;
        boolean listening = true;
        // bind socket to the specified local port number using try with resources
        try (
                ServerSocket serverSocket = new ServerSocket(port_number)
        ) {
            // keep the program running forever
            while (listening) {
                // create a new thread and start runnable
                new Thread(new Runnable(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // notify if error happened during socket binding
            System.out.println("-0.01:Error binding socket to port " + port_number);
        }
    }

}
