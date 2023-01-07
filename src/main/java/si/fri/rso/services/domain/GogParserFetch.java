package si.fri.rso.services.domain;

import java.time.Duration;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.fri.rso.services.GogParserService;
import si.fri.rso.services.dtos.GogGameBySearchDto;
import si.fri.rso.services.dtos.PriceDto;

@RequestScoped
public class GogParserFetch {
    Logger log = LoggerFactory.getLogger(GogParserFetch.class);

    @RestClient
    GogParserService gogParserService;


    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "fallbackGogGames")
    public List<GogGameBySearchDto> getGogGames(String searchString) {
//        timeouts after 250 ms with introduced random additional delay of up to 300ms
        return gogParserService.getGamesBySearchString(searchString);
    }

    public List<GogGameBySearchDto> fallbackGogGames(String searchString) {
        log.warn("Called GogParserFetch::fallbackGogGames");
        return gogParserService.getGamesBySearchStringFallback(searchString);
    }


    public List<PriceDto> getGogPrices(List<String> ids) {
        return gogParserService.getGamePrices(ids).await().atMost(Duration.ofSeconds(5));
    }

}
