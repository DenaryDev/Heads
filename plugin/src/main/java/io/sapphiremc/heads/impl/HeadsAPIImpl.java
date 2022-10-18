/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.impl;

import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.api.HeadsAPI;
import io.sapphiremc.heads.api.head.HeadsManager;

public class HeadsAPIImpl implements HeadsAPI {

    private final HeadsPlugin plugin;

    public HeadsAPIImpl(HeadsPlugin plugin) {
        this.plugin = plugin;

        HeadsAPI.Holder.api = this;
    }

    @Override
    public HeadsManager getHeadsManager() {
        return plugin.getHeadDBManager();
    }
}
