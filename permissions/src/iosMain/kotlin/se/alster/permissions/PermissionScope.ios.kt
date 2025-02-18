package se.alster.permissions

import androidx.compose.runtime.Composable

@Composable
actual fun PermissionScope(
    permission: Permission,
    onSuccess: @Composable () -> Unit,
    onFailure: @Composable (shouldShowRationale: Boolean, requestForPermissions: () -> Unit) -> Unit
) {
    // TODO: Check if we are not allowed to use the permission.
    onSuccess()
}