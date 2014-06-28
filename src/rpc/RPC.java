package rpc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import business.Business;

/**
 * As a RPC server to receive cilent's request
 * @author sdcsyyg
 */
public class RPC extends ServerSocket implements Runnable {

    public static final String HOST = "localhost";
    public static final int port = 21329; // Reverse of my birthday, because 92312 is out of range for OS ports

    public Business handler;

    public RPC(int port) throws IOException {
        super(port);
    }

    /**
     * Get a server which can do any business mothers
     * @param handler which implements Business
     * @param port server port to listen
     * @throws IOException
     */
    public RPC(Business handler, int port) throws IOException {
        super(port);
        this.handler = handler;
    }

    @Override
    public void run() {
        System.out.println("RPC server listening at port : " + this.getLocalPort());
        System.out.println("RPC server listening for this type of job : " + Business.class.getName());
        while(true) {
            try {
                Socket sck = this.accept();
                Tasker tasker = new Tasker(sck);
                new Thread(tasker).start();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    this.close();
                } catch (IOException e1) {
                }
                break;//TODO : Need to refine here
            } finally {
            }
        }
    }

    /**
     * To do task that client requests
     * @author sdcsyyg
     */
    class Tasker implements Runnable {
        private Socket sck;
        private DataInputStream in;
        private DataOutputStream out;

        public Tasker(Socket sck) {
            this.sck = sck;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(sck.getInputStream());
                out = new DataOutputStream(sck.getOutputStream());
                String methodName = in.readUTF(); // Read method name here
                int argsLength = in.readInt(); // Read the length of args here
                String args[] = new String[argsLength]; 
                for(int i=0; i<argsLength; i++) { // Read args here
                    args[i] = in.readUTF();
                }
                String instruction = args[0];
                Method m = handler.getClass().getMethod(methodName, String.class);
                out.writeUTF((String)m.invoke(handler, instruction));
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(in != null) {
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        in = null;
                        out = null;
                    }
                }
            }
        }
    }

}
