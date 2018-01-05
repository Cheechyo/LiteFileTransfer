package edu.lns.kwj.server;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Cheechyo on 2016. 10. 13..
 */
public class MidtermFileServer {

    /* 기본 업로드 폴더 위치 */
    public static final String DEFAULT_UPLOAD_PATH = "./upload";
    /* 인사말 요청 메소드 */
    public static final String REQ_WELCOME = "welcome";
    /* 서버 인사말 */
    private static final String WELCOME_MSG = "Welcome to File Server\n";
    /* 파일 리스트 요청 메소드 */
    public static final String REQ_FILE_LIST = "fileList";
    /* 파일 요청 메소드 */
    public static final String GET_FILE = "get";
    /* 메소드와 파일 이름을 나누는 delimiter */
    public static final String LIST_DELIMITER = ":";

    private final int port;
    private final String uploadPath;


    private BufferedWriter out;
    private BufferedReader in;
    private final ServerSocket server;
    private Socket socket;

    private Thread serverThread;
    /* 서버쪽 로그 출력 여부 */
    private boolean printLog;

    public MidtermFileServer(int port) throws IOException {
        this.printLog = true;
        this.port = port;
        this.server = new ServerSocket(port);
        this.uploadPath = DEFAULT_UPLOAD_PATH;
    }

    public MidtermFileServer(int port, boolean printLog) throws IOException {
        this.printLog = printLog;
        this.port = port;
        this.server = new ServerSocket(port);
        this.uploadPath = DEFAULT_UPLOAD_PATH;
    }

    public MidtermFileServer(int port, boolean printLog, String uploadPath) throws IOException {
        this.printLog = printLog;
        this.port = port;
        this.server = new ServerSocket(port);
        this.uploadPath = uploadPath;
    }

    public void open() throws IOException {
        serverThread = new Thread(new Runnable() {
            public void run() {
                /*
                try {
                    MidtermFileServer.this.writeMsg("Welcome to File Server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                while (true) {
                    String msg;
                    try {
                        msg = MidtermFileServer.this.readLine();
                        Thread.sleep(1000);
                        print("server received : " + msg);
                        if (msg.equals(REQ_WELCOME)) {
                            writeMsg(WELCOME_MSG);
                        } else if (msg.equals(REQ_FILE_LIST)) {
                            MidtermFileServer.this.writeFileList();
                        } else {
                            if (msg.split(" ")[0].equals(GET_FILE)){
                                MidtermFileServer.this.writeFile(msg.split(" ")[1]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        socket = server.accept();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        serverThread.start();
    }

    private void print(String s) {
        if (printLog)
            System.out.println(s);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    /**
     * 서버의 파일 리스트를 클라이언트에게 보내는 함수.
     * @throws IOException
     */
    public void writeFileList() throws IOException {
        if (out == null){
            //out = new PrintWriter(socket.getOutputStream(), true);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        out.write(getFileList(uploadPath) + "ENDOFLIST\n");
        out.flush();
    }

    public void writeMsg(String msg) throws IOException{
        while (!socket.isConnected())
            ;
        if (out == null){
            //out = new PrintWriter(socket.getOutputStream(), true);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        out.write(msg + "\n");
        out.flush();
    }

    /**
     * 서버의 파일 리스트 문자열을 만드는 함수.
     * @param path
     * @return
     */
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
        File f = new File(uploadPath + "/" + path);
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
