package systems.opalia.service.logging.impl.logback;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.filter.Filter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public abstract class PrintStreamAppender<E>
        extends OutputStreamAppender<E> {

    abstract Filter<E> getFilter();

    abstract PrintStream getPrintStream();

    @Override
    public void start() {

        OutputStream outputStream =
                new OutputStream() {

                    @Override
                    public void write(int value)
                            throws IOException {

                        getPrintStream().write(value);
                    }

                    @Override
                    public void write(byte[] bytes)
                            throws IOException {

                        getPrintStream().write(bytes);
                    }

                    @Override
                    public void write(byte[] bytes, int offset, int length)
                            throws IOException {

                        getPrintStream().write(bytes, offset, length);
                    }

                    @Override
                    public void flush()
                            throws IOException {

                        getPrintStream().flush();
                    }
                };

        addFilter(getFilter());
        setOutputStream(outputStream);
        super.start();
    }
}
