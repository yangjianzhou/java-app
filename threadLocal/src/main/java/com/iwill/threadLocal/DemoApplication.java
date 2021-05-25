package com.iwill.threadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        ThreadLocal<String> threadLocal = new ThreadLocal<String>();
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<String>();
        inheritableThreadLocal.get();
        inheritableThreadLocal.set("");

        TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<String>();
        SpringApplication.run(DemoApplication.class);
    }
}
