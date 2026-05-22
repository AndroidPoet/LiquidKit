import LiquidKitSample
import SwiftUI
import UIKit

struct ContentView: View {
    var body: some View {
        if #available(iOS 26.0, *) {
            NativeLiquidNavigationView()
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
@Observable
final class AppNavigationCoordinator {
    enum AppTab: Hashable {
        case home
        case search
        case settings
    }

    var selectedTab: AppTab = .home
    var homePath: [String] = []
    var searchPath: [String] = []
    var settingsPath: [String] = []

    func popToRoot(for tab: AppTab) {
        switch tab {
        case .home:
            homePath.removeAll()
        case .search:
            searchPath.removeAll()
        case .settings:
            settingsPath.removeAll()
        }
    }
}

@available(iOS 26.0, *)
struct NativeLiquidNavigationView: View {
    @State private var coordinator = AppNavigationCoordinator()

    var body: some View {
        TabView(
            selection: Binding(
                get: { coordinator.selectedTab },
                set: { coordinator.selectedTab = $0 }
            )
        ) {
            Tab("Home", systemImage: "house", value: AppNavigationCoordinator.AppTab.home) {
                TabContentView(
                    title: "Home",
                    path: $coordinator.homePath,
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.HomeViewController
                )
            }

            Tab("Search", systemImage: "magnifyingglass", value: AppNavigationCoordinator.AppTab.search) {
                TabContentView(
                    title: "Search",
                    path: $coordinator.searchPath,
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.SearchViewController
                )
            }

            Tab("Settings", systemImage: "gearshape", value: AppNavigationCoordinator.AppTab.settings) {
                TabContentView(
                    title: "Settings",
                    path: $coordinator.settingsPath,
                    makeRootViewController: LiquidKitSample.MainViewControllerKt.SettingsViewController
                )
            }
        }
        .tabBarMinimizeBehavior(.automatic)
        .tint(.accentColor)
    }
}

@available(iOS 26.0, *)
struct TabContentView: View {
    let title: String
    @Binding var path: [String]
    let makeRootViewController: () -> UIViewController

    var body: some View {
        NavigationStack(path: $path) {
            NativeNavComposeView(makeViewController: makeRootViewController)
                .ignoresSafeArea(.all)
                .navigationTitle(title)
                .navigationBarHidden(true)
        }
    }
}

@available(iOS 26.0, *)
struct NativeNavComposeView: UIViewControllerRepresentable {
    let makeViewController: () -> UIViewController

    func makeUIViewController(context: Context) -> UIViewController {
        makeViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
