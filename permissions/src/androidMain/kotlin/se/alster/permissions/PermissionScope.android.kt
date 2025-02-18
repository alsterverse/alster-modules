package se.alster.permissions

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun PermissionScope(
    permission: Permission,
    onSuccess: @Composable () -> Unit,
    onFailure: @Composable (shouldShowRationale: Boolean, requestForPermissions: () -> Unit) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        permission.toAndroidPermission()
    )

    if (cameraPermissionState.status.isGranted) {
        onSuccess()
    } else {
        onFailure(cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

private fun Permission.toAndroidPermission(): String {
    return when (this) {
        Permission.Microphone -> android.Manifest.permission.RECORD_AUDIO
    }
}