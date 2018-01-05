package edu.lns.kwj.client;

import edu.lns.kwj.server.MidtermFileServer;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class MidtermFileClient {
    private int port = 8889;
    private Socket socket;
    private BufferedReader lineReader;

    public MidtermFileClient() {

    }

    public void close() throws IOException {
        if (!socket.isClosed())
            socket.close();
    }

    public void connect(String locslhost, int port) throws IOException {
        socket = new Socket("localhost", port);
        /* welcome 메세지는 동작하지 않아 주석처리했습니다 */
        writeMsg(MidtermFileServer.REQ_WELCOME); // welcome문자열을 요청함
        println(readLine()); // welcome 메세지를 출력함
    }

    public void writeMsg(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(str + "\n");
        writer.flush();
    }

    /**
     * 서버쪽에서 보낸 문자열을 읽는 함수입니다
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        if (lineReader == null)
            lineReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return  lineReader.readLine();
    }

    /**
     * 서버쪽에 파일 리스트를 요청하고, 그 파일 리스트에서 파일(디렉토리 제외)을 구분해 출력하는 함수입니다
     * @return
     * @throws IOException
     */
    public List<String> getFileList() throws IOException {
        writeMsg(MidtermFileServer.REQ_FILE_LIST);
        LinkedList<String> fileList = new LinkedList<String>();
        while(true){
            String line = readLine();
            if (line.split(MidtermFileServer.LIST_DELIMITER)[0].equals("File")){
                fileList.add(line.split(MidtermFileServer.LIST_DELIMITER)[1]);
            } else if (line.equals("ENDOFLIST")){
                break;
            }
        }
        return fileList;
    }

    /**
     * 파일을 요청하는 get 메소드를 보내는 함수입니다.
     * @param fileName
     * @param outPath
     * @throws IOException
     */
    public void requestFile(String fileName, String outPath) throws IOException {
        writeMsg(MidtermFileServer.GET_FILE + " " + fileName);
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
