package si.fri.rso.logging;

import io.quarkiverse.loggingjson.JsonGenerator;
import io.quarkiverse.loggingjson.JsonProvider;
import org.jboss.logmanager.ExtLogRecord;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class LoggingJsonProvider implements JsonProvider {

    @Override
    public void writeTo(JsonGenerator generator, ExtLogRecord event) throws IOException {

        Object marker = event.getMarker();

        if (marker != null) {
            generator.writeObjectField("marker", marker);
        }
    }
}