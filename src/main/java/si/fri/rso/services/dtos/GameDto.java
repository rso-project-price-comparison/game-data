package si.fri.rso.services.dtos;

import java.util.List;

public record GameDto(String name, List<StoreItem> storeItemList) {
}