/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Setter;
import me.denarydev.heads.api.head.HeadsManager;

/**
 * Api for Heads plugin
 */
public abstract class HeadsAPI {
    @Setter(AccessLevel.PACKAGE)
    private static HeadsAPI impl;

    public static HeadsAPI get() {
        Preconditions.checkNotNull(impl, "Api not initialized yet.");
        return impl;
    }

    public abstract HeadsManager getHeadsManager();
}
