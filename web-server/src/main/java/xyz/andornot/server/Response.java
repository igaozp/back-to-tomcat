package xyz.andornot.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * HTTP Response = Status-Line
 * * (( general-header | response-header | entity-header ) CRLF)
 * CRLF
 * [ message-body ]
 * Status-Line = HTTP_Version SP Status-Code SP Reason-Phrase CRLF
 */
public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResponse() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                // file not found
                String errorMessage = """
                        HTTP/1.1 404 File Not Found
                        Content-Type: text/html
                        Content-Length: 23
                                                
                        <h1>File Not Found</h1>
                        """;
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
