import LiquidKitSample
import SwiftUI
import UIKit

struct ContentView: View {
    var body: some View {
        if #available(iOS 26.0, *) {
            IosOwnedLiquidTabView()
        } else {
            ComposeFallbackView()
                .ignoresSafeArea(.all)
        }
    }
}

struct ComposeFallbackView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        LiquidKitSample.MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

@available(iOS 26.0, *)
enum IosOwnedTab: Hashable {
    case home
    case search
    case settings
}

@available(iOS 26.0, *)
struct IosOwnedLiquidTabView: View {
    @State private var selectedTab = IosOwnedTab.home

    var body: some View {
        TabView(
            selection: Binding(
                get: { selectedTab },
                set: { selectedTab = $0 }
            )
        ) {
            Tab("Home", systemImage: "house", value: IosOwnedTab.home) {
                ComposeTabRootView(
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.HomeViewController
                )
                .ignoresSafeArea(.all)
            }

            Tab("Search", systemImage: "magnifyingglass", value: IosOwnedTab.search) {
                ComposeTabRootView(
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.SearchViewController
                )
                .ignoresSafeArea(.all)
            }

            Tab("Settings", systemImage: "gearshape", value: IosOwnedTab.settings) {
                ComposeTabRootView(
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.SettingsViewController
                )
                .ignoresSafeArea(.all)
            }
        }
        .tabBarMinimizeBehavior(.automatic)
        .tint(.accentColor)
    }
}

@available(iOS 26.0, *)
struct ComposeTabRootView: UIViewControllerRepresentable {
    let makeRootViewController: () -> UIViewController

    func makeUIViewController(context: Context) -> UIViewController {
        makeRootViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
