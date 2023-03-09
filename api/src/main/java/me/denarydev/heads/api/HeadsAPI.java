/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api;

import me.denarydev.heads.api.head.HeadsManager;
import lombok.RequiredArgsConstructor;

/**
 * Api for Heads plugin
 */
public interface HeadsAPI {

    static HeadsAPI get() {
        return Holder.api;
    }

    HeadsManager getHeadsManager();

    @RequiredArgsConstructor
    class Holder {
        public static HeadsAPI api;
    }
}
