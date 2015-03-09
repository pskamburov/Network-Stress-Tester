/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author petar
 */
public class TestServer {

    public static void main(String[] args) {

        MultiThreadedServer server = new MultiThreadedServer(8080);
        new Thread(server).start();

        try {
            Thread.sleep(500 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }

}
