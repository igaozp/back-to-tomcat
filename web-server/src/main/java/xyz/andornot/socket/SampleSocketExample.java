package xyz.andornot.socket;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SampleSocketExample {
    private static final Logger LOG = Logger.getLogger("SampleSocketExample");

    /**
     * start simple-server service before run this method
     *
     * @param args args
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        try (var socket = new Socket("127.0.0.1", 8080)) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // send an HTTP request to the web server
            printWriter.println("GET /index HTTP/1.1");
            printWriter.println("Host: localhost:8080");
            printWriter.println("Connection: Close");
            printWriter.println();

            // read the response
            boolean loop = true;
            StringBuilder sb = new StringBuilder();
            while (loop) {
                if (bufferedReader.ready()) {
                    int i = 0;
                    while (i != -1) {
                        i = bufferedReader.read();
                        sb.append((char) i);
                    }
                    loop = false;
                }
                Thread.sleep(50);
            }

            // display the response
            LOG.info(sb::toString);
        }
    }
}
