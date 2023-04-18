/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads;

import me.denarydev.crystal.config.BukkitConfigs;
import me.denarydev.crystal.config.CrystalConfigs;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;

public final class Settings {

    private static MainConfig main;

    public static MainConfig main() {
        return main;
    }

    public static void load(Path path) throws ConfigurateException {
        final var serializers = TypeSerializerCollection.builder()
                .registerAll(BukkitConfigs.serializers())
                .build();
        main = CrystalConfigs.loadConfig(path.resolve("config.conf"), serializers, MainConfig.class, false);
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
}
