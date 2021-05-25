package com.iwill.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {

    static ExecutorService executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) throws Exception {
        for (int i = 0 ; i <1000; i ++) {
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        sendMessage();
                    }catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
            });
        }
    }

    public static void sendMessage() throws Exception{
        /**
         * 向本机的8080端口发送请求
         */
        Socket socket = new Socket("127.0.0.1", 8182);
        /**
         * 通过socket得到输出流，构造PrintWriter对象
         */
        //Thread.sleep(60000L);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /**
         * 读取输入信息
         */
        for (int i = 0 ; i < 10 ; i++){
            writer.println(i);
            writer.flush();
            Thread.sleep(1000L);
            System.out.println("服务端响应为：" + reader.readLine());
        }
        //Thread.sleep(6000L);
        writer.println("exit");
        writer.flush();
        //Thread.sleep(10000L);
        writer.close();
        reader.close();
        socket.close();
    }
}
