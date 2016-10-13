package edu.lns.kwj.server;

import com.sun.istack.internal.NotNull;

import java.io.*;
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
    private Socket socket;

    public LiteFileServer(int port) throws IOException {
        this.port = port;
        server = new ServerSocket(port);
    }

    public void open() throws IOException {
        socket = server.accept();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public void writeFileList() throws IOException {
        if (out == null){
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        out.write(getFileList(".") + "ENDOFLIST\n");
        out.flush();
    }

    private String getFileList(@NotNull String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                sb.append("File:" + listOfFiles[i].getName() + "\n");
            } else if (listOfFiles[i].isDirectory()) {
                sb.append("Directory:" + listOfFiles[i].getName() + "\n");
            }
        }
        return sb.toString();
    }

    public void writeFile(String path) throws IOException {
        File f = new File(path);
        if (f.isFile() && f.canRead()){
            InputStream in = new FileInputStream(f);
            OutputStream out = socket.getOutputStream();
            int count = 0;
            byte[] buf = new byte[16*1024];
            count = in.read(buf);
            do {
                out.write(buf, 0, count);
                count = in.read(buf);
            } while (count > 0);
            out.flush();
        } else {

        }
    }
}
