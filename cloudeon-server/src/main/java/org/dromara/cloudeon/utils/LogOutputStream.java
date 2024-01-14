package org.dromara.cloudeon.utils;

import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.processor.TaskParam;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class LogOutputStream extends OutputStream {
    private TaskParam taskParam;
    //    private Logger logger;
    private StringFormatter formatter;
    private StringBuilder buffer = new StringBuilder();

    public LogOutputStream(TaskParam taskParam) {
        new LogOutputStream(taskParam, s -> s);
    }

    public LogOutputStream(TaskParam taskParam, StringFormatter formatter) {
        super();
        this.taskParam = taskParam;
        this.formatter = formatter;
    }

    @Override
    public void write(int b) {
        char c = (char) b;
        if (c == '\n') {
            flush();
        } else {
            buffer.append(c);
        }
    }

    @Override
    public void flush() {
        if (buffer.length() > 0) {
            LogUtil.logWithTaskId(taskParam, () -> log.info(formatter.format(buffer.toString())));
            buffer.setLength(0);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public interface StringFormatter {
        String format(String str);
    }

}
