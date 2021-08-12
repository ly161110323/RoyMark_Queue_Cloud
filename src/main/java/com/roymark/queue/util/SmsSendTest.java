package com.roymark.queue.util;

import java.net.InetSocketAddress;
import java.net.Socket;

public class SmsSendTest
{
    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("10.249.41.66", 555), 3000);
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
