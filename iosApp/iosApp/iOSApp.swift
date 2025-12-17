import SwiftUI
import IosUmbrellaKit

@main
struct iOSApp: App {
    init() {
        IOSInitializer.shared.inititialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}