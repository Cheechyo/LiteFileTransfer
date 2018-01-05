package edu.lns.kwj;

import edu.lns.kwj.server.MidtermFileServer;

import java.io.IOException;

/**
 * 서버를 실행하는 메인 클래스입니다
 * Created by Cheechyo on 2016. 10. 20..
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        MidtermFileServer server = new MidtermFileServer(E.port); // MidtermFileServer를 연다. 여기서 port상수는 25111이다
        server.open(); // 서버를 여는 함수
        while (true); // server.open이 비동기 처리기 때문에 메인 스레드라면 while문이 필요함
    }


}
