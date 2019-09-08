package systems.opalia.service.logging.impl.logback;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import systems.opalia.interfaces.rendering.Renderer;


public class LoggingOutputStream
        extends OutputStream {

    private final Consumer<String> consume;
    private final ArrayList<Byte> buffer = new ArrayList<>();

    public LoggingOutputStream(Consumer<String> consume) {

        this.consume = consume;
    }

    public PrintStream createPrintStream() {

        try {

            return new PrintStream(this, true, Renderer.appDefaultCharset().name());

        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(int b) {

        buffer.add((byte) b);
    }

    @Override
    public void flush() {

        if (!buffer.isEmpty()) {

            byte[] array = new byte[buffer.size()];

            for (int i = 0; i < buffer.size(); i++)
                array[i] = buffer.get(i);

            if (buffer.stream().anyMatch(x ->
                    x != 0x09 && x != 0x0A && x != 0x0B && x != 0x0C && x != 0x0D && x != 0x20))
                consume.accept(new String(array, Renderer.appDefaultCharset()).trim());

            buffer.clear();
        }
    }
}
