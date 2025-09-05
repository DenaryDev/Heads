/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.head;

import me.denarydev.crystal.paper.item.ItemBuilder;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
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
        return Settings.messages().errors.noTags;
    }

    @Override
    public ItemStack itemStack() {
        if (base64 != null) {
            return ItemBuilder.playerHead(base64)
                .displayNameRich(Settings.menus().heads.items.head.name, Placeholder.component("name", Component.text(name)))
                .loreRich(Settings.menus().heads.items.head.lore,
                    Placeholder.parsed("category", category().getName()),
                    Placeholder.unparsed("tags", tagsString()),
                    Placeholder.unparsed("id", String.valueOf(id)))
                .build();
        } else {
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }
}
