package test;

import application.Server;

public class ServerDriver {
    public static void main(String[] args) throws Exception {
        int port = 10000;
        System.out.println("ポート" + port + "でサーバーをたてます。");
        Server server = new Server(10000);
        server.acceptClient();
    }
}