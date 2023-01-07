package si.fri.rso.services.dtos;

import si.fri.rso.StoreEnum;

public record PriceDto(String gameId , Float finalPrice, String currency, StoreEnum storeEnum) {
}