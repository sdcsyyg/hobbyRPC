package test;

import java.net.InetSocketAddress;

import rpc.RPCProxy;
import business.Business;

/**
 * As a client to test whether RPC works
 * @author sdcsyyg
 */
public class Client {

    public static final String HOST = "localhost";
    public static final int PORT = 21329;

    public static void main(String[] args) throws Exception {
        Business business = (Business) RPCProxy.getProxy(Business.class, new InetSocketAddress(HOST, PORT));

        String thisReport = business.doThis("Please DO THIS task, THX");
        System.out.println(thisReport);

        String thatReport = business.doThat("Please DO THAT task, THX");
        System.out.println(thatReport);
    }

}
