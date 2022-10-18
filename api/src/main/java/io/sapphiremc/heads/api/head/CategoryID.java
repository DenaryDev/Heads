/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.api.head;

/**
 * ID of the categories
 */
public enum CategoryID {

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
    CUSTOM;

    /**
     * Return name of this category in lower case.
     *
     * @return lower case name
     */
    public String toLower() {
        return this.name().toLowerCase();
    }

    /**
     * Return capitalized name of this category.
     *
     * @return capitalized name
     */
    public String toCapitalized() {
        return (this.name().charAt(0) + this.name().substring(1).toLowerCase()).replace("_", " ");
    }

    /**
     * Parse category id from string
     *
     * @param id category id string
     * @return {@link CategoryID}
     */
    public static CategoryID getCategoryID(String id) {
        id = id.toUpperCase();
        id = id.replace("-", "_");

        try {
            return CategoryID.valueOf(id);
        } catch (IllegalArgumentException ex) {
            return CategoryID.CUSTOM;
        }
    }
}
