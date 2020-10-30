package edu.jhu.network;

import edu.jhu.controller.RatesController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class used to span a new runnable for threat
 */
public class Runnable implements java.lang.Runnable {
    // maintains socket reference
    protected Socket socket;

    /**
     * Public constructor
     * @param socket connecting socket
     */
    public Runnable(Socket socket) {
        // pass to local member
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // use try with resources to manage input and output
        try (
                InputStreamReader input = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(input);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // get data from client
            String data = reader.readLine();
            // create a new instance of price controller and pass data to constructor
            out.println(new RatesController(data).getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
