package edu.lns.kwj;

import edu.lns.kwj.client.LiteFileClient;
import edu.lns.kwj.server.LiteFileServer;

import java.io.*;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.print;

public class Main {

    public static void main(String[] args) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    LiteFileServer server = new LiteFileServer(E.port, false);
                    server.open(); // 서버를 여는 함수
                    while (true); // server.open이 비동기 처리기 때문에 메인 스레드라면 while문이 필요함
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                LiteFileClient client = new LiteFileClient();
                try {
                    client.connect("localhost", E.port);
                    List<String> fileList = client.getFileList();
                    println("server's File list");
                    for (String s: fileList) {
                        println(s);
                    }
                    println("-----리스트 첫번째 파일을 ./download/test.txt에 만들어 붙여넣습니다. ----");
                    if (fileList.size() > 0) {
                        client.requestFile(fileList.get(2), "./download/test.txt");
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
