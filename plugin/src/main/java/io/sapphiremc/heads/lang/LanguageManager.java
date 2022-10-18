/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.lang;

import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.utils.Formatter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class LanguageManager {

    private final HeadsPlugin plugin;
    private final Map<String, FileConfiguration> languages = new HashMap<>();

    private String defaultLang;
    private boolean usePlayerLang;

    @SuppressWarnings("all")
    public void reload(@NotNull FileConfiguration config) {
        if (this.languages.size() > 0) languages.clear();

        usePlayerLang = config.getBoolean("lang.use-player-lang", true);
        if (!config.contains("lang.default")) {
            plugin.logWarning("Default language not specified in plugin config, using en_us");
            defaultLang = "en_us";
        } else {
            defaultLang = config.getString("lang.default");
        }

        plugin.logDebug("Loading language files...");
        File langDir = new File(plugin.getDataFolder() + File.separator + "lang");
        if (!langDir.exists()) langDir.mkdirs();

        List<String> names = List.of(langDir.list());

        if (!names.contains("en_us.yml")) {
            plugin.saveResource(new File("lang/en_us.yml").getPath(), false);
        }
        if (!names.contains("ru_ru.yml")) {
            plugin.saveResource(new File("lang/ru_ru.yml").getPath(), false);
        }

        for (File file : langDir.listFiles()) {
            if (!file.getName().endsWith(".yml")) {
                plugin.logDebug("Skipping file " + file.getName());
                continue;
            }

            languages.put(file.getName().replace(".yml", ""), plugin.getConfigManager().getConfig(file));
        }

        if (!languages.keySet().contains(defaultLang)) {
            plugin.logWarning("The language file " + defaultLang + ".yml does not exist in " + langDir.getPath() + " folder, using file en_us.yml");
        }

        plugin.logDebug("Successfully loaded " + languages.size() + " languages.");
    }

    @NotNull
    public Message getMessage(@NotNull String key) {
        return getMessage(null, key);
    }

    @NotNull
    public Message getMessage(@Nullable Player player, @NotNull String key) {
        if (getDefaultLang().isString(key)) {
            String stringMsg = getLangFile(player).getString(key, getDefaultLang().getString(key));
            if (stringMsg == null || stringMsg.isEmpty()) {
                stringMsg = getDefaultLang().getString(key);
                if (stringMsg == null || stringMsg.isEmpty()) {
                    stringMsg = "&c<missing key: " + key + ">";
                }
            }

            return new Message(stringMsg);
        } else {
            List<String> listMsg = getLangFile(player).getStringList(key);
            if (listMsg.isEmpty()) {
                listMsg = getDefaultLang().getStringList(key);
                if (listMsg.isEmpty()) {
                    listMsg = List.of("&c<missing key: " + key + ">");
                }
            }

            return new Message(listMsg);
        }
    }

    @NotNull
    public FileConfiguration getDefaultLang() {
        return languages.get(defaultLang);
    }

    @NotNull
    private FileConfiguration getLangFile(@Nullable Player player) {
        String langKey;
        if (player != null && usePlayerLang) {
            @SuppressWarnings("deprecation")
            String playerLang = player.getLocale();
            if (languages.containsKey(playerLang)) {
                langKey = playerLang;
            } else {
                plugin.logDebug("Cannot find language " + playerLang + " for player " + player.getName());
                langKey = defaultLang;
            }
        } else {
            langKey = defaultLang;
        }

        return languages.get(langKey);
    }

    public static class Message {
        private final MessageType type;
        private String msg;
        private List<String> listMsg;

        private Message(String msg) {
            this.type = MessageType.STRING;
            this.msg = msg;
        }

        private Message(List<String> listMsg) {
            this.type = MessageType.LIST;
            this.listMsg = listMsg;
        }

        public Message processPlaceholder(String placeholder, String replacement) {
            if (type == MessageType.LIST) {
                List<String> processed = new ArrayList<>();
                for (String s : listMsg) {
                    if (s.contains(placeholder)) {
                        processed.add(s.replace(placeholder, replacement));
                    } else {
                        processed.add(s);
                    }
                }
                this.listMsg = processed;
            } else {
                msg = msg.replace(placeholder, replacement);
            }

            return this;
        }

        public void send(CommandSender receiver) {
            if (type == MessageType.LIST) {
                receiver.sendMessage(asList().toArray(new String[0]));
            } else if (type == MessageType.STRING) {
                receiver.sendMessage(asString());
            }
        }

        public String asString() {
            return Formatter.format(msg);
        }

        public List<String> asList() {
            return Formatter.format(listMsg);
        }

        private enum MessageType {
            STRING, LIST
        }
    }
}
