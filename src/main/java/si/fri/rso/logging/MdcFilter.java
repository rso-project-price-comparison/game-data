package si.fri.rso.logging;

import io.quarkus.logging.LoggingFilter;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.MDC;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

@LoggingFilter(name = "mdc-filter")
public class MdcFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {

        MDC.put("app.name", ConfigProvider.getConfig().getValue("quarkus.application.name", String.class));
        MDC.put("app.env", ConfigProvider.getConfig().getValue("quarkus.application.version", String.class));
        MDC.put("app.version", ConfigProvider.getConfig().getValue("quarkus.profile", String.class));

        return true;
    }
}
