/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package me.denarydev.heads.api;

import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.api.head.HeadsManager;

public class HeadsAPIImpl extends HeadsAPI {

    private final HeadsPlugin plugin;

    public HeadsAPIImpl(HeadsPlugin plugin) {
        this.plugin = plugin;
        HeadsAPI.setImpl(this);
    }

    @Override
    public HeadsManager getHeadsManager() {
        return plugin.getHeadDBManager();
    }
}
