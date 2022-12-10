package si.fri.rso.resources;

import si.fri.rso.services.dtos.GameDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/gamedata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GameDataResource {

    @GET
    @Path("/games")
    List<GameDto> getGames(@QueryParam("searchString") String searchString);


    // TODO
    //@GET
    //@Path("/game")
    //getPrices(@QueryParam("searchString") String searchString);

    //@GET
    //@Path("/price")
    //List<GamePriceDto> getGamePrices(@QueryParam("ids") List<String> ids);
}