package si.fri.rso.services;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import si.fri.rso.services.dtos.GogGameBySearchDto;
import si.fri.rso.services.dtos.PriceDto;


@Path("/")
@RegisterRestClient(configKey = "gog-parser")
public interface GogParserService {

    @GET
    @Path("/game")
    List<GogGameBySearchDto> getGamesBySearchString(@QueryParam("searchString") String searchString);

    @GET
    @Path("/game/fallback")
    List<GogGameBySearchDto> getGamesBySearchStringFallback(@QueryParam("searchString") String searchString);

    @GET
    @Path("/price")
    Uni<List<PriceDto>> getGamePrices(@QueryParam("ids") List<String> ids);

}