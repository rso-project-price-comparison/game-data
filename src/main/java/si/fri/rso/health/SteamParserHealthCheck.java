//package si.fri.rso.health;
//
//import io.smallrye.health.checks.UrlHealthCheck;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//import org.eclipse.microprofile.health.HealthCheck;
//import org.eclipse.microprofile.health.Readiness;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.ws.rs.HttpMethod;
//
//@ApplicationScoped
//public class SteamParserHealthCheck {
//
//    @ConfigProperty(name = "steam.url")
//    String steamUrl;
//
//    @Readiness
//    HealthCheck checkURL() {
//        return new UrlHealthCheck(steamUrl)
//                .name("Steam parser health check").requestMethod(HttpMethod.GET).statusCode(200);
//    }
//
//}