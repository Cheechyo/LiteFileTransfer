package edu.lns.kwj;

import edu.lns.kwj.client.MidtermFileClient;
import edu.lns.kwj.server.MidtermFileServer;

import java.io.*;
import java.util.List;

/**
 * 서버와 클라이언트를 동시에 실행해 진행하는(스레드) 메인 클래스입니다
 */
public class Main {

    /**
     * 서버와 클라이언트를 동시에 실행해 진행하는(스레드) 메인 함수입니다
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    MidtermFileServer server = new MidtermFileServer(E.port); // MidtermFileServer를 연다. 여기서 port상수는 25111이다
                    server.open(); // 서버를 여는 함수
                    while (true); // server.open이 비동기 처리기 때문에 메인 스레드라면 while문이 필요함
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                MidtermFileClient client = new MidtermFileClient(); // 서버 연결할 클라이언트 객체
                try {
                    client.connect("localhost", E.port); // 서버와 연결하는 함수 welcome문자열을 받아오는것에 문제가 있어 삭제했습니다
                    println(client.readLine());
                    List<String> fileList = client.getFileList();
                    println("server's File list (" + MidtermFileServer.DEFAULT_UPLOAD_PATH + ")");
                    for (String s: fileList) {
                        println(s);
                    }
                    println("-----서버의 파일 중 upload/test.txt를 받아 클라이언트의 download/test.txt에 저장합니다----");
                    if (fileList.size() > 0) {
                        client.requestFile("test.txt", "./download/test.txt"); // 서버의 test.txt를 요청
                        println("done");
                    } else {

                    }
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void println(String str) throws IOException {
        System.out.println(str);
    }
}
