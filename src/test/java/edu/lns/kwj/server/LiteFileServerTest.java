package edu.lns.kwj.server;

import edu.lns.kwj.client.LiteFileClient;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class LiteFileServerTest {

    private int port = 8889;
    interface EventTestInterface{
        public void serverRun() throws IOException;
        public void clientRun() throws IOException;
    }
    @Test
    public void sync(){
        print("haha");
        (new Thread(new Runnable() {
            public void run() {
                try {
                    LiteFileServer server = new LiteFileServer(port, false);
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
                    client.writeMsg("handshake");
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

    @Test
    public void messageSync(){
        csTest(new EventTestInterface(){
            public void serverRun() throws IOException {
                LiteFileServer server = new LiteFileServer(port, false);
                server.open();
                print(server.readLine());
            }
            public void clientRun() throws IOException {
                LiteFileClient client = new LiteFileClient();
                client.connect("localhost", port);
                client.writeMsg("handshake");
                client.close();
            }
        });
    }

    @Test
    public void listSync(){
        csTest(new EventTestInterface(){
            public void serverRun() throws IOException {
                LiteFileServer server = new LiteFileServer(port, false);
                server.open();
                String msg = server.readLine();
                print("server received : " + msg);
                if (msg.equals("fileList")){
                    server.writeFileList();
                }
            }
            public void clientRun() throws IOException {
                LiteFileClient client = new LiteFileClient();
                client.connect("localhost", port);
                client.writeMsg("fileList");
                while(true){
                    String buf = client.readLine();
                    print(buf);
                }
                //client.close();
            }
        });
    }

    @Test
    public void fileGet(){
        csTest(new EventTestInterface(){
            public void serverRun() throws IOException {
                LiteFileServer server = new LiteFileServer(port, false);
                server.open();
                while (true) {
                    String msg = server.readLine();
                    print("server received : " + msg);
                    if (msg.equals("fileList")) {
                        server.writeFileList();
                    } else if (msg.split(":")[0].equals("File")){
                        server.writeFile(msg.split(":")[1]);
                    }
                }
            }
            public void clientRun() throws IOException {
                LiteFileClient client = new LiteFileClient();
                client.connect("localhost", port);
                List<String> fileList = client.getFileList();
                if (fileList.size() > 0) {
                    client.requestFile(fileList.get(2), "test.txt");
                    print("done");
                } else {

                }
                while(true){
                }
                //client.close();
            }
        });
    }

    @Test
    public void senario(){
        csTest(new EventTestInterface(){
            public void serverRun() throws IOException {
                LiteFileServer server = new LiteFileServer(port, false);
                server.open();
            }
            public void clientRun() throws IOException {
                LiteFileClient client = new LiteFileClient();
                client.connect("localhost", port);
                List<String> fileList = client.getFileList();
                if (fileList.size() > 0) {
                    client.requestFile(fileList.get(0), "./download/" + fileList.get(0).split(":")[1]);
                    print("done");
                } else {

                }
                client.close();
            }
        });
    }

    private void csTest(final EventTestInterface eventTestInterface) {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    eventTestInterface.serverRun();
                } catch (IOException e) {
                    print("server error");
                    e.printStackTrace();
                }
                print("server end");
            }
        })).start();
        (new Thread(new Runnable() {
            public void run() {
                try {
                    eventTestInterface.clientRun();
                } catch (IOException e) {
                    print("client error");
                    e.printStackTrace();
                }
                print("client end");
            }
        })).start();
        while (true)
            ;
    }

    private void print(String str) {
        System.out.println(str);
    }

    //@Test
    public void server_client_nosync() throws IOException {
        LiteFileServer server = new LiteFileServer(port, false);
        server.open();
        LiteFileClient client = new LiteFileClient();
        client.connect("localhost", port);
        client.writeMsg("hello");
        System.out.println(server.readLine());
        client.close();
    }
}