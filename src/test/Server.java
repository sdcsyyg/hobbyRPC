package test;

import java.io.IOException;

import business.BusinessHandler;
import rpc.RPC;

/**
 * As a server to test whether RPC works
 * @author sdcsyyg
 */
public class Server {

    public static final int PORT = 21329;

    public static void main(String[] args) {
        try {

            // Get a server to provide
            new Thread(new RPC(new BusinessHandler(), PORT)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
