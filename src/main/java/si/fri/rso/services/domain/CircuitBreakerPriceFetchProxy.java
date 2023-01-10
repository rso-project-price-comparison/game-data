package si.fri.rso.services.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import si.fri.rso.GamePriceDto;
import si.fri.rso.services.dtos.PriceDto;

@Singleton
public class CircuitBreakerPriceFetchProxy {

    @Inject
    GogParserFetch gogParserFetch;

    @Inject
    SteamParserFetch steamParserFetch;


    private AtomicLong counter = new AtomicLong(0);

    //    In this example, the circuit breaker will be in the half-open state if the failure ratio (the ratio of failed requests to total requests) exceeds 50%
//    and the request volume threshold (the minimum number of requests needed before the circuit breaker will trip) has been reached. When the circuit breaker is
//    in the half-open state, it will allow a limited number of requests to pass through and will delay subsequent requests by 1000 milliseconds.
//    If the service responds as expected, the circuit breaker will transition to the closed state. If the service does not respond as expected,
//    the circuit breaker will remain open and requests will continue to be blocked.
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
    public List<PriceDto> getPriceDtos(List<String> gog, List<String> steam, boolean circuitBreakerTest) {

        if(circuitBreakerTest)
            maybeFail();

        List<GamePriceDto> steamPrices = steamParserFetch.getSteamPrices(steam);
        List<PriceDto> gogPrices = gogParserFetch.getGogPrices(gog);
        List<PriceDto> result = Stream.concat(
                        steamPrices.stream()
                                .map(gpd -> new PriceDto(gpd.getGameId(), gpd.getFinalPrice(), gpd.getCurrency(), gpd.getStoreEnum())),
                        gogPrices.stream())
                .toList();
        return result;
    }

    private void maybeFail() {
        // introduce some artificial failures
        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
            throw new RuntimeException("Service failed.");
        }
    }
}
