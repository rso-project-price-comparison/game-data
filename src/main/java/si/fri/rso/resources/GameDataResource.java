package si.fri.rso.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import si.fri.rso.services.dtos.GameBySearchDto;
import si.fri.rso.services.dtos.GameDto;
import si.fri.rso.services.dtos.GamePriceDto;
import si.fri.rso.services.dtos.PriceRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/gamedata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "game-data", description = "Game data query")
public interface GameDataResource {

    @GET
    @Path("/game")
    @Operation(summary = "Get games by search string.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Found games.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = GameBySearchDto.class, type = SchemaType.ARRAY))}),
            @APIResponse(responseCode = "400", description = "Query parameter searchString is required",
                    content = @Content)})
    List<GameDto> getGames(@QueryParam("searchString") String searchString);

    @POST
    @Path("/price")
    @Operation(summary = "Get prices of games.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Found games.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = GamePriceDto.class, type = SchemaType.ARRAY))}),
            @APIResponse(responseCode = "400", description = "Query parameter ids is required",
                    content = @Content)})
    List<GamePriceDto> getPrices(List<PriceRequest> request);

    @POST
    @Path("liveness/enable")
    @Operation(summary = "Change application alive state to true.")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Alive state successfuly changed."),
            @APIResponse(responseCode = "400", description = "Query parameter state is required", content = @Content)
    })
    void enableLivenessCheck();

    @POST
    @Path("liveness/disable")
    @Operation(summary = "Change application alive state to false.")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Alive state successfuly changed."),
            @APIResponse(responseCode = "400", description = "Query parameter state is required", content = @Content)
    })
    void disableLivenessCheck();
}