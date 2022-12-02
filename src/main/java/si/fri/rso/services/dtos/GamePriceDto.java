package si.fri.rso.services.dtos;

public record GamePriceDto(String gameId ,Float finalPrice, String currency, StoreEnum storeEnum) {
}