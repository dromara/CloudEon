package org.dromara.cloudeon.utils;

import cn.hutool.core.lang.func.Func1;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;

public class WebSocketSessionOutputStream extends OutputStream {

    private WebSocketSession webSocketSession;
    private StringBuilder buffer = new StringBuilder();

    private Func1<String, String> formatter;

    public WebSocketSessionOutputStream(WebSocketSession webSocketSession, Func1<String, String> formatter) {
        super();
        this.webSocketSession = webSocketSession;
        this.formatter = formatter;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        if (c == '\n') {
            flush();
        } else {
            buffer.append(c);
        }
    }

    @Override
    public void flush() throws IOException {
        if (!webSocketSession.isOpen()) {
            close();
        }
        if (buffer.length() > 0) {
            webSocketSession.sendMessage(new TextMessage(formatter.callWithRuntimeException(buffer.toString())));
            buffer.setLength(0);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

}
