package edu.lns.kwj.server;

import edu.lns.kwj.client.LiteFileClient;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class LiteFileServerTest {

    private int port = 8889;

    @Test
    public void sync(){
        print("haha");
        (new Thread(new Runnable() {
            public void run() {
                try {
                    LiteFileServer server = new LiteFileServer(port);
                    server.open();
                    print(server.readLine());
                    print("server end");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
        (new Thread(new Runnable() {
            public void run() {
                LiteFileClient client = new LiteFileClient();
                try {
                    client.connect("localhost", port);
                    client.writeMsg("hello");
                    client.close();
                    print("client end");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
        while (true)
            ;
    }

    private void print(String str) {
        System.out.println(str);
    }

    @Test
    public void server_client_nosync() throws IOException {
        LiteFileServer server = new LiteFileServer(port);
        server.open();
        LiteFileClient client = new LiteFileClient();
        client.connect("localhost", port);
        client.writeMsg("hello");
        System.out.println(server.readLine());
        client.close();
    }
}