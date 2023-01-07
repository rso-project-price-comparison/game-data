package si.fri.rso.services.dtos;

import si.fri.rso.StoreEnum;

public record GogGameBySearchDto(String name, String appid, StoreEnum storeEnum) {
}
