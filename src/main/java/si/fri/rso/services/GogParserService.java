package si.fri.rso.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.fri.rso.services.dtos.GameBySearchDto;
import si.fri.rso.services.dtos.GamePriceDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;


@Path("/")
@RegisterRestClient(configKey = "gog-parser")
public interface GogParserService {

    @GET
    @Path("/game")
    List<GameBySearchDto> getGamesBySearchString(@QueryParam("searchString") String searchString);

    @GET
    @Path("/price")
    List<GamePriceDto> getGamePrices(@QueryParam("ids") List<String> ids);

}