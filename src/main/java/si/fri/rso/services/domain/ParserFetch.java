package si.fri.rso.services.domain;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import si.fri.rso.services.GogParserService;
import si.fri.rso.services.SteamParserService;
import si.fri.rso.services.dtos.GameBySearchDto;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class ParserFetch {

    @RestClient
    SteamParserService steamParserService;

    @RestClient
    GogParserService gogParserService;

    public List<GameBySearchDto> getSteamGames(String searchString) {
        return steamParserService.getGamesBySearchString(searchString);
    }

    public List<GameBySearchDto> getGogGames(String searchString) {
        return gogParserService.getGamesBySearchString(searchString);
    }

}
