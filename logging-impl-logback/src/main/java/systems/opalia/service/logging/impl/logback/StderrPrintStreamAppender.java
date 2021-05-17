package systems.opalia.service.logging.impl.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import java.io.PrintStream;


public class StderrPrintStreamAppender
        extends PrintStreamAppender<ILoggingEvent> {

    @Override
    Filter<ILoggingEvent> getFilter() {

        return new Filter<ILoggingEvent>() {

            @Override
            public FilterReply decide(ILoggingEvent event) {

                if (event.getLevel().equals(Level.WARN) ||
                        event.getLevel().equals(Level.ERROR))
                    return FilterReply.ACCEPT;
                else
                    return FilterReply.DENY;
            }
        };
    }

    @Override
    PrintStream getPrintStream() {

        return PrintStreams.stderr;
    }
}
