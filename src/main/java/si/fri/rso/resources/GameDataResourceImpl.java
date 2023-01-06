package si.fri.rso.resources;

import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import si.fri.rso.health.AliveSingleton;
import si.fri.rso.services.domain.ParserFetch;
import si.fri.rso.services.dtos.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;

@RequestScoped
public class GameDataResourceImpl implements GameDataResource {
    @Inject
    ParserFetch parserFetch;

    @Inject
    AliveSingleton singleton;

    private static final Marker ENTRY_MARKER = MarkerFactory.getMarker("ENTRY");
    private static final Marker OUT_MARKER = MarkerFactory.getMarker("OUT");

    Logger log = LoggerFactory.getLogger(GameDataResourceImpl.class);

    @Override
    public List<GameDto> getGames(String searchString) {

        log.info(ENTRY_MARKER, "Calling game data service: get games by search...");

        if (searchString == null) {
            throw new BadRequestException("Query parameter searchString is required");
        }

        List<GameBySearchDto> steamGames = Optional.ofNullable(parserFetch.getSteamGames(searchString)).orElse(new ArrayList<>());
        List<GameBySearchDto> gogGames = Optional.ofNullable(parserFetch.getGogGames(searchString)).orElse(new ArrayList<>());

        Map<String, GameDto> gamesMap = new HashMap<>(Collections.emptyMap());

        steamGames.forEach(g -> {
                List<StoreItem> storeItemList = new ArrayList<>();
                storeItemList.add(new StoreItem(g.appid(), g.storeEnum()));
                gamesMap.put(g.name(), new GameDto(g.name(), storeItemList));
        });

        gogGames.forEach(g -> {
            if(!gamesMap.containsKey(g.name())){
                List<StoreItem> storeItemList = new ArrayList<>();
                storeItemList.add(new StoreItem(g.appid(), g.storeEnum()));
                gamesMap.put(g.name(), new GameDto(g.name(), storeItemList));
            } else {
                GameDto gameDto = gamesMap.get(g.name());
                gameDto.storeItemList().add(new StoreItem(g.appid(), g.storeEnum()));
            }
        });

        ArrayList<GameDto> result = new ArrayList<>(gamesMap.values());

        if(result.isEmpty()) {
            log.info(OUT_MARKER, "Calling game data service: no games found.");
        } else {
            log.info(OUT_MARKER, "Calling game data service: games by search successfully fetched.");
        }

        return new ArrayList<>(gamesMap.values());
    }

    @Override
    public List<GamePriceDto> getPrices(List<PriceRequest> request) {

        log.info(ENTRY_MARKER, "Calling game data service: get prices...");

        List<String> gog = getIdsFromStore(request, StoreEnum.GOG);

        List<String> steam = getIdsFromStore(request, StoreEnum.STEAM);

        List<GamePriceDto> result = Streams.concat(parserFetch.getSteamPrices(steam).stream(), parserFetch.getGogPrices(gog).stream()).toList();

        if(result.isEmpty()) {
            log.info(OUT_MARKER, "Calling game data service: no prices found.");
        } else {
            log.info(OUT_MARKER, "Calling game data service: prices successfully fetched.");
        }

        return result;
    }

    private static List<String> getIdsFromStore(List<PriceRequest> request, StoreEnum store) {
        return request.stream().
                filter(priceRequest -> Objects.equals(priceRequest.storeEnum(), store))
                .map(PriceRequest::id)
                .toList();
    }

    public void enableLivenessCheck() {
        log.info(ENTRY_MARKER, "Calling game data service: enable liveness check...");
        singleton.setState(true);
        log.info(OUT_MARKER, "Calling game data service: liveness successfully enabled.");
    }

    public void disableLivenessCheck() {
        log.info(ENTRY_MARKER, "Calling game data service: disable liveness check...");
        singleton.setState(false);
        log.info(OUT_MARKER, "Calling game data service: liveness successfully disabled.");
    }

    // TODO remove, just check if consul properties are working
    //@ConfigProperty(name = "greeting.message", defaultValue="Hello from default")
    //String message;
    //@GET
    //@Override
    //public String hello() {
    //    return message;
    //}

}