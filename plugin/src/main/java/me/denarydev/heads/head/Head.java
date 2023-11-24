/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.head;

import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Head(CategoryID categoryID, int id, String name, String base64, boolean recent, List<String> tags) implements me.denarydev.heads.api.head.Head {

    @Override
    public Category category() {
        return HeadsPlugin.getInstance().getHeadDBManager().getCategory(categoryID);
    }

    @Override
    public boolean hasTags() {
        return this.tags != null && !this.tags.isEmpty();
    }

    @Override
    public String tagsString() {
        if (hasTags()) {
            StringBuilder builder = new StringBuilder();
            for (String s : tags) {
                if (!builder.isEmpty()) builder.append(", ");
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            }
            return builder.toString();
        }
        return "No Tags";
    }

    @Override
    public ItemStack itemStack() {
        return ItemUtils.itemBuilder()
                .itemStack(ItemUtils.getCustomHead(base64))
                .displaynameRich(Settings.menus().heads.items.head.name, Placeholder.unparsed("name", name))
                .loreRich(Settings.menus().heads.items.head.lore,
                        Placeholder.parsed("category", category().getName()),
                        Placeholder.unparsed("tags", tagsString()),
                        Placeholder.unparsed("id", String.valueOf(id)))
                .build();
    }
}
