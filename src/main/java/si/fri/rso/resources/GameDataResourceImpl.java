package si.fri.rso.resources;

import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Singleton;
import javax.ws.rs.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import si.fri.rso.GameBySearchDto;
import si.fri.rso.StoreEnum;
import si.fri.rso.health.AliveSingleton;
import si.fri.rso.services.domain.CircuitBreakerPriceFetchProxy;
import si.fri.rso.services.domain.GogParserFetch;
import si.fri.rso.services.domain.SteamParserFetch;
import si.fri.rso.services.dtos.*;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;

@Singleton
public class GameDataResourceImpl implements GameDataResource {
    @Inject
    SteamParserFetch steamParserFetch;

    @Inject
    GogParserFetch gogParserFetch;

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

        List<GameBySearchDto> steamGames = Optional.ofNullable(steamParserFetch.getSteamGames(searchString)).orElse(new ArrayList<>());
        List<GogGameBySearchDto> gogGames = Optional.ofNullable(gogParserFetch.getGogGames(searchString)).orElse(new ArrayList<>());

        Map<String, GameDto> gamesMap = new HashMap<>(Collections.emptyMap());

        steamGames.forEach(g -> {
            List<StoreItem> storeItemList = new ArrayList<>();
            storeItemList.add(new StoreItem(g.getAppid(), g.getStoreEnum()));
            gamesMap.put(g.getName(), new GameDto(g.getName(), storeItemList));
        });

        gogGames.forEach(g -> {
            if (!gamesMap.containsKey(g.name())) {
                List<StoreItem> storeItemList = new ArrayList<>();
                storeItemList.add(new StoreItem(g.appid(), StoreEnum.GOG));
                gamesMap.put(g.name(), new GameDto(g.name(), storeItemList));
            } else {
                GameDto gameDto = gamesMap.get(g.name());
                gameDto.storeItemList().add(new StoreItem(g.name(), StoreEnum.GOG));
            }
        });

        ArrayList<GameDto> result = new ArrayList<>(gamesMap.values());

        if (result.isEmpty()) {
            log.info(OUT_MARKER, "Calling game data service: no games found.");
        } else {
            log.info(OUT_MARKER, "Calling game data service: games by search successfully fetched.");
        }

        return new ArrayList<>(gamesMap.values());
    }
    private AtomicLong counter = new AtomicLong(0);

    @Override
    public List<PriceDto> getPrices(List<PriceRequest> request) {

        log.info(ENTRY_MARKER, "Calling game data service: get prices...");

        List<String> gog = getIdsFromStore(request, StoreEnum.GOG);

        List<String> steam = getIdsFromStore(request, StoreEnum.STEAM);

        final Long invocationNumber = counter.getAndIncrement();

        try {
            List<PriceDto> result = circuitBreakerPriceFetchProxy.getPriceDtos(gog, steam);
            log.warn(String.format("GameDataResourceImpl#getPrices() invocation #%d returning successfully", invocationNumber));

            if (result.isEmpty()) {
                log.info(OUT_MARKER, "Calling game data service: no prices found.");
            } else {
                log.info(OUT_MARKER, "Calling game data service: prices successfully fetched.");
            }

            return result;
        } catch (RuntimeException e) {
            String message = e.getClass().getSimpleName() + ": " + e.getMessage();
            log.error(String.format("GameDataResourceImpl#getPrices() invocation #%d failed: %s", invocationNumber, message));
            throw new ServiceUnavailableException("Circuit breaker");
        }
    }

    @Inject
    CircuitBreakerPriceFetchProxy circuitBreakerPriceFetchProxy;


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