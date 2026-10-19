/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads;

import io.sapphiremc.lib.configurate.ConfigurateException;
import io.sapphiremc.lib.configurate.objectmapping.ConfigSerializable;
import io.sapphiremc.lib.configurate.objectmapping.meta.Comment;
import io.sapphiremc.lib.configurate.serialize.TypeSerializerCollection;
import me.denarydev.crystal.config.ConfigLoaders;
import me.denarydev.crystal.config.ConfigMapper;
import me.denarydev.crystal.paper.configurate.PaperSerializers;
import me.denarydev.crystal.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
            .registerAll(PaperSerializers.get())
            .build();

        main = ConfigMapper.load(ConfigLoaders.yaml(path.resolve("config.yml"), serializers), MainConfig.class, false);
        menus = ConfigMapper.load(ConfigLoaders.yaml(path.resolve("menus.yml"), serializers), MenusConfig.class, false);
        messages = ConfigMapper.load(ConfigLoaders.yaml(path.resolve("messages.yml")), MessagesConfig.class, false);
    }

    @ConfigSerializable
    public static class MainConfig {
        @Comment("Режим отладки")
        public boolean debug = false;
        @Comment("Текстуры иконок категорий")
        public Categories categories = new Categories();
        @Comment("Отдельное право для каждой категории")
        public boolean perCategoryPermissions = false;
        @Comment("Отправлять ли сообщение о начале поиска")
        public boolean searchingMessage = true;
        @Comment("Поиск через чат по кнопке в меню")
        public Search search = new Search();
        @Comment("Скрывать ли в главком меню категории, в которых нет голов")
        public boolean showEmptyCategories = false;

        @ConfigSerializable
        public static class Search {
            @Comment("Слово, которое отменяет поиск")
            public String cancelWord = "отмена";
            @Comment("Сколько секунд ждать запрос, после чего поиск отменяется")
            public int promptTimeout = 60;
        }

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
        public Categories categories = new Categories();
        public Commands commands = new Commands();

        @ConfigSerializable
        public static class General {
            public Search search = new Search();

            @ConfigSerializable
            public static class Search {
                public String input = "<i>Введите поисковой запрос в чат или напишите <cancel> для отмены.";
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
        public static class Categories {
            public NameDescription alphabet = new NameDescription("Алфавит", List.of("<gray>Буквы, цифры и другие символы"));
            public NameDescription animals = new NameDescription("Животные", List.of("<gray>Головы в виде различных животных,", "<gray>Как реальных, так и выдуманных"));
            public NameDescription blocks = new NameDescription("Блоки", List.of("<gray>Уменьшенные версии всевозможных блоков"));
            public NameDescription decoration = new NameDescription("Декорации", List.of("<gray>ОЧЕНЬ много всевозможных украшений:", "<gray>Подарки, часы, кубик рубика и тд", "<gray>А так же ванильные предметы в виде голов)"));
            public NameDescription food_drinks = new NameDescription("Еда и напитки", List.of("<gray>Думаю, в объяснении нет необходимости)"));
            public NameDescription humans = new NameDescription("Люди", List.of("<gray>Всякие разные люди:", "<gray>Рыцари, пираты, девушки и тд"));
            public NameDescription humanoid = new NameDescription("Гуманоиды", List.of("<gray>Хиробрины, орки, пришельцы и тд"));
            public NameDescription miscellaneous = new NameDescription("Разнообразное", List.of("<gray>Тут есть много всего, включая", "<gray>Флаги стран, логотипы компаний, эмоции и тд"));
            public NameDescription monsters = new NameDescription("Монстры", List.of("<gray>Монстры, как майнкрафтовские, так и нет)"));
            public NameDescription plants = new NameDescription("Растения", List.of("<gray>Цветочки, ягодки и другая растительность"));
            public NameDescription other = new NameDescription("Другие головы", List.of("<gray>Категория для голов, которые", "<gray>не нашли своё место", "<gray>в других категориях"));
            public NameDescription recent = new NameDescription("Недавно добавленное", List.of("<gray>Самые свежие головы с сайта", "<gray>minecraft-heads.com"));
            public NameDescription favorites = new NameDescription("Избранное", List.of("<gray>Головы, помеченные вами как избранные"));
            public NameDescription search = new NameDescription("Результаты поиска", List.of("<gray>Всё, что нашлось по вашему запросу"));

            @ConfigSerializable
            public static class NameDescription {
                public String name;
                public List<String> description;

                NameDescription() {
                    // For Configurate
                }

                private NameDescription(final String name, final List<String> description) {
                    this.name = name;
                    this.description = description;
                }
            }
        }

        @ConfigSerializable
        public static class Commands {
            public Help help = new Help();
            public Open open = new Open();
            public Reload reload = new Reload();
            public Search search = new Search();
            public PlayerHead playerhead = new PlayerHead();

            @ConfigSerializable
            public static class Help {
                public String header = "<aqua>Heads <dark_gray>| <yellow>Доступные команды:\n<gray><i>Параметры в <> обязательны, а в [] не обязательны";
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
                public String success = "<yellow>Плагин перезагрузил конфигурацию и начал загрузку базы данных, это может занять некоторое время...";

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

            @ConfigSerializable
            public static class PlayerHead {
                public String info = "<i>Выдаёт голову игрока: свою или по нику.";
                public String usage = "/<label> [ник]";
                public String notFound = "<red>Не удалось найти скин игрока <name>!";
                public String success = "<green>Вы получили голову игрока <name>.";
                @Comment("Название предмета")
                public String itemName = "<yellow>Голова <name>";
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
                public ItemStack search = ItemBuilder.empty()
                    .type(Material.COMPASS)
                    .displayNameRich("<yellow>Поиск")
                    .loreRich(List.of(
                        "<gray>ЛКМ - поиск по названию и тегам",
                        "<gray>ПКМ - поиск только по названию",
                        "<gray>Или команда: /heads search <название|тег>"
                    ))
                    .build();
                public ItemStack back = ItemBuilder.empty()
                    .type(Material.BOOK)
                    .displayNameRich("<red>Вернуться к категориям")
                    .build();
                public ItemStack background = ItemBuilder.empty()
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
                        " ",
                        "<gray>В этой категории <count> голов"
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
                public ItemStack empty = ItemBuilder.empty()
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
