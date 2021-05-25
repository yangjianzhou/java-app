package com.iwill.io.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception {
        /**
         * 创建ServerSocket监听端口8080
         */
        ServerSocket serverSocket = new ServerSocket(8080);
        /**
         * 等待客户端请求
         */
        Socket socket = serverSocket.accept();
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        System.out.println("客户端请求：" + reader.readLine());
        String input = serverInput.readLine();
        while (!input.equals("exit")) {
            writer.println(input);
            writer.flush();
            System.out.println("客户端请求：" + reader.readLine());

            input = serverInput.readLine();
        }

        reader.close();
        writer.close();
        socket.close();
        serverSocket.close();
    }
}
