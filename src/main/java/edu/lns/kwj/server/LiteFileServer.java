package edu.lns.kwj.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class LiteFileServer {
    private final int port;
    private PrintWriter out;
    private BufferedReader in;
    private final ServerSocket server;

    public LiteFileServer(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
    }

    public void open() throws IOException {
        Socket socket = server.accept();
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String readLine() throws IOException {
        return in.readLine();
    }
}
