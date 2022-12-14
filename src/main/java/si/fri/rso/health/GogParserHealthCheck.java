package si.fri.rso.health;

import io.smallrye.health.checks.UrlHealthCheck;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.HttpMethod;

@ApplicationScoped
public class GogParserHealthCheck {

    @ConfigProperty(name = "gog.url")
    String gogUrl;

    @Readiness
    HealthCheck checkURL() {
        return new UrlHealthCheck(gogUrl)
                .name("Gog parser health check").requestMethod(HttpMethod.GET).statusCode(200);
    }

}