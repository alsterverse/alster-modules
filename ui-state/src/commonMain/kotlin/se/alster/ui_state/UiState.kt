package se.alster.ui_state

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Data<out T>(val value: T) : UiState<T>
    data class Error(val value: Throwable) : UiState<Nothing>
}

fun <T> Result<T>.toUiState(): UiState<T> {
    onSuccess {
        return UiState.Data(it)
    }
    onFailure {
        return UiState.Error(it)
    }
    return UiState.Loading
}

fun <T : Any> T.toUiStateData(): UiState.Data<T> {
    return UiState.Data(this)
}

fun <T, R> UiState<T>.mapData(data: (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Data -> UiState.Data(data(value))
        is UiState.Error -> UiState.Error(value)
        is UiState.Loading -> UiState.Loading
        is UiState.Idle -> UiState.Idle
    }
}

fun <T> UiState<T>.dataOrNull(): T? {
    return when (this) {
        is UiState.Data -> value
        else -> null
    }
}
