package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 */
public class WorkerRunnable implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream());
//            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
//            output.write(("HTTP/1.1 200 OK\r\n\r\nWorkerRunnable: "
//                    + this.serverText + " - "
//                    + time
//                    + "").getBytes());
//            output.close();
            InputStream inStream = clientSocket.getInputStream();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(inStream));
            System.out.println(rd.readLine());
            requestWriter.println("HTTP/1.1 200 OK");
            requestWriter.flush();
            requestWriter.close();
            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
