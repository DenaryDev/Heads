/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package me.denarydev.heads;

import me.denarydev.crystal.config.BukkitConfigs;
import me.denarydev.crystal.config.CrystalConfigs;
import me.denarydev.crystal.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;
import java.util.List;

public final class Settings {

    private static MainConfig main;
    private static MenusConfig menus;
    private static MessagesConfig messages;

    public static MainConfig main() {
        return main;
    }

    public static MenusConfig menus() {
        return menus;
    }

    public static MessagesConfig messages() {
        return messages;
    }

    public static void load(Path path) throws ConfigurateException {
        final var serializers = TypeSerializerCollection.builder()
                .registerAll(BukkitConfigs.serializers())
                .build();
        main = CrystalConfigs.loadConfig(path.resolve("config.conf"), serializers, MainConfig.class, false);
        menus = CrystalConfigs.loadConfig(path.resolve("menus.conf"), serializers, MenusConfig.class, false);
        messages = CrystalConfigs.loadConfig(path.resolve("messages.conf"), MessagesConfig.class, false);
    }

    @ConfigSerializable
    public static class MainConfig {
        @Comment("Режим отладки")
        public boolean debug = false;
        @Comment("Язык сообщений плагина")
        public String language = "ru_ru";
        @Comment("Текстуры иконок категорий")
        public Categories categories = new Categories();
        @Comment("Отдельное право для каждой категории")
        public boolean perCategoryPermissions = false;
        @Comment("Отправлять сообщение о начале поиска или нет?")
        public boolean searchingMessage = true;

        @ConfigSerializable
        public static class Categories {
            public String alphabet = "a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e";
            public String animals = "be8456155142cbe4e61353ffbaff304d3d9c4bc9247fc27b92e33e6e26067edd";
            public String blocks = "11ed9abf51fe4ea84cfcb27297f1bc54cd382edf85e7bd6e75ecca2b806611";
            public String decoration = "ce22391e35a3e5bcee89db312e874fdc9d9e7a6351314b82bda97fbd2be87eb8";
            public String food_drinks = "632545a9897c4f0809d7e9d7de4b499cafd8d6a42a0eacf4caabf3488adca211";
            public String humans = "ac8351d66e221eaf177c2ec4aa2db9f469b33ecb5c89476ed8a4a66a69880400";
            public String humanoid = "499d585a9abf59fae277bb684d24070cef21e35609a3e18a9bd5dcf73a46ab93";
            public String miscellaneous = "868f4cef949f32e33ec5ae845f9c56983cbe13375a4dec46e5bbfb7dcb6";
            public String monsters = "c45c11e0327035649ca0600ef938900e25fd1e38017422bc9740e4cda2cba892";
            public String plants = "158337a5285a6458543c77aa8f90c276ae537ad16a4e9af8b45733e99f4cbd";
            public String custom = "7f6f8e81330def176d2d52a59bf6af7b27d06acb75749fb1e943830d0162c945";
            public String emptyFavorites = "43dac8bc0da4eecabace0ede5fa6ab7573f2b6209b5ac88ac50943e57d8b170a";
            public String favorites = "9fd5f5a6cc552d237f7443f50b97b2ec033094cc08c59422b969f7a7e7048e06";
        }
    }

    @ConfigSerializable
    public static class MessagesConfig {
        public General general = new General();
        public Errors errors = new Errors();
        public Category category = new Category();
        public Commands commands = new Commands();

        @ConfigSerializable
        public static class General {
            public Search search = new Search();

            @ConfigSerializable
            public static class Search {
                public String input = "<i>Введите поисковой запрос или /cancel для отмены.";
                public String searching = "<gray><i>Выполняю поиск по '<args>'...";
                public String cancelled = "<gray><i>Поиск отменён.";
            }
        }

        @ConfigSerializable
        public static class Errors {
            public String noPermission = "<red>У вас недостаточно прав, чтобы использовать это!";
            public String playersOnly = "<red>Эту команду могут использовать только игроки!";
            public String databaseNotLoaded = "<red>База данных ещё не загрузилась, попробуйте позднее!";
            public String invalidConfiguration = "<red>Плагин не смог загрузиться из-за неправильной конфигурации! В консоли может быть больше информации об этом.";
            @Comment("Добавляется в описание головы в меню, если у неё нет тегов")
            public String noTags = "Тегов нет";
        }

