package si.fri.rso.resources;

import si.fri.rso.services.dtos.GameDto;
import si.fri.rso.services.dtos.GamePriceDto;
import si.fri.rso.services.dtos.PriceRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/gamedata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameDataResource {

    @GET
    @Path("/game")
    List<GameDto> getGames(@QueryParam("searchString") String searchString);

    @POST
    @Path("/price")
    List<GamePriceDto> getPrices(List<PriceRequest> request);
}