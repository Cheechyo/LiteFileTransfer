package edu.lns.kwj.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class LiteFileClient {
    private int port = 8889;
    private Socket socket;

    public LiteFileClient() {

    }

    public static void main(String[] args) throws IOException {
        LiteFileClient client= new LiteFileClient();
        client.connect("localhost", 8889);
        client.writeMsg("hello\n");
        client.close();
    }

    public void close() throws IOException {
        if (!socket.isClosed())
            socket.close();
    }

    public void connect(String locslhost, int port) throws IOException {
        socket = new Socket("localhost", port);
    }

    public void writeMsg(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(str + "\n");
        writer.flush();
    }

}
