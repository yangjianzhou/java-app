package com.iwill.io.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        /**
         * 向本机的8080端口发送请求
         */
        Socket socket = new Socket("127.0.0.1", 8080);
        BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
        /**
         * 通过socket得到输出流，构造PrintWriter对象
         */
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /**
         * 读取输入信息
         */
        String input = clientInput.readLine();
        while (!input.equals("exit")) {
            /**
             * 将输入信息发送到服务端
             */
            writer.println(input);
            /**
             * 刷新输出流，使服务器端可以立马收到请求信息
             */
            writer.flush();
            /**
             * 读取服务端返回信息
             */
            System.out.println("服务端响应为：" + reader.readLine());
            /**
             * 读取下一天输入信息
             */
            input = clientInput.readLine();
        }

        writer.close();
        reader.close();
        socket.close();
    }
}
