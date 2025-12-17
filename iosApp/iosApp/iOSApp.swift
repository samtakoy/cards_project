import SwiftUI
import IosUmbrellaKit

@main
struct iOSApp: App {
    init() {
        IOSInitializer.shared.init()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}