package si.fri.rso;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@SuppressWarnings("RestResourceMethodInspection")
@OpenAPIDefinition(
        tags = {

                @Tag(name = "game-data", description = "Game data query"),
        },
        info = @Info(
                title = "Game data API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Budget gamers API Support",
                        email = "td1469@student.uni-lj.si"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class GameDataApplication extends Application {
}
