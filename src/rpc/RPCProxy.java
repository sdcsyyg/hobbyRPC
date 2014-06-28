package rpc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

import business.Business;

/**
 * RPC proxy provider
 * @author sdcsyyg
 */
public class RPCProxy {

    public static Business getProxy(Class<? extends Business> businessClazz, InetSocketAddress addr) {
        Invoker invoker = null;
        Business proxy = null;
        try {
            // Do not need businessClazz here, because each single server is associated with a handler class
            invoker = new Invoker(addr);
            proxy = (Business)Proxy.newProxyInstance(businessClazz.getClassLoader(), new Class[]{businessClazz}, invoker);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return proxy;
    }

    private static class Invoker implements InvocationHandler {
        Socket socket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        InetSocketAddress address = null;
        private Invoker(InetSocketAddress address) throws IOException {
            this.address = address;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            connect();
            dos.writeUTF(method.getName());//Write method's name
            dos.writeInt(args.length); // Write the length of args
            for(int i=0; i<args.length; i++ ) { // Write args to server
                dos.writeUTF((String)args[i]); // The args here must be String
            }
            dos.flush();
            // Get the result from server
            // TODO : Need to refine here, should be multiple threads
            String report = dis.readUTF();
            disconnect();
            return report;
        }

        private void connect() throws IOException {
            socket = new Socket(address.getHostName(), address.getPort());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        }
        private void disconnect() throws IOException {
            dos.close();
            dis.close();
            socket.close();
        }

    }

}
