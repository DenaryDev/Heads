/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.hdb;

import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.api.head.CategoryID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Head implements io.sapphiremc.heads.api.head.Head {

    @Getter
    private final CategoryID categoryID;
    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private final String base64;
    @Getter
    private final boolean recent;
    @Getter
    private final List<String> tags;

    @Override
    public Category getCategory() {
        return HeadsPlugin.getInstance().getHeadDBManager().getCategory(categoryID);
    }

    @Override
    public boolean hasTags() {
        return this.tags != null && !this.tags.isEmpty();
    }

    @Override
    public String getTagsString() {
        if (hasTags()) {
            StringBuilder builder = new StringBuilder();
            for (String s : tags) {
                if (builder.length() > 0) builder.append(", ");
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            }
            return builder.toString();
        }
        return "No Tags";
    }
}
