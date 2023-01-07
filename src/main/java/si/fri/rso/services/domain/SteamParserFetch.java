package si.fri.rso.services.domain;

import io.quarkus.grpc.GrpcClient;
import java.time.Duration;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import si.fri.rso.*;

@RequestScoped
public class SteamParserFetch {

    @GrpcClient
    GameService steamParserService;


    public List<GameBySearchDto> getSteamGames(String searchString) {
        return steamParserService.getGamesBySearchString(GetGamesBySearchStringRequest.newBuilder()
                        .setSearchString(searchString)
                        .build()).await()
                .atMost(Duration.ofSeconds(10))
                .getGamesList();
    }

    public List<GamePriceDto> getSteamPrices(List<String> ids) {
        return steamParserService.getGamePrices(GetGamePricesRequest.newBuilder()
                        .addAllIds(ids)
                        .build()).await().atMost(Duration.ofSeconds(5))
                .getGamePricesList();
    }
}
