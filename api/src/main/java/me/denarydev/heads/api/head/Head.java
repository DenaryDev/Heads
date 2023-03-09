/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.head;

import java.util.List;

/**
 * Head class
 */
public interface Head {

    CategoryID getCategoryID();

    Category getCategory();

    int getId();

    String getName();

    String getBase64();

    boolean hasTags();

    List<String> getTags();

    String getTagsString();
}
