/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.head;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Head class
 */
public interface Head {

    CategoryID categoryID();

    Category category();

    int id();

    String name();

    String base64();

    boolean hasTags();

    List<String> tags();

    String tagsString();

    ItemStack itemStack();
}
