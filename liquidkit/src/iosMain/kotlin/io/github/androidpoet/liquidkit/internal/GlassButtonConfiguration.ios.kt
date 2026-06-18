package io.github.androidpoet.liquidkit.internal

import platform.UIKit.UIButtonConfiguration

/**
 * Returns the iOS 26 system Liquid Glass [UIButtonConfiguration] for a button.
 *
 * @param prominent when true returns the opaque, primary-emphasis prominent glass configuration;
 *   otherwise the translucent secondary glass configuration.
 *
 * On a device/SDK that exposes these factories (iOS 26+) this yields authentic system Liquid Glass.
 */
internal fun glassButtonConfigurationFor(prominent: Boolean): UIButtonConfiguration =
    if (prominent) {
        UIButtonConfiguration.prominentGlassButtonConfiguration()
    } else {
        UIButtonConfiguration.glassButtonConfiguration()
    }
