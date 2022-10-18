/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.api;

import io.sapphiremc.heads.api.head.HeadsManager;
import lombok.RequiredArgsConstructor;

/**
 * Api for SapphireHeads plugin
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
