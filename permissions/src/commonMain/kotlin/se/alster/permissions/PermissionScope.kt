package se.alster.permissions

import androidx.compose.runtime.Composable

@Composable
expect fun PermissionScope(
    permission: Permission,
    onSuccess: @Composable () -> Unit,
    onFailure: @Composable (shouldShowRationale: Boolean, requestForPermissions: () -> Unit) -> Unit
)