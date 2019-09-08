package systems.opalia.service.logging.impl.logback;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.util.StatusPrinter;
import java.util.List;


public class OnPrintStreamStatusListener
        extends ContextAwareBase
        implements StatusListener, LifeCycle {

    private boolean started = false;
    private String prefix;
    private static final long DEFAULT_RETROSPECTIVE = 600;
    private long retrospective = DEFAULT_RETROSPECTIVE;

    public void addStatusEvent(Status status) {

        if (started && checkStatusLevel(status))
            print(status);
    }

    public void start() {

        started = true;

        if (retrospective > 0) {
            retrospectivePrint();
        }
    }

    public void stop() {

        started = false;
    }

    public boolean isStarted() {

        return started;
    }

    public String getPrefix() {

        return prefix;
    }

    public void setPrefix(String prefix) {

        this.prefix = prefix;
    }

    public void setRetrospective(long retrospective) {

        this.retrospective = retrospective;
    }

    public long getRetrospective() {

        return retrospective;
    }

    private void print(Status status) {

        StringBuilder sb = new StringBuilder();

        if (prefix != null)
            sb.append(prefix);

        StatusPrinter.buildStr(sb, "", status);
        PrintStreams.stderr.print(sb);
    }

    private void retrospectivePrint() {

        if (context != null) {

            List<Status> statusList = context.getStatusManager().getCopyOfStatusList();
            long now = System.currentTimeMillis();

            for (Status status : statusList)
                if (checkStatusLevel(status) && checkStatusTimestamp(status, now))
                    print(status);
        }
    }

    private boolean checkStatusLevel(Status status) {

        return status.getLevel() == Status.ERROR || status.getLevel() == Status.WARN;
    }

    private boolean checkStatusTimestamp(Status status, long timestamp) {

        return timestamp - status.getDate() < retrospective;
    }
}
