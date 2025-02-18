import androidx.compose.ui.window.ComposeUIViewController
import se.alster.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
