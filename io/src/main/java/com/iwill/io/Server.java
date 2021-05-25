package com.iwill.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    static ExecutorService executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) throws Exception {
        /**
         * 创建ServerSocket监听端口8080
         */
        ServerSocket serverSocket = new ServerSocket(8182);
        /**
         * 等待客户端请求
         */
        Socket socket = null;
        Server server = new Server();
        while ((socket = serverSocket.accept()) != null) {
            executorService.execute(server.new SocketThread(socket, serverSocket));
        }

        serverSocket.close();
    }

    class SocketThread implements Runnable {

        private Socket socket;

        private ServerSocket serverSocket;

        public SocketThread(Socket socket, ServerSocket serverSocket) {
            this.socket = socket;
            this.serverSocket = serverSocket;
        }

        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String clientData = reader.readLine();
                while (!clientData.equals("exit")) {
                    writer.println("server response : " + clientData);
                    writer.flush();
                    System.out.println("客户端请求：" + clientData);
                    clientData = reader.readLine();
                }

                //Thread.sleep(10000L);
                reader.close();
                writer.close();
                socket.close();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }
}
