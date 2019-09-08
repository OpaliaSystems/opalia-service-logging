package systems.opalia.service.logging.impl.logback;

import java.io.PrintStream;


public class PrintStreams {

    public final static PrintStream stdout = System.out;
    public final static PrintStream stderr = System.err;

    public static void stdoutBind(PrintStream printStream) {

        setOut(printStream);
    }

    public static void stderrBind(PrintStream printStream) {

        setErr(printStream);
    }

    public static void stdoutUnbind() {

        setOut(stdout);
    }

    public static void stderrUnbind() {

        setErr(stderr);
    }

    private static void setOut(PrintStream printStream) {

        System.setOut(printStream);
        scala.Console.setOut(printStream);
    }

    private static void setErr(PrintStream printStream) {

        System.setErr(printStream);
        scala.Console.setErr(printStream);
    }
}