        @ConfigSerializable
        public static class Category {
            public String alphabet = "Алфавит";
            public String animals = "Животные";
            public String blocks = "Блоки";
            public String decoration = "Декорации";
            public String food_drinks = "Еда и напитки";
            public String humans = "Люди";
            public String humanoid = "Гуманоиды";
            public String miscellaneous = "Разнообразное";
            public String monsters = "Монстры";
            public String plants = "Растения";
            public String other = "Другие головы";
            public String recent = "Недавно добавленное";
            public String favorites = "Избранное";
            public String search = "Результаты поиска";
        }

        @ConfigSerializable
        public static class Commands {
            public Help help = new Help();
            public Open open = new Open();
            public Reload reload = new Reload();
            public Search search = new Search();

            @ConfigSerializable
            public static class Help {
                public String header = "light_blue>Heads <dark_gray>| <yellow>Доступные команды:\n<gray><i>Параметры в <> обязательны, а в [] не обязательны";
                public String entry = "<dark_gray><b>*</b> <gold><usage> <gray>- <yellow><info>";
            }

            @ConfigSerializable
            public static class Open {
                public String info = "<i>Открывает меню голов.";
                public String usage = "/<label>";
            }

            @ConfigSerializable
            public static class Reload {
                public Database database = new Database();

                public String info = "<i>Перезагружает конфигурацию плагина.";
                public String usage = "/<label> reload";
                public String success = "<green>Конфигурация плагина успешно перезагружена.";

                @ConfigSerializable
                public static class Database {
                    public String success = "<green>База данных успешно загружена за <time>мс! Всего загружено <amount> голов.";
                    public String failed = "<red>Не удалось загрузить базу данных, в консоли может быть больше информации об этом!";
                }
            }

            @ConfigSerializable
            public static class Search {
                public String info = "<i>Ищет головы по названию и тегам.";
                public String usage = "/<label> search <название|тег>";
            }
        }
    }

    @ConfigSerializable
    public static class MenusConfig {
        public Common common = new Common();
        public Overview overview = new Overview();
        public Heads heads = new Heads();

        @ConfigSerializable
        public static class Common {
            public Items items = new Items();

            @ConfigSerializable
            public static class Items {
                public ItemStack search = ItemUtils.itemBuilder()
                        .type(Material.COMPASS)
                        .displayNameRich("<yellow>Поиск")
                        .loreRich(List.of(
                                "<gray>Для поиска используйте:",
                                "<gray>/heads search <название|тег>"
                        ))
                        .build();
                public ItemStack back = ItemUtils.itemBuilder()
                        .type(Material.BOOK)
                        .displayNameRich("<red>Вернуться к категориям")
                        .build();
                public ItemStack background = ItemUtils.itemBuilder()
                        .type(Material.GRAY_STAINED_GLASS_PANE)
                        .displayName(Component.space())
                        .build();
            }
        }

        @ConfigSerializable
        public static class Overview {
            public String title = "Головы (<count> загружено)";
            public Items items = new Items();

            @ConfigSerializable
            public static class Items {
                public TypeNameLore category = new TypeNameLore(
                        "<yellow><name>",
                        List.of(
                                "<gray><count> голов"
                        )
                );
            }
        }

        @ConfigSerializable
        public static class Heads {
            public String title = "<name> (<count> голов)";
            public Items items = new Items();

            @ConfigSerializable
            public static class Items {
                public TypeNameLore head = new TypeNameLore(
                        "<blue><name>",
                        List.of(
                                "<dark_gray><category>",
                                "<dark_gray><tags>",
                                "<dark_gray>ID: <gray><id>"
                        )
                );
                public TypeNameLore pageUnselected = new TypeNameLore(
                        Material.PAPER,
                        "<gray>Страница <page>",
                        List.of(
                                "<dark_gray><id>"
                        )
                );
                public TypeNameLore pageSelected = new TypeNameLore(
                        Material.MAP,
                        "<gray>Страница <page>",
                        List.of(
                                "<dark_gray><id>"
                        )
                );
                public ItemStack empty = ItemUtils.itemBuilder()
                        .type(Material.STRUCTURE_VOID)
                        .displayNameRich("<red>пусто")
                        .build();
            }
        }

        @ConfigSerializable
        public static class TypeNameLore {
            public Material type;
            public String name;
            public List<String> lore;

            TypeNameLore() {
                // For Configurate
            }

            public TypeNameLore(String name, List<String> lore) {
                this.name = name;
                this.lore = lore;
            }

            public TypeNameLore(Material type, String name, List<String> lore) {
                this.type = type;
                this.name = name;
                this.lore = lore;
            }
        }
    }
}
