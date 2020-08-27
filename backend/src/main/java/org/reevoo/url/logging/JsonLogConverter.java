package org.reevoo.url.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSONObject;

public class JsonLogConverter extends ClassicConverter {
    private JSONObject object = new JSONObject();

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        object.put("msg", iLoggingEvent.getMessage());
        object.put("level", iLoggingEvent.getLevel().levelStr);
        object.put("threadName", iLoggingEvent.getThreadName());
        object.put("method", iLoggingEvent.getLoggerName());
        return object.toString();
    }
}
