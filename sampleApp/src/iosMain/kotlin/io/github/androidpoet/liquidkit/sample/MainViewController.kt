package io.github.androidpoet.liquidkit.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    LiquidKitComposeOwnedTabShell()
}

fun HomeViewController(): UIViewController = tabRootViewController(LiquidKitSampleTab.Home)

fun SearchViewController(): UIViewController = tabRootViewController(LiquidKitSampleTab.Search)

fun SettingsViewController(): UIViewController = tabRootViewController(LiquidKitSampleTab.Settings)

private fun tabRootViewController(tab: LiquidKitSampleTab): UIViewController = ComposeUIViewController {
    LiquidKitSampleTabRoot(selectedTab = tab)
}
