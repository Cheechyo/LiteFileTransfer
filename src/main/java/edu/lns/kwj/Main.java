package edu.lns.kwj;

import edu.lns.kwj.server.LiteFileServer;

import java.io.*;

public class Main {

    private static final int port = 8889;

    public static void main(String[] args) throws IOException {
        LiteFileServer server = new LiteFileServer(port);
        server.open();
        println(server.readLine());
        println("----");
    }

    private static void println(String str) throws IOException {
        System.out.println(str);
    }
}
