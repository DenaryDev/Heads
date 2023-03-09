package me.arcaniax.hdb.api.enums;

import me.denarydev.heads.api.head.CategoryID;

@Deprecated
public enum CategoryEnum {
    ALPHABET,
    ANIMALS,
    BLOCKS,
    DECORATION,
    FOOD_DRINKS,
    HUMANS,
    HUMANOID,
    MISCELLANEOUS,
    MONSTERS,
    PLANTS,
    CUSTOM,
    CUSTOM2,
    CUSTOM3,
    CUSTOM4,
    CUSTOM5,
    ONLINE_PLAYERS,
    DISABLED;

    public static CategoryEnum fromId(CategoryID id) {
        return switch (id) {
            case RECENT, FAVORITES, SEARCH, OTHER -> CUSTOM;
            default -> CategoryEnum.valueOf(id.name());
        };
    }
}
