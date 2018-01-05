package edu.lns.kwj;

import edu.lns.kwj.client.MidtermFileClient;
import edu.lns.kwj.server.MidtermFileServer;

import java.io.IOException;
import java.util.List;

/**
 * 클라이언트를 실행하는 메인 클래스입니다
 * Created by Cheechyo on 2016. 10. 20..
 */
public class ClientMain {
    public static void main(String[] args) throws IOException {
        MidtermFileClient client = new MidtermFileClient(); // 서버 연결할 클라이언트 객체
        try {
            client.connect("localhost", E.port); // 서버와 연결하는 함수 welcome문자열을 받아오는것에 문제가 있어 삭제했습니다
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
    private static void println(String str) throws IOException {
        System.out.println(str);
    }
}
