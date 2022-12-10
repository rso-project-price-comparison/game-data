package si.fri.rso.resources;

import si.fri.rso.services.domain.ParserFetch;
import si.fri.rso.services.dtos.GameBySearchDto;
import si.fri.rso.services.dtos.GameDto;
import si.fri.rso.services.dtos.StoreItem;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;

public class GameDataResourceImpl implements GameDataResource {
    @Inject
    ParserFetch parserFetch;

    @Override
    public List<GameDto> getGames(String searchString) {
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



        return new ArrayList<>(gamesMap.values());
    }

    //@Override
    //public List<GameBySearchDto> getGamesBySearchString(String searchString) {
    //    if (searchString == null) {
    //        throw new BadRequestException("Query parameter searchString is required");
    //    }

    //    return SteamMapper.toGameBySearchDto(gameFetch.getGameBySearch(searchString));
    //}


}