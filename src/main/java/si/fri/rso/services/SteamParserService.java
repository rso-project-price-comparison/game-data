package si.fri.rso.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import si.fri.rso.GameBySearchDto;
import si.fri.rso.GamePriceDto;


@Path("/")
@RegisterRestClient(configKey = "steam-parser")
public interface SteamParserService {

    @GET
    @Path("/game")
    List<GameBySearchDto> getGamesBySearchString(@QueryParam("searchString") String searchString);

    @GET
    @Path("/price")
    List<GamePriceDto> getGamePrices(@QueryParam("ids") List<String> ids);

}