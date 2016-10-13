package edu.lns.kwj.client;

import edu.lns.kwj.server.LiteFileServer;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class LiteFileClient {
    private int port = 8889;
    private Socket socket;
    private BufferedReader lineReader;

    public LiteFileClient() {

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

    public String readLine() throws IOException {
        if (lineReader == null)
            lineReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return  lineReader.readLine();
    }

    public List<String> getFileList() throws IOException {
        writeMsg(LiteFileServer.REQ_FILE_LIST);
        LinkedList<String> fileList = new LinkedList<String>();
        while(true){
            String line = readLine();
            if (line.split(LiteFileServer.LIST_DELIMITER)[0].equals("File")){
                fileList.add(line.split(LiteFileServer.LIST_DELIMITER)[1]);
            } else if (line.equals("ENDOFLIST")){
                break;
            }
        }
        return fileList;
    }

    public void requestFile(String fileName, String outPath) throws IOException {
        writeMsg("File" + LiteFileServer.LIST_DELIMITER + fileName);
        File file = new File(outPath);
        File parent = new File(file.getParent());
        // if the directory does not exist, create it
        if (!parent.exists()) {
            println("creating directory: " + fileName);
            boolean result = false;

            try{
                parent.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                println("DIR created : " + parent.getPath());
            }
        }
        byte[] buf = new byte[16*1024];
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int count = inputStream.read(buf);
        do {
            fileOutputStream.write(buf, 0, count);
            count = inputStream.read(buf);
        } while (count > 0);
        fileOutputStream.close();
    }

    private void println(String s) {
        System.out.println(s);
    }
}
